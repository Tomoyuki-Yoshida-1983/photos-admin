package jp.yoshida.photoadmin.common.util;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
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

        String tmpdir = System.getProperty("java.io.tmpdir");
        if (tmpdir.endsWith(File.separator)) {
            tmpdir = tmpdir.substring(0, tmpdir.length() - 1);
        }

        Path tempPath = Files.createTempFile(Paths.get(tmpdir), "prefix", ".suffix");
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

}
