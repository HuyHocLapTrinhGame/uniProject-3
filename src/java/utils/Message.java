package utils;

import jakarta.servlet.http.HttpSession;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Message {
    private static final String BUNDLE_PATH = "i18n.message";

    public static String get(String key, Locale locale) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PATH, locale);
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "???" + key + "???";
        }
    }

    public static String get(HttpSession session, String key) {
        Locale locale = new Locale("en");
        if (session != null) {
            Object attr = session.getAttribute("locale");
            if (attr instanceof Locale) {
                locale = (Locale) attr;
            }
        }
        return get(key, locale);
    }
    
}

