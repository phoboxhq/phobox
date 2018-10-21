package de.milchreis.phobox.utils;

import java.awt.Desktop;
import java.net.URI;

public class Browser {

	public static void open(String url) throws Exception {

		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			desktop.browse(new URI(url));
			
		} else {
			Runtime runtime = Runtime.getRuntime();
			runtime.exec("xdg-open " + url);
		}
	}
}
