package de.milchreis.phobox.utils.exif;

import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.junit.Assert.*;

public class RawToJpgTest {

    @Test(expected = IllegalArgumentException.class)
    public void test_getJpg_with_null() throws Exception {
        RawToJpg.getJpg(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getJpg_with_not_existing_file() throws Exception {
        RawToJpg.getJpg(new File("/this/file/not/exists.txt"));
    }

    @Test
    public void test_getJpg_with_valid_file() throws Exception {

        // Arrange
        File file = new File("src/test/resources/example-images/Canon-6D_01.CR2");

        // Act
        BufferedImage image = RawToJpg.getJpg(file);

        // Assert
        assertNotNull(image);
        assertEquals( 5472, image.getWidth());
        assertEquals( 3648, image.getHeight());
    }

}