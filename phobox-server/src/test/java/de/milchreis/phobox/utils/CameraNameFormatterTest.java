package de.milchreis.phobox.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class CameraNameFormatterTest {

    @Test
    public void test_camera_names() {

        String[][] missingInfos = new String[][]{
                {null, "Canon EOS 6D"},
                {"", "Canon EOS 6D"},
                {"Canon", ""},
                {"Canon", null},
                {null, null},
                {"Canon", "Canon EOS 6D"},
                {"Canon", "EOS 6D"},
                {"Canon", "EOS 6D Canon"},
        };

        assertEquals("Canon EOS 6D", CameraNameFormatter.getFormattedCameraName(missingInfos[0]));
        assertEquals("Canon EOS 6D", CameraNameFormatter.getFormattedCameraName(missingInfos[1]));
        assertEquals("Canon", CameraNameFormatter.getFormattedCameraName(missingInfos[2]));
        assertEquals("Canon", CameraNameFormatter.getFormattedCameraName(missingInfos[3]));
        assertEquals("Unknown", CameraNameFormatter.getFormattedCameraName(missingInfos[4]));
        assertEquals("Canon EOS 6D", CameraNameFormatter.getFormattedCameraName(missingInfos[5]));
        assertEquals("Canon EOS 6D", CameraNameFormatter.getFormattedCameraName(missingInfos[6]));
        assertEquals("EOS 6D Canon", CameraNameFormatter.getFormattedCameraName(missingInfos[7]));
    }

    @Test
    public void test_camera_names_with_constructor() {

        String[][] missingInfos = new String[][]{
                {null, "Canon EOS 6D"},
                {"", "Canon EOS 6D"},
                {"Canon", ""},
                {"Canon", null},
                {null, null},
                {"Canon", "Canon EOS 6D"},
                {"Canon", "EOS 6D"},
                {"Canon", "EOS 6D Canon"},
        };

        assertEquals("Canon EOS 6D", new CameraNameFormatter(missingInfos[0][0], missingInfos[0][1]).getFormattedCameraName());
        assertEquals("Canon EOS 6D", new CameraNameFormatter(missingInfos[1][0], missingInfos[1][1]).getFormattedCameraName());
        assertEquals("Canon", new CameraNameFormatter(missingInfos[2][0], missingInfos[2][1]).getFormattedCameraName());
        assertEquals("Canon", new CameraNameFormatter(missingInfos[3][0], missingInfos[3][1]).getFormattedCameraName());
        assertEquals("Unknown", new CameraNameFormatter(missingInfos[4][0], missingInfos[4][1]).getFormattedCameraName());
        assertEquals("Canon EOS 6D", new CameraNameFormatter(missingInfos[5][0], missingInfos[5][1]).getFormattedCameraName());
        assertEquals("Canon EOS 6D", new CameraNameFormatter(missingInfos[6][0], missingInfos[6][1]).getFormattedCameraName());
        assertEquals("EOS 6D Canon", new CameraNameFormatter(missingInfos[7][0], missingInfos[7][1]).getFormattedCameraName());
    }
}