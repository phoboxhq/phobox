package de.milchreis.phobox.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

@Slf4j
public class Restarter {

    public static void restartApplication() throws URISyntaxException, IOException {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";

        String jarFileUri = Restarter.class.getProtectionDomain().getCodeSource().getLocation().toURI().toString();
        String jarFilePath = jarFileUri.substring(jarFileUri.indexOf("file:/") + 6, jarFileUri.indexOf("!"));

        final File currentJar = new File(jarFilePath);

        if (!currentJar.getName().endsWith(".jar"))
            return;

        final ArrayList<String> command = new ArrayList<String>();
        command.add(javaBin);
        command.add("-jar");
        command.add(currentJar.getPath());

        final ProcessBuilder builder = new ProcessBuilder(command);
        builder.start();
        System.exit(0);
    }
}
