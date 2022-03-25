package kw.test.string;

class DefaultImplementation implements Translator.Implementation {
    @Override
    public String trc(String source, String comment) {
        return source;
    }

    @Override
    public String trn(String singular, String plural, int n) {
        String txt = n == 1 ? singular : plural;
        return txt.replace("%#", String.valueOf(n));
    }

    @Override
    public String getCharacters() {
        return "";
    }

    static DefaultImplementation instance = new DefaultImplementation();

    DefaultImplementation() {}
}
