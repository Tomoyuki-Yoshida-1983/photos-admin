package jp.yoshida.photoadmin.common.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import jp.yoshida.photoadmin.common.constant.MessageConstants;
import jp.yoshida.photoadmin.common.constant.StandardConstants;
import jp.yoshida.photoadmin.common.exception.PhotosBusinessException;
import jp.yoshida.photoadmin.common.exception.PhotosSystemException;
import jp.yoshida.photoadmin.dto.PhotoDto;
import lombok.NonNull;
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
            @NonNull MultipartFile sendingPhoto,
            @NonNull String extension) throws PhotosBusinessException, PhotosSystemException {

        Path tempPath;

        try {
            tempPath = Files.createTempFile(Paths.get(StandardConstants.JAVA_IO_TMPDIR),
                    StandardConstants.TMPDIR_PREFIX, StandardConstants.TMPDIR_SUFFIX);
            sendingPhoto.transferTo(tempPath);
        } catch (IOException e) {
            throw new PhotosSystemException(MessageConstants.ERROR_TEMP_FILE_PROCESSING_FAILED);
        }

        try {
            BufferedImage originalImage = ImageIO.read(new FileInputStream(String.valueOf(tempPath)));
            File tempFile = new File(String.valueOf(tempPath));
            int originalHeight = originalImage.getHeight();
            int thumbnailHeight = 100;
            float magnification = (float) 100 / originalHeight;
            int originalWidth = originalImage.getWidth();
            int thumbnailWidth = Math.round(originalWidth * magnification);

            BufferedImage thumbnail = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
            thumbnail.createGraphics().drawImage(ImageIO.read(tempFile).getScaledInstance(
                    thumbnailWidth, thumbnailHeight, Image.SCALE_SMOOTH), 0, 0, null);
            Files.delete(tempPath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, extension, baos);

            return baos.toByteArray();
        } catch (Exception e) {
            throw new PhotosBusinessException(MessageConstants.ERROR_FILE_PROCESSING_FAILED);
        }
    }

    public static void setMetaDatum(
            @NonNull PhotoDto photoDto,
            @NonNull MultipartFile sendingPhoto) throws PhotosBusinessException, PhotosSystemException {

        Path tempPath;
        try {
            tempPath = Files.createTempFile(Paths.get(StandardConstants.JAVA_IO_TMPDIR),
                    StandardConstants.TMPDIR_PREFIX, StandardConstants.TMPDIR_SUFFIX);
            sendingPhoto.transferTo(tempPath);
        } catch (IOException e) {
            throw new PhotosSystemException(MessageConstants.ERROR_TEMP_FILE_PROCESSING_FAILED);
        }

        Metadata metadata;

        try {
            File tempFile = new File(String.valueOf(tempPath));
            metadata = ImageMetadataReader.readMetadata(tempFile);
            Files.delete(tempPath);
        } catch (Exception e) {
            throw new PhotosBusinessException(MessageConstants.ERROR_FILE_PROCESSING_FAILED);
        }

        Directory ifdDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

        if (Objects.nonNull(ifdDirectory)) {
            photoDto.setWidth(ifdDirectory.getDescription(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH));
            photoDto.setHeight(ifdDirectory.getDescription(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT));
            photoDto.setShootingDateTime(ifdDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
        }

        Directory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

        if (Objects.nonNull(gpsDirectory)) {
            photoDto.setLatitude(gpsDirectory.getDescription(ExifGPSTagSet.TAG_GPS_LATITUDE));
            photoDto.setLatitudeRef(gpsDirectory.getDescription(ExifGPSTagSet.TAG_GPS_LATITUDE_REF));
            photoDto.setLongitude(gpsDirectory.getDescription(ExifGPSTagSet.TAG_GPS_LONGITUDE));
            photoDto.setLongitudeRef(gpsDirectory.getDescription(ExifGPSTagSet.TAG_GPS_LONGITUDE_REF));
        }
    }
}
