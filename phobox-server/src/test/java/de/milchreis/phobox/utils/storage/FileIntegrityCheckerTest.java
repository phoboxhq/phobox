package de.milchreis.phobox.utils.storage;

import org.junit.Test;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static de.milchreis.phobox.helper.ConcurrentHelper.doItEvery;
import static org.junit.Assert.assertTrue;

public class FileIntegrityCheckerTest {

    @Test
    public void test_checkFile() {

        // Arrange
        File file = new File("src/test/resources/example-images/Canon-6D_01.jpg");
        long start = System.currentTimeMillis();
        doItEvery(TimeUnit.MILLISECONDS, 100,  4, () -> file.setLastModified(System.currentTimeMillis()));

        // Act
        FileIntegrityChecker.waitUntilIsComplete(file, 150);

        // Assert
        long end = System.currentTimeMillis();
        assertTrue(end - start > (150 * 3));
    }

}