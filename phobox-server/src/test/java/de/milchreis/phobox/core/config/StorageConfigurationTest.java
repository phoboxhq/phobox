package de.milchreis.phobox.core.config;

import de.milchreis.phobox.core.model.UserCredentials;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.*;

public class StorageConfigurationTest {

    @After
    public void cleanup() {
        File testing = new File("src/test/resources/test.config.yaml");
        if(testing.isFile())
            testing.delete();
    }


    @Test
    public void test_loadingConfigurationFile() throws IOException {

        // Arrange
        File config = new File("src/test/resources/example.config.yaml");

        // Act
        StorageConfiguration configuration = StorageConfiguration.load(config);

        // Assert
        assertNotNull(configuration);

        assertEquals(new Integer(9001), configuration.getPhoboxPort());
        assertEquals("23:10", configuration.getStorageScan());
        assertEquals("%Y-%M-%D/", configuration.getImportFormat());

        assertNotNull(configuration.getLoginCredentials());
        assertEquals(1, configuration.getLoginCredentials().size());

        UserCredentials userCredentials = configuration.getLoginCredentials().get(0);
        assertEquals("nick", userCredentials.getUsername());
        assertEquals("$2a$10$k1n4tz2YbqXbeQ9Cm5ZUkOG84/9hRjOTh1ERI2gCkAskkyDQFqxtW", userCredentials.getPassword());

    }

    @Test
    public void test_writingConfigurationFile() throws IOException {

        // Arrange
        StorageConfiguration configuration = StorageConfiguration.load(new File("src/test/resources/example.config.yaml"));
        configuration.setPhoboxPort(12000);

        // Act
        File config = new File("src/test/resources/test.config.yaml");
        configuration.write(config);

        // Assert
        assertTrue(config.exists());
        assertTrue(config.isFile());

        String writtenConfig = new String(Files.readAllBytes(config.toPath()));
        assertTrue(writtenConfig.contains("phoboxPort: 12000"));

    }

    @Test
    public void test_storageScantime() throws IOException {

        // Arrange
        StorageConfiguration validConfig = StorageConfiguration.load(new File("src/test/resources/example.config.yaml"));

        StorageConfiguration invalidConfig = StorageConfiguration.load(new File("src/test/resources/example.config.yaml"));
        invalidConfig.setStorageScan("40:80");

        // Act/Assert
        assertTrue(validConfig.hasValidStorageScanTime());
        assertFalse(invalidConfig.hasValidStorageScanTime());
    }

}