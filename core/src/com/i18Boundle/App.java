package com.i18Boundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Locale;

/**
 * 国际 化
 */
public class App extends Group {
    public App(){
        FileHandle enFileHandle = Gdx.files.internal("en_us/MyBundle");
        I18NBundle bundle = I18NBundle.createBundle(enFileHandle,Locale.US);
//        System.out.println(bundle.get("levelCompleted.currentScore"));
        System.out.println(bundle.format("levelCompleted.currentScore", 1));
    }

    private void initializeText() {
//        I18NBundle b = lm.getCurrentBundle();
//        gameTitle = b.get("gameTitle");   // 根据key来获取对应的values
//        gameBegin = b.format("gameBegin", 81); // 先根据key来获取对应的values值，再格式化语言
//        gameHelp = b.get("gameHelp");

    }

    private void translate() {
//        if(lm.getCurrentLanguage().equals("chinese"))
//            lm.setCurrentLanguage("english");
//        else
//            lm.setCurrentLanguage("chinese");

        initializeText();
    }

    public class LanguageManager {
        private ObjectMap<String, I18NBundle> languages;
        private String currentLanguage;

        public LanguageManager() {
            languages = new ObjectMap<String, I18NBundle>();
            /*此处获取的默认语言格式，和后面保存的语言格式不统一，在正式使用时会做一个转换
             * 或者语言和I18NBundle对象不保存在ObjectMap里面，用别的数据结构，此处不考虑这种情况
             * */
            currentLanguage = Locale.getDefault().toString();
        }

        /**
         * 储存语言及其对应的I18NBundle对象，方便以后获取
         * @param name 语言名称
         * @param fileHandle  properties文件对象
         * @param locale   国家编码
         */
        public void loadLanguage(String name, FileHandle fileHandle, Locale locale) {
            if (name != null && !name.isEmpty() && fileHandle != null && locale != null)
                languages.put(name.toLowerCase(), I18NBundle.createBundle(fileHandle, locale));
        }

        public void loadLanguage(String name, FileHandle fileHandle) {
            if (name != null && !name.isEmpty() && fileHandle != null)
                languages.put(name.toLowerCase(), I18NBundle.createBundle(fileHandle));
        }

        public void removeLanguage(String name, I18NBundle bundle) {
            if (name != null && !name.isEmpty() && bundle != null)
                languages.remove(name.toLowerCase());
        }

        public void setCurrentLanguage(String name) {
            if (languages.containsKey(name.toLowerCase()))
                currentLanguage = name;
        }

        public String getCurrentLanguage() {
            System.out.println("currentLanguage: " + currentLanguage);
            return currentLanguage;
        }

        public I18NBundle getCurrentBundle() {
            return languages.get(currentLanguage);
        }
    }
}