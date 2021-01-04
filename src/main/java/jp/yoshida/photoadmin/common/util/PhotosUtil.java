package jp.yoshida.photoadmin.common.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import jp.yoshida.photoadmin.common.constant.StandardConstants;
import jp.yoshida.photoadmin.dto.PhotoDto;
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

    public static byte[] createThumbnail(MultipartFile sendingPhoto, String extension) throws IOException {

        Path tempPath = Files.createTempFile(Paths.get(StandardConstants.JAVA_IO_TMPDIR),
                StandardConstants.TMPDIR_PREFIX, StandardConstants.TMPDIR_SUFFIX);
        sendingPhoto.transferTo(tempPath);
        BufferedImage originalImage = ImageIO.read(new FileInputStream(String.valueOf(tempPath)));
        File tempFile = new File(String.valueOf(tempPath));
        int originalHeight = originalImage.getHeight();
        int thumbnailHeight = 100;
        float magnification = (float) 100 / originalHeight;
        int originalWidth = originalImage.getWidth();
        int thumbnailWidth = Math.round(originalWidth * magnification);

        BufferedImage thumbnail = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
        thumbnail.createGraphics().drawImage(ImageIO.read(tempFile).getScaledInstance(
                thumbnailWidth, thumbnailHeight, Image.SCALE_SMOOTH),0,0,null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(thumbnail, extension, baos);

        return baos.toByteArray();
    }

    public static void setMetaDatum(
            PhotoDto photoDto, MultipartFile sendingPhoto) throws IOException, ImageProcessingException {

        Path tempPath = Files.createTempFile(Paths.get(StandardConstants.JAVA_IO_TMPDIR),
                StandardConstants.TMPDIR_PREFIX, StandardConstants.TMPDIR_SUFFIX);
        sendingPhoto.transferTo(tempPath);
        File tempFile = new File(String.valueOf(tempPath));
        Metadata metadata = ImageMetadataReader.readMetadata(tempFile);
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
