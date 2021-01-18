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

public class PhotosUtil {

    @NonNull
    public static byte[] createThumbnail(
            @NonNull MultipartFile sendingPhoto) throws PhotosBusinessException, PhotosSystemException {

        Path tempPath;

        try {
            tempPath = Files.createTempFile(Paths.get(StandardsConstants.JAVA_IO_TMPDIR),
                    StandardsConstants.TMPDIR_PREFIX, StandardsConstants.TMPDIR_SUFFIX);
            sendingPhoto.transferTo(tempPath);
        } catch (IOException e) {
            throw new PhotosSystemException(MessagesConstants.ERROR_TEMP_FILE_PROCESSING_FAILED);
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(String.valueOf(tempPath));
            BufferedImage originalImage = ImageIO.read(fileInputStream);
            fileInputStream.close();
            File tempFile = new File(String.valueOf(tempPath));
            int originalHeight = originalImage.getHeight();
            int thumbnailHeight = 100;
            float magnification = (float) 100 / originalHeight;
            int originalWidth = originalImage.getWidth();
            int thumbnailWidth = Math.round(originalWidth * magnification);

            BufferedImage thumbnail = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
            thumbnail.createGraphics().drawImage(ImageIO.read(tempFile).getScaledInstance(
                    thumbnailWidth, thumbnailHeight, Image.SCALE_SMOOTH), 0, 0, null);
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

    public static void setMetaDatum(
            @NonNull Photo photo,
            @NonNull MultipartFile sendingPhoto) throws PhotosBusinessException, PhotosSystemException {

        Path tempPath;
        try {
            tempPath = Files.createTempFile(Paths.get(StandardsConstants.JAVA_IO_TMPDIR),
                    StandardsConstants.TMPDIR_PREFIX, StandardsConstants.TMPDIR_SUFFIX);
            sendingPhoto.transferTo(tempPath);
        } catch (IOException e) {
            throw new PhotosSystemException(MessagesConstants.ERROR_TEMP_FILE_PROCESSING_FAILED);
        }

        Metadata metadata;

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
