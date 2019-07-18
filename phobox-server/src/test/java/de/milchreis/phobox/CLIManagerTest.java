package de.milchreis.phobox;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.model.PhoboxModel;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class CLIManagerTest {

    @Test
    public void test_allValidArgs() throws IOException, ParseException {

        // Arrange
        Phobox.getModel().setAutoSave(false);

        // Act
        CLIManager.parse(new String[]{
                "-nw",
                "-s", "/path/to/storage",
                "-w", "/path/to/watchDir",
                "-p", "12000",
                "-b", "/path/to/backupDir",
                "-db",
        });

        // Assert
        PhoboxModel model = Phobox.getModel();
        assertFalse(model.isActiveGui());
        assertTrue(model.isDatabasebrowser());
        assertEquals("/path/to/storage", model.getStoragePath());
        assertEquals(new File("/path/to/watchDir").getAbsolutePath(), model.getWatchPath().getAbsolutePath());
        assertEquals(new File("/path/to/backupDir").getAbsolutePath(), model.getBackupPath().getAbsolutePath());
        assertEquals(new Integer(12000), model.getStorageConfiguration().getPhoboxPort());
    }


}