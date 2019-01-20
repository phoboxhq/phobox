package de.milchreis.phobox.utils;

import java.awt.Desktop;
import java.net.URI;

public class Browser {

    public static void open(String url) {

        String os = System.getProperty("os.name").toLowerCase();
        Runtime runtime = Runtime.getRuntime();

        try {
            if (os.indexOf("win") >= 0) {
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);

            } else if (os.indexOf("mac") >= 0) {
                runtime.exec("open " + url);

            } else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
                runtime.exec("xdg-open " + url);

            } else {
                return;
            }
        } catch (Exception e) {
            return;
        }
        return;

    }
}
