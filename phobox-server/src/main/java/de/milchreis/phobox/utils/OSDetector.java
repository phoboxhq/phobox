package de.milchreis.phobox.utils;

public class OSDetector {

    public enum OS {
        WINDOWS, MAC, LINUX, UNKNOWN
    }

    public static OS getLocalOS() {

        String os = System.getProperty("os.name").toLowerCase();

        if(os.indexOf("win") >= 0)
            return OS.WINDOWS;
        else if(os.indexOf("mac") >= 0)
            return OS.MAC;
        else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0)
            return OS.LINUX;
        else
            return OS.UNKNOWN;
    }
}
