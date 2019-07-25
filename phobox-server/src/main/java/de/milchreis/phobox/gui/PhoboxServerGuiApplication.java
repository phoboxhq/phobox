package de.milchreis.phobox.gui;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.utils.phobox.BundleHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

@Slf4j
public class PhoboxServerGuiApplication extends Application {

    private static PhoboxServerGuiApplication instance;

    private Scene storageAskScene;
    private Scene uploadScene;

    private Stage mainStage;
    private Stage splash;


    public PhoboxServerGuiApplication() {
        instance = this;
    }

    public void showSplash() {
        Platform.runLater(() -> {
            mainStage.hide();
            splash.show();
        });
    }

    public void showStorageInitalization() {
        Platform.runLater(() -> {
            splash.hide();
            mainStage.setScene(storageAskScene);
            mainStage.show();
        });

        while(Phobox.getModel().getStoragePath() == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.warn("Error while sleep on initial storage definition", e);
            }
        }
    }

    public void showUpload() {
        Platform.runLater(() -> {
            splash.hide();
            mainStage.setScene(uploadScene);
            mainStage.show();
        });
    }

    public synchronized static PhoboxServerGuiApplication launchAndGet() {
        if (instance == null) {
            Executors.newFixedThreadPool(1).execute(() -> {
                Application.launch(PhoboxServerGuiApplication.class);
            });

            while (instance == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
        return instance;
    }

    @Override
    public void stop() {
        Platform.runLater(() -> {
            splash.close();
            mainStage.close();

            try {
                super.stop();
            } catch (Exception e) {
                log.error("Error while stopping JavaFX application", e);
            }
        });
    }

    @Override
    public void start(Stage primaryStage) {

        mainStage = primaryStage;
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/favicon_64.png")));
        primaryStage.setOnCloseRequest(e -> onClose());
        primaryStage.setTitle("Phobox");

        storageAskScene = createScene("PhoboxServerGuiStorageAsk.fxml");
        uploadScene = createScene("PhoboxServerGui.fxml");

        splash = new Stage();
        splash.initStyle(StageStyle.UNDECORATED);
        splash.getIcons().add(new Image(getClass().getResourceAsStream("/img/favicon_64.png")));
        splash.setScene(createScene("PhoboxSplash.fxml"));
        splash.setTitle("Phobox");
    }

    public Scene createScene(String fxmlPath) {
        try {
            URL url = PhoboxServerGuiApplication.class.getClassLoader().getResource(fxmlPath);
            return new Scene(FXMLLoader.load(url, BundleHelper.getSuitableBundle()));
        } catch (Exception e) {
            log.error("Could not load GUI definition for " + fxmlPath, e);
        }
        return null;
    }

    protected void onClose() {
        System.exit(0);
    }

    public void closeSplash() {
        Platform.runLater(() -> {
            splash.hide();
        });
    }

    public void showAlert(Exception exception) {
        Platform.runLater(() -> {

            ResourceBundle bundle = BundleHelper.getSuitableBundle();
            String title = bundle.getString("error.title");
            String headline = bundle.getString("error.intro");
            String message = bundle.getString("error.help")
                    + "\n\n" +
                    exception != null ? exception.getMessage() : "ERROR";

            ErrorDialog dialog = new ErrorDialog(title, headline, message, exception);

            // Set icon
            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/favicon_64.png")));

            // Show
            dialog.showDialog();

            onClose();
        });
    }
}
