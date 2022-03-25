package kw.test.pack;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class Ht {
    public static void main(String[] args) {
        texturePack();
    }
    static String[] atlasFileName = {"all"};
    private static void texturePack() {
        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.pot = false;
        settings.maxHeight = 3348;
        settings.maxWidth = 3348;
        settings.duplicatePadding = true;
        settings.paddingX = 8;
        settings.paddingY = 8;
        settings.edgePadding = true;
        settings.bleed = true;
        settings.combineSubdirectories = true;
        settings.format = Pixmap.Format.RGBA8888;
        settings.filterMag = Texture.TextureFilter.Linear;
        settings.filterMin = Texture.TextureFilter.Linear;
        settings.useIndexes = false;
        settings.stripWhitespaceX = true;
        settings.stripWhitespaceY = true;
        processAndroid(settings);
    }
    private static void processAndroid(TexturePacker.Settings setting) {
        for (int i = 0; i < atlasFileName.length; i++) {
            String input = atlasFileName[i];
            if (input == null) return;
            try {
                TexturePacker.process(setting, input + "/", "image1/" , input);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
