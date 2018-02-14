package de.milchreis.phobox.utils;

public class JavaFXDetector {

	public static boolean isAvailable() {

		try {
			JavaFXDetector.class.getClassLoader().loadClass("javafx.embed.swing.JFXPanel");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
