package de.milchreis.phobox.utils.image;

import com.drew.imaging.ImageProcessingException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ImportFormatterTest {

    @Test
    public void test_with_static_path() throws ImageProcessingException, IOException {

        // Arrange
        File relativePathToFile = new File("src/test/resources/example-images/Canon-6D_01.jpg");

        // Act
        ImportFormatter formatter = new ImportFormatter("/static/path/");
        File targetPathWithout = formatter.createPath(relativePathToFile);

        // Assert
        assertTrue(formatter.isValid());
        assertNotNull(targetPathWithout);
        assertEquals(new File("/static/path/").getPath(), targetPathWithout.getPath());
    }

    @Test
    public void test_with_data_path() throws ImageProcessingException, IOException {

        // Arrange
        File relativePathToFile = new File("src/test/resources/example-images/Canon-6D_01.jpg");
        System.out.println(relativePathToFile.getAbsolutePath());

        // Act
        ImportFormatter formatter = new ImportFormatter("/static/path/%Y-%M-%D");
        File targetPathWithout = formatter.createPath(relativePathToFile);

        // Assert
        assertTrue(formatter.isValid());
        assertNotNull(targetPathWithout);
        assertEquals(new File("/static/path/2019-06-10").getPath(), targetPathWithout.getPath());
    }

    @Test
    public void test_non_valid_format() {

        // Assert
        assertFalse(new ImportFormatter(null).isValid());
        assertFalse(new ImportFormatter("").isValid());
    }


}