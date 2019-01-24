package de.milchreis.phobox.utils.phobox;

import java.util.Locale;
import java.util.ResourceBundle;

public class BundleHelper {

    public static ResourceBundle getSuitableBundle() {
        try {
            return ResourceBundle.getBundle("MessagesBundle", Locale.getDefault());
        } catch(Exception e) {
            return ResourceBundle.getBundle("MessagesBundle", new Locale("en_US"));
        }
    }
}
