package jp.yoshida.photos_admin.common.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import jp.yoshida.photos_admin.common.constant.KeyWordsConstants;
import jp.yoshida.photos_admin.common.constant.MessagesConstants;
import jp.yoshida.photos_admin.common.constant.StandardsConstants;
import jp.yoshida.photos_admin.common.exception.PhotosBusinessException;
import jp.yoshida.photos_admin.common.exception.PhotosSystemException;
import jp.yoshida.photos_admin.repository.entity.Photo;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.plugins.tiff.ExifGPSTagSet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * 写真の加工などを行うユーティリティ
 */
public class PhotosUtil {

    /**
     * サムネイル作成
     * @param sendingPhoto アップロードされた写真ファイル
     * @return サムネイル
     * @throws PhotosBusinessException 業務例外
     * @throws PhotosSystemException システム例外
     */
    @NonNull
    public static byte[] createThumbnail(
            @NonNull MultipartFile sendingPhoto) throws PhotosBusinessException, PhotosSystemException {

        Path tempPath;

        // サムネイルの作成に使用する一時ファイルの作成に失敗した場合、システム例外とする。
        try {
            tempPath = Files.createTempFile(Paths.get(StandardsConstants.JAVA_IO_TMPDIR),
                    StandardsConstants.TMPDIR_PREFIX, StandardsConstants.TMPDIR_SUFFIX);
            sendingPhoto.transferTo(tempPath);
        } catch (IOException e) {
            throw new PhotosSystemException(MessagesConstants.ERROR_TEMP_FILE_PROCESSING_FAILED);
        }

        // 画像イメージ処理中に例外が発生した場合、取り扱えない写真として業務例外とする。
        try {
            FileInputStream fileInputStream = new FileInputStream(String.valueOf(tempPath));
            BufferedImage originalImage = ImageIO.read(fileInputStream);
            fileInputStream.close();
            File tempFile = new File(String.valueOf(tempPath));

            /*
             * サムネイルの高さを固定値として、元の写真の高さからの縮尺を算出して、
             * 幅も同じ縮尺となるように設定することで縦横比を保ってサムネイルを作成する。
             */
            int originalHeight = originalImage.getHeight();
            int thumbnailHeight = KeyWordsConstants.THUMBNAIL_HEIGHT;
            float magnification = (float) KeyWordsConstants.THUMBNAIL_HEIGHT / originalHeight;
            int originalWidth = originalImage.getWidth();
            int thumbnailWidth = Math.round(originalWidth * magnification);

            BufferedImage thumbnail = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
            thumbnail.createGraphics().drawImage(
                    ImageIO.read(tempFile).getScaledInstance(thumbnailWidth, thumbnailHeight, Image.SCALE_SMOOTH),
                    KeyWordsConstants.NUMBER_ZERO, KeyWordsConstants.NUMBER_ZERO, null);
            Files.deleteIfExists(tempPath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, KeyWordsConstants.IMAGE_FORMAT_NAME_JPG, baos);

            return baos.toByteArray();
        } catch (Exception e) {
            throw new PhotosBusinessException(
                    MessagesConstants.ERROR_FILE_PROCESSING_FAILED,
                    MessagesConstants.INFO_LEVEL_ERROR);
        }
    }

    /**
     * メタデータ設定
     * @param photo 写真
     * @param sendingPhoto アップロードされた写真ファイル
     * @throws PhotosBusinessException 業務例外
     * @throws PhotosSystemException システム例外
     */
    public static void setMetaDatum(
            @NonNull Photo photo,
            @NonNull MultipartFile sendingPhoto) throws PhotosBusinessException, PhotosSystemException {

        Path tempPath;

        // メタデータの抽出に使用する一時ファイルの作成に失敗した場合、システム例外とする。
        try {
            tempPath = Files.createTempFile(Paths.get(StandardsConstants.JAVA_IO_TMPDIR),
                    StandardsConstants.TMPDIR_PREFIX, StandardsConstants.TMPDIR_SUFFIX);
            sendingPhoto.transferTo(tempPath);
        } catch (IOException e) {
            throw new PhotosSystemException(MessagesConstants.ERROR_TEMP_FILE_PROCESSING_FAILED);
        }

        Metadata metadata;

        // メタデータ抽出中に例外が発生した場合、取り扱えない写真として業務例外とする。
        try {
            File tempFile = new File(String.valueOf(tempPath));
            metadata = ImageMetadataReader.readMetadata(tempFile);
            Files.deleteIfExists(tempPath);
        } catch (Exception e) {
            throw new PhotosBusinessException(
                    MessagesConstants.ERROR_FILE_PROCESSING_FAILED,
                    MessagesConstants.INFO_LEVEL_ERROR);
        }

        Directory ifdDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

        if (Objects.nonNull(ifdDirectory)) {
            photo.setWidth(ifdDirectory.getDescription(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH));
            photo.setHeight(ifdDirectory.getDescription(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT));
            photo.setShootingDateTime(ifdDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
        }

        Directory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

        if (Objects.nonNull(gpsDirectory)) {
            photo.setLatitude(gpsDirectory.getDescription(ExifGPSTagSet.TAG_GPS_LATITUDE));
            photo.setLatitudeRef(gpsDirectory.getDescription(ExifGPSTagSet.TAG_GPS_LATITUDE_REF));
            photo.setLongitude(gpsDirectory.getDescription(ExifGPSTagSet.TAG_GPS_LONGITUDE));
            photo.setLongitudeRef(gpsDirectory.getDescription(ExifGPSTagSet.TAG_GPS_LONGITUDE_REF));
        }
    }

    /**
     * <p>文字列結合（NULL無視）</p>
     * <p>2つの文字列を区切り文字で区切って連結する。</p>
     * <p>結合する文字列が2つともNULLの場合にはNULLを返す。</p>
     * <p>結合する文字列のどちらか一方がNULLの場合は区切り文字を付けずに他方の値を返す。</p>
     * <p>区切り文字がNULLの場合は区切り文字を付けずに連結する。</p>
     * @param origin 元文字列
     * @param appendix 追加文字列
     * @param delimiter 区切り文字
     * @return 結合された文字列
     */
    @Nullable
    public static String concatIgnoreNull(
            @Nullable String origin,
            @Nullable String appendix,
            @Nullable String delimiter) {

        if (Objects.isNull(origin) && Objects.isNull(appendix)) {
            return null;
        }

        if (Objects.isNull(origin) || Objects.isNull(appendix) || Objects.isNull(delimiter)) {
            delimiter = KeyWordsConstants.SYMBOL_BLANK;
        }

        return Objects.toString(origin, KeyWordsConstants.SYMBOL_BLANK)
                + delimiter
                + Objects.toString(appendix, KeyWordsConstants.SYMBOL_BLANK);
    }
}
