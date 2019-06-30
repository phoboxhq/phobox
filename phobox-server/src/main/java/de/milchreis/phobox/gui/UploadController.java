package de.milchreis.phobox.gui;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.PhoboxDefinitions;
import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.core.file.FileProcessor;
import de.milchreis.phobox.core.model.PhoboxModel;
import de.milchreis.phobox.utils.storage.FilesystemHelper;
import de.milchreis.phobox.utils.system.Browser;
import de.milchreis.phobox.utils.system.LocalIpAddress;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class UploadController implements Initializable {
	
	private @FXML Label addressLabel;
	private @FXML Button storageButton;

	private ExecutorService executorService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			if(LocalIpAddress.getLocalIP().size() > 0) {
				addressLabel.setText("http://"+LocalIpAddress.getLocalIP().get(0)+":"+Phobox.getModel().getStorageConfiguration().getPhoboxPort());
			}

			executorService = Executors.newSingleThreadExecutor();

		} catch (SocketException e) {
			log.error("Error while loading local address", e);
		}
	}
	
	@FXML
	protected void onChangeStoragePath() throws IOException {
		DirectoryChooser fileChooser = new DirectoryChooser();
		File file = fileChooser.showDialog(addressLabel.getScene().getWindow());
		if (file != null) {
			String path = file.getAbsolutePath();
			storageButton.setText(path);
			PreferencesManager.setStoragePath(new File(path));
			Phobox.getModel().setStoragePath(path);
		}
	}
	
	@FXML
	protected void onDrag(DragEvent event) {
		Dragboard db = event.getDragboard();
		if (db.hasFiles()) {
			event.acceptTransferModes(TransferMode.COPY);
		} else {
			event.consume();
		}
	}
	
	@FXML
	protected void onDrop(final DragEvent event){

		Dragboard db = event.getDragboard();
		File incomingPath = Phobox.getModel().getIncomingPath();
		final List<File> fileList = db.getFiles();

		executorService.execute(() -> {

			for (File fileFromDragAndDrop : fileList) {
				if (fileFromDragAndDrop.isDirectory()) {
					FileProcessor.createNew(fileFromDragAndDrop, PhoboxDefinitions.SUPPORTED_IMPORT_FORMATS, (file, info) -> {
						copyFilesToDirectory(file, incomingPath);
					});
				} else {
					copyFilesToDirectory(fileFromDragAndDrop, incomingPath);
				}
			}
		});

		event.setDropCompleted(true);
		event.consume();
	}

	private void copyFilesToDirectory(File file, File targetPath) {
		try {
			FileUtils.copyFileToDirectory(file, targetPath);
		} catch (IOException e) {
			log.error("Error while coping files", e);
		}
	}
	
	@FXML
	protected void onOpenGUI() {
		PhoboxModel model = Phobox.getModel();
		Browser.open("http://localhost:"+model.getStorageConfiguration().getPhoboxPort());
	}

	@FXML
	protected void onOpenExplorer() {
		FilesystemHelper.openSystemExplorer(new File(Phobox.getModel().getStoragePath()));
	}

	@FXML
	protected void onClose() {
		System.exit(0);
	}
}

