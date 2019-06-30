package de.milchreis.phobox.gui;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.utils.system.Restarter;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StorageAskController {

	private @FXML Button storageButton;
	private File path;

	@FXML
	protected void onChangeStoragePath() {
		DirectoryChooser fileChooser = new DirectoryChooser();
		File file = fileChooser.showDialog(storageButton.getScene().getWindow());
		if (file != null) {
			path = file;
			storageButton.setText(path.getAbsolutePath());
		}
	}

	@FXML
	protected void onNext() throws IOException {
		PreferencesManager.setStoragePath(path);
		Phobox.getModel().setStoragePath(path.getAbsolutePath());
	}
}

