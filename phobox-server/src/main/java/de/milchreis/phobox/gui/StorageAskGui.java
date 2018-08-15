package de.milchreis.phobox.gui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.core.schedules.CopyScheduler;
import de.milchreis.phobox.core.schedules.ImportScheduler;
import de.milchreis.phobox.core.schedules.StorageScanScheduler;
import de.milchreis.phobox.server.api.StorageService;
import javafx.application.Application;
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
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StorageAskGui extends Application implements Initializable {
	
	private Stage stage;
	private @FXML Button storageButton;
	private @FXML CheckBox createThumbs;
	
    @Override
    public void start(Stage primaryStage) {
    	this.stage = primaryStage;
    	if(Phobox.getModel().isActiveGui()) {
    		try {
    			initGui();
			} catch (Exception e) {
				log.error("Error while loading GUI", e);
			}
    	}
    }
    
    public void initGui() throws Exception {
    	URL url = StorageAskGui.class.getClassLoader().getResource("PhoboxServerGuiStorageAsk.fxml");
    	
    	Parent root = FXMLLoader.<Parent>load(url);
    	Scene scene = new Scene(root, 500, 370);
    	
    	stage.getIcons().add(new Image(getClass().getResourceAsStream("/WebContent/img/favicon_64.png")));
    	stage.setOnCloseRequest(e -> onClose());
    	stage.setTitle("Phobox");
    	stage.setScene(scene);
    	stage.show();
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	@FXML
	protected void onChangeStoragePath() {
		DirectoryChooser fileChooser = new DirectoryChooser();
		File file = fileChooser.showDialog(stage);
		if (file != null) {
			String path = file.getAbsolutePath();
			storageButton.setText(path);
			PreferencesManager.set(PreferencesManager.STORAGE_PATH, path);
			Phobox.getModel().setStoragePath(path);
		}
	}
	
	@FXML
	protected void onClose() {
		System.exit(0);
	}
	
	@FXML
	protected void onNext() throws Exception {
		stage = (Stage) storageButton.getScene().getWindow();
		stage.close();

		Platform.runLater(() -> new ServerGui().start(new Stage()));
		Phobox.startServer();
		
		if(createThumbs.isSelected()) {
			new StorageService().rethumb();
		}
		
		// Initialize the scheduler for importing and scanning new files
		new ImportScheduler(3000);
		new CopyScheduler(3000);
		new StorageScanScheduler(24);
	}
}

