package de.milchreis.phobox.utils.system;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(DataProviderRunner.class)
public class OSDetectorTest {

    @DataProvider
    public static Object[][] getOSDetections() {
        return new Object[][]{
                {"windows", OSDetector.OS.WINDOWS},
                {"WiNdOwS", OSDetector.OS.WINDOWS},
                {"mac", OSDetector.OS.MAC},
                {"MAC", OSDetector.OS.MAC},
                {"linux", OSDetector.OS.LINUX},
                {"LinuX", OSDetector.OS.LINUX},
                {"Unix", OSDetector.OS.LINUX},
                {"xyz", OSDetector.OS.UNKNOWN},
        };
    }

    @Test
    @UseDataProvider("getOSDetections")
    public void test_getLocalOS(String javaOsDefinition, OSDetector.OS expectedOS) {

        // Arrange
        System.setProperty("os.name", javaOsDefinition);

        // Act/Assert
        assertEquals(expectedOS, OSDetector.getLocalOS());

    }

}