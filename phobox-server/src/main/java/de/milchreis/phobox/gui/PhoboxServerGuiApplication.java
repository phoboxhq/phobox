package de.milchreis.phobox.gui;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.utils.phobox.BundleHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
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
                e.printStackTrace();
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
    public void start(Stage primaryStage) throws Exception {

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
}
