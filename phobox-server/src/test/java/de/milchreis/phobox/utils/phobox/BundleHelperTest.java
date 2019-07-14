package de.milchreis.phobox.utils.phobox;

import org.junit.Test;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BundleHelperTest {

    @Test
    public void test_get_language_by_supported_locale() {

        // Arrange
        Locale.setDefault(new Locale("de", "DE"));

        // Act
        ResourceBundle bundle = BundleHelper.getSuitableBundle();

        // Assert
        assertNotNull(bundle);
        assertEquals("de", bundle.getLocale().getLanguage());
        assertEquals("Hallo bei Phobox", bundle.getString("welcome"));
    }

    @Test
    public void test_get_fallback_language_by_unsupported_locale() {

        // Arrange
        Locale.setDefault(new Locale("nb", "NO"));

        // Act
        ResourceBundle bundle = BundleHelper.getSuitableBundle();

        // Assert
        assertNotNull(bundle);
        assertEquals("en_us", bundle.getLocale().getLanguage());
        assertEquals("Welcome to Phobox", bundle.getString("welcome"));
    }

}