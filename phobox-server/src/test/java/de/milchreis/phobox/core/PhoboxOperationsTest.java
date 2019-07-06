package de.milchreis.phobox.core;

import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.core.operations.PhoboxOperations;
import de.milchreis.phobox.core.operations.ThumbnailOperations;
import de.milchreis.phobox.utils.system.OSDetector;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class PhoboxOperationsTest {

    @Test
    public void test_createThumbPath() throws IOException {

        // Arrange
        PhoboxModel model = new PhoboxModel();
        model.setAutoSave(false);
        File file;

        if(OSDetector.getLocalOS().equals(OSDetector.OS.WINDOWS)) {
            file = new File("C:\\root\\path\\this\\is\\a\\path\\to\\file.CR2");
            model.setStoragePath("C:\\root\\path\\");
            model.setThumbPath(new File("C:\\root\\path\\phobox\\thumbs\\"));
        } else {
            file = new File("/root/path/this/is/a/path/to/file.CR2");
            model.setStoragePath("/root/path/");
            model.setThumbPath(new File("/root/path/phobox/thumbs/"));
        }

        // Act
        File thumbFile = new ThumbnailOperations(model).getPhysicalThumbnail(file);

        // Assert
        if(OSDetector.getLocalOS().equals(OSDetector.OS.WINDOWS)) {
            assertEquals("C:\\root\\path\\phobox\\thumbs\\this\\is\\a\\path\\to\\file.jpg", thumbFile.getAbsolutePath());
        } else {
            assertEquals("/root/path/phobox/thumbs/this/is/a/path/to/file.jpg", thumbFile.getAbsolutePath());
        }
    }

}