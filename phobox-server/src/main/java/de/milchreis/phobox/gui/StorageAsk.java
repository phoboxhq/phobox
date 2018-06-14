package de.milchreis.phobox.gui;

import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.PreferencesManager;

public class StorageAsk {

	public static void askWithGUI() {
		JOptionPane.showMessageDialog(null, "First run: Please choose your image directory");
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fileChooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	setPath(fileChooser.getSelectedFile().getAbsolutePath());
	    }
	}
	
	public static void askWithCLI() {
		System.out.println("Type path to pictures:");
		Scanner s  = new Scanner(System.in);
		String pathInput = s.nextLine();
		s.close();
		setPath(pathInput);
	}
	
	private static void setPath(String path) {
		PreferencesManager.set("storage.path", path);
		Phobox.getModel().setStoragePath(path);
	}
}
