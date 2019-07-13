package de.milchreis.phobox.utils.image;

import org.imgscalr.Scalr;
import org.junit.After;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.junit.Assert.*;

public class ImageProcessingTest {

    private File workingFile;

    @After
    public void clean() {
        if(workingFile != null && workingFile.isFile())
            workingFile.delete();
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getJpg_with_null() throws Exception {
        ImageProcessing.getImage(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getJpg_with_not_existing_file() throws Exception {
        ImageProcessing.getImage(new File("/this/file/not/exists.txt"));
    }

    @Test
    public void test_getImage_with_valid_file() throws Exception {

        // Arrange
        File file = new File("src/test/resources/example-images/Canon-6D_01.jpg");

        // Act
        BufferedImage image = ImageProcessing.getImage(file);

        // Assert
        assertNotNull(image);
        assertEquals(5472, image.getWidth());
        assertEquals(3648, image.getHeight());
    }

    @Test
    public void test_scaling() throws Exception {

        // Arrange
        File file = new File("src/test/resources/example-images/Canon-6D_01.jpg");

        // Act
        BufferedImage image = ImageProcessing.scale(ImageProcessing.getImage(file), 150, 100);

        // Assert
        assertNotNull(image);
        assertEquals(150, image.getWidth());
        assertEquals(100, image.getHeight());
    }

    @Test
    public void test_scaling_with_fileoutput() throws Exception {

        // Arrange
        File file = new File("src/test/resources/example-images/Canon-6D_01.jpg");
        workingFile = new File("src/test/resources/example-images/Canon-6D_01_copy.jpg");

        // Act
        ImageProcessing.scale(ImageProcessing.getImage(file), workingFile, 150, 100);

        // Assert
        assertTrue(workingFile.exists());

        BufferedImage scaledImage = ImageProcessing.getImage(workingFile);
        assertEquals(150, scaledImage.getWidth());
        assertEquals(100, scaledImage.getHeight());
    }

    @Test
    public void test_rotate() throws Exception {

        // Arrange
        File file = new File("src/test/resources/example-images/Canon-6D_01.jpg");
        workingFile = new File("src/test/resources/example-images/Canon-6D_01_copy.jpg");

        // Act
        ImageProcessing.rotate(file, workingFile, Scalr.Rotation.CW_90);

        // Assert
        assertNotNull(workingFile);

        BufferedImage originalImage = ImageProcessing.getImage(file);
        assertEquals(5472, originalImage.getWidth());
        assertEquals(3648, originalImage.getHeight());

        BufferedImage rotatedImage = ImageProcessing.getImage(workingFile);
        assertEquals(3648, rotatedImage.getWidth());
        assertEquals(5472, rotatedImage.getHeight());
    }

}