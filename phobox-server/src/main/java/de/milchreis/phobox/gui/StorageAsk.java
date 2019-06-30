package de.milchreis.phobox.gui;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.utils.phobox.BundleHelper;

public class StorageAsk {
	
	public static void askWithCLI() throws IOException {

		ResourceBundle bundle = BundleHelper.getSuitableBundle();
		String message = bundle.getString("start.firstrun.info");

		System.out.println(message);
		Scanner scanner  = new Scanner(System.in);
		String pathInput = scanner.nextLine();
		scanner.close();

		setPath(pathInput);
	}
	
	private static void setPath(String path) throws IOException {
		PreferencesManager.setStoragePath(new File(path));
		Phobox.getModel().setStoragePath(path);
	}
}
