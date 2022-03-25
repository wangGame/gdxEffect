package kw.test.string;
public class Translator {
    public interface Implementation {
        String trc(String source, String context);

        String trn(String singular, String plural, int n);

        /** Returns a String containing all distinct characters used by this translation */
        String getCharacters();
    }

    private static Implementation sImplementation = DefaultImplementation.instance;

    /**
     * Switch to the specified implementation, or fall back to the default, pass-through,
     * implementation if
     *
     * @p impl is null
     */
    public static void setImplementation(Implementation impl) {
        if (impl == null) {
            sImplementation = DefaultImplementation.instance;
        } else {
            sImplementation = impl;
        }
    }

    public static String tr(String source) {
        return sImplementation.trc(source, null);
    }

    @SuppressWarnings("unused")
    public static String trc(String source, String context) {
        return sImplementation.trc(source, context);
    }

    /**
     * Returns a translated version of a message with a plural form.
     *
     * <p>The count in the messages is represented by the %# placeholder.
     */
    public static String trn(String singular, String plural, int count) {
        return sImplementation.trn(singular, plural, count);
    }
}

