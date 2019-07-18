package de.milchreis.phobox.core.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.milchreis.phobox.core.model.UserCredentials;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Data
public class StorageConfiguration {

    private String importFormat = "%Y/%Y-%M/%Y-%M-%D";
    private List<UserCredentials> loginCredentials;
    private String storageScan = "23:00";
    private Integer phoboxPort = 8080;

    public static StorageConfiguration load(File storageConfigFile) throws IOException {
        if(storageConfigFile == null || !storageConfigFile.exists()) {
            return new StorageConfiguration();

        } else {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readValue(storageConfigFile, StorageConfiguration.class);
        }
    }

    public void write(File storageConfigFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.writeValue(storageConfigFile, this);
    }

    public boolean hasValidStorageScanTime() {

        if(storageScan == null || storageScan.isEmpty() || !storageScan.contains(":"))
            return false;

        try {
            int[] time = getStorageScantime();

            int hour = time[0];
            if(hour > 23 || hour < 0) {
                log.warn("Hour is not between 0 and 23");
                return false;
            }

            int minute = time[1];
            if(minute > 59 || minute < 0) {
                log.warn("Minute is not between 0 and 59");
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("StorageScan property as no valid format: " + storageScan + " - Expects HH:MM", e);
            return false;
        }
    }

    @JsonIgnore
    public int[] getStorageScantime() {
        try {
            String[] parts = storageScan.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);

            return new int[]{hours, minutes};

        } catch (Exception e) {
            throw new IllegalArgumentException("StorageScan property as no valid format: " + storageScan + " - Expects HH:MM");
        }
    }

}
