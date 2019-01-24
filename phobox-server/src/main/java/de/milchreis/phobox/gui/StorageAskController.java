package de.milchreis.phobox.gui;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.utils.system.Restarter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

