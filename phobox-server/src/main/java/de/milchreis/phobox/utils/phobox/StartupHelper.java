package de.milchreis.phobox.utils.phobox;

import de.milchreis.phobox.core.Phobox;
import de.milchreis.phobox.core.config.PreferencesManager;
import de.milchreis.phobox.gui.PhoboxServerGuiApplication;

public class StartupHelper {

    public static PhoboxServerGuiApplication createGui() {
        if(Phobox.getModel().isActiveGui()) {
            return PhoboxServerGuiApplication.launchAndGet();
        }
        return null;
    }

    public static boolean isFirstRun() {
        return Phobox.getModel().getStoragePath() == null
                && PreferencesManager.get(PreferencesManager.STORAGE_PATH) == null;
    }

    public static void initShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Phobox.getEventRegistry().onStop()));
    }
}
