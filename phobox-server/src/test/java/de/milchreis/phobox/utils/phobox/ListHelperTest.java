package de.milchreis.phobox.utils.phobox;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ListHelperTest {

    @Test
    public void test_endsWith() {

        assertFalse(ListHelper.endsWith("breakfast", new String[] {"slow", "medium", "quick"}));
        assertTrue(ListHelper.endsWith("breakfast", new String[] {"slow", "medium", "fast"}));

        assertTrue(ListHelper.endsWith("breakfast", null));
        assertTrue(ListHelper.endsWith("breakfast", new String[] {""}));
        assertFalse(ListHelper.endsWith("breakfast", new String[] {}));

        assertFalse(ListHelper.endsWith("", new String[] {"abc"}));
        assertTrue(ListHelper.endsWith("", null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_endsWith_with_null_argument() {
        ListHelper.endsWith(null, new String[] {"slow", "medium", "quick"});
    }

    @Test
    public void test_contains() {

        assertFalse(ListHelper.contains("breakfast", Arrays.asList("slow", "medium", "quick")));
        assertTrue(ListHelper.contains("breakfast", Arrays.asList("slow", "medium", "fast")));

        assertFalse(ListHelper.contains("breakfast", null));
        assertTrue(ListHelper.contains("breakfast", Arrays.asList("")));
        assertFalse(ListHelper.contains("breakfast", Arrays.asList()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_contains_with_null_argument() {
        ListHelper.contains(null, Arrays.asList("slow", "medium", "fast"));
    }
}