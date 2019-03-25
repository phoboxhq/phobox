package de.milchreis.phobox.core;

import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.utils.system.OSDetector;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class PhoboxOperationsTest {

    @Test
    public void test_createThumbPath() {

        // Arrange
        PhoboxModel model = new PhoboxModel();
        model.setAutoSave(false);
        File file;

        if(OSDetector.getLocalOS().equals(OSDetector.OS.WINDOWS)) {
            file = new File("C:\\root\\path\\this\\is\\a\\path\\to\\file.CR2");
            model.setStoragePath("C:\\root\\path\\");
        } else {
            file = new File("/root/path/this/is/a/path/to/file.CR2");
            model.setStoragePath("/root/path/");
        }

        // Act
        PhoboxOperations ops = new PhoboxOperations(model);
        File thumbFile = ops.getThumb(file);

        // Assert
        if(OSDetector.getLocalOS().equals(OSDetector.OS.WINDOWS)) {
            assertEquals("C:\\root\\path\\phobox\\thumbs\\this\\is\\a\\path\\to\\file.jpg", thumbFile.getAbsolutePath());
        } else {
            assertEquals("/root/path/phobox/thumbs/this/is/a/path/to/file.jpg", thumbFile.getAbsolutePath());
        }
    }

}