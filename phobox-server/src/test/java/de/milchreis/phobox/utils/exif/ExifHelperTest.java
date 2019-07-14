package de.milchreis.phobox.utils.exif;

import com.drew.imaging.ImageProcessingException;
import de.milchreis.phobox.core.model.exif.ExifContainer;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ExifHelperTest {

    @Test
    public void test_getCreationDate() throws IOException, ImageProcessingException {

        // Arrange
        File file = new File("src/test/resources/example-images/Canon-6D_01.CR2");

        // Act
        Date creationDate = ExifHelper.getCreationDate(file);

        // Assert
        assertEquals("Mon Jun 10 13:43:46 CEST 2019", creationDate.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getCreationDate_with_null_input() throws Exception {
        // Act
        ExifHelper.getCreationDate(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void test_getCreationDate_with_not_existing_file() throws Exception {
        // Act
        ExifHelper.getCreationDate(new File("this/doesnt/exists.txt"));
    }

    @Test
    public void test_getOrientation() throws Exception {

        // Arrange
        File file = new File("src/test/resources/example-images/Canon-6D_01.CR2");

        // Act
        int orientation = ExifHelper.getOrientation(file);

        // Assert
        assertEquals(1, orientation);
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_getOrientation_with_null_input() throws Exception {
        // Act
        ExifHelper.getOrientation(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void test_getOrientation_with_not_existing_file() throws Exception {
        // Act
        ExifHelper.getOrientation(new File("this/doesnt/exists.txt"));
    }

    @Test
    public void test_getDimension() throws Exception {

        // Arrange
        File file = new File("src/test/resources/example-images/Canon-6D_01.CR2");

        // Act
        int[] dimension = ExifHelper.getDimension(file);

        // Assert
        assertEquals(4104, dimension[0]);
        assertEquals(2736, dimension[1]);
    }


    @Test(expected = IllegalArgumentException.class)
    public void test_getDimension_with_null_input() throws Exception {
        // Act
        ExifHelper.getDimension(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void test_getDimension_with_not_existing_file() throws Exception {
        // Act
        ExifHelper.getDimension(new File("this/doesnt/exists.txt"));
    }

    @Test
    public void test_getCamera() throws Exception {

        // Arrange
        File file = new File("src/test/resources/example-images/Canon-6D_01.CR2");

        // Act
        String[] camera = ExifHelper.getCamera(file);

        // Assert
        assertEquals("Canon", camera[0]);
        assertEquals("Canon EOS 6D", camera[1]);
    }


    @Test(expected = IllegalArgumentException.class)
    public void test_getCamera_with_null_input() throws Exception {
        // Act
        ExifHelper.getCamera(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void test_getCamera_with_not_existing_file() throws Exception {
        // Act
        ExifHelper.getCamera(new File("this/doesnt/exists.txt"));
    }

    @Test
    public void test_getExifDataMap() throws Exception {

        // Arrange
        File file = new File("src/test/resources/example-images/Canon-6D_01.CR2");

        // Act
        ExifContainer exifContainer = ExifHelper.getExifDataMap(file);

        // Assert
        assertEquals("f/3,2", exifContainer.getFNumber());
        assertEquals("1/500 sec", exifContainer.getExposureTime());
        assertEquals("100", exifContainer.getISO());
        assertEquals("85 mm", exifContainer.getFocalLength());
    }


    @Test(expected = IllegalArgumentException.class)
    public void test_getExifDataMap_with_null_input() throws Exception {
        // Act
        ExifHelper.getExifDataMap(null);
    }

    @Test(expected = FileNotFoundException.class)
    public void test_getExifDataMap_with_not_existing_file() throws Exception {
        // Act
        ExifHelper.getExifDataMap(new File("this/doesnt/exists.txt"));
    }

}