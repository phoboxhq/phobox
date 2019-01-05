package de.milchreis.phobox.gui;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.core.schedules.CopyScheduler;
import de.milchreis.phobox.core.schedules.ImportScheduler;
import de.milchreis.phobox.core.schedules.StorageScanScheduler;
import de.milchreis.phobox.server.api.StorageController;
import de.milchreis.phobox.utils.BundleHelper;
import de.milchreis.phobox.utils.Restarter;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StorageAskController {

	private @FXML Button storageButton;

	@FXML
	protected void onChangeStoragePath() throws IOException, URISyntaxException {
		DirectoryChooser fileChooser = new DirectoryChooser();
		File file = fileChooser.showDialog(storageButton.getScene().getWindow());
		if (file != null) {
			String path = file.getAbsolutePath();
			storageButton.setText(path);
			PreferencesManager.set(PreferencesManager.STORAGE_PATH, path);

			Restarter.restartApplication();
		}
	}

	@FXML
	protected void onNext() throws IOException, URISyntaxException {
		Restarter.restartApplication();
	}
}

