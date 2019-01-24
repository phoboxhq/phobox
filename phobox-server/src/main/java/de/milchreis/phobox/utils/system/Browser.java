package de.milchreis.phobox.utils.system;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Browser {

    public static void open(String url) {

        OSDetector.OS localOS = OSDetector.getLocalOS();
        Runtime runtime = Runtime.getRuntime();

        try {
            if (localOS == OSDetector.OS.WINDOWS) {
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);

            } else if (localOS == OSDetector.OS.MAC) {
                runtime.exec("open " + url);

            } else if (localOS == OSDetector.OS.LINUX) {
                runtime.exec("xdg-open " + url);
            }

        } catch (Exception e) {
            log.warn("Could not find a browser on this system", e);
        }
    }
}
