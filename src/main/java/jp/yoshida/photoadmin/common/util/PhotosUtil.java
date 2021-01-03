package jp.yoshida.photoadmin.common.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
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

public class PhotosUtil {

    public static byte[] createThumbnail(MultipartFile rawPhoto, String extension) throws IOException {

        Path tempPath = PhotosUtil.createTempPath();
        rawPhoto.transferTo(tempPath);
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

    public static void setMetaDatum(PhotoDto photoDto) throws IOException, ImageProcessingException {

        Path tempPath = PhotosUtil.createTempPath();
        photoDto.getSendingPhoto().transferTo(tempPath);
        File tempFile = new File(String.valueOf(tempPath));
        Metadata metadata = ImageMetadataReader.readMetadata(tempFile);
        Directory ifdDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        Directory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
        photoDto.setWidth(ifdDirectory.getDescription(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH));
        photoDto.setHeight(ifdDirectory.getDescription(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT));
        photoDto.setShootingDateTime(ifdDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL));
        photoDto.setLatitude(gpsDirectory.getDescription(ExifGPSTagSet.TAG_GPS_LATITUDE));
        photoDto.setLatitudeRef(gpsDirectory.getDescription(ExifGPSTagSet.TAG_GPS_LATITUDE_REF));
        photoDto.setLongitude(gpsDirectory.getDescription(ExifGPSTagSet.TAG_GPS_LONGITUDE));
        photoDto.setLongitudeRef(gpsDirectory.getDescription(ExifGPSTagSet.TAG_GPS_LONGITUDE_REF));
    }

    public static Path createTempPath() throws IOException {

        String tmpdir = System.getProperty("java.io.tmpdir");

        if (tmpdir.endsWith(File.separator)) {
            tmpdir = tmpdir.substring(0, tmpdir.length() - 1);
        }

        return Files.createTempFile(Paths.get(tmpdir), "prefix", ".suffix");
    }
}
