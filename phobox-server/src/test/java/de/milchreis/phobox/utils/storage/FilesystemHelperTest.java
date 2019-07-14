package de.milchreis.phobox.utils.storage;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class FilesystemHelperTest {

    private File emptyDir;

    @After
    public void clean() {
        if(emptyDir != null && emptyDir.exists() && emptyDir.isDirectory()) {
            emptyDir.delete();
        }
    }


    @Test
    public void test_getRawIfExists_with_existing_file() {

        // Arrange
        File file = new File("src/test/resources/example-images/Canon-6D_01.jpg");

        // Act
        File rawFile = FilesystemHelper.getRawIfExists(file, new String[] {"NEF", "CR2"});

        // Assert
        assertNotNull(rawFile);
        assertEquals(new File("src/test/resources/example-images/Canon-6D_01.CR2").getAbsolutePath(), rawFile.getAbsolutePath());
        assertTrue(rawFile.exists());
    }

    @Test
    public void test_getRawIfExists_without_existing_file() {

        // Arrange
        File file = new File("src/test/resources/example-images/Canon-6D_01.jpg");

        // Act
        File rawFile = FilesystemHelper.getRawIfExists(file, new String[] {"NEF"});

        // Assert
        assertNull(rawFile);
    }

    @Test
    public void test_isDirEmpty() throws IOException {

        emptyDir = new File("src/test/resources/emptyDir");
        emptyDir.mkdirs();

        assertFalse(FilesystemHelper.isDirEmpty(Paths.get("./")));
        assertTrue(FilesystemHelper.isDirEmpty(emptyDir.toPath()));
    }

}