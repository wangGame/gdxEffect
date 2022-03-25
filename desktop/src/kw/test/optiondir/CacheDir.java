package kw.test.optiondir;

import org.junit.Test;

import java.io.File;

public class CacheDir {

    @Test
    public void cacheDirTest(){
        String cacheDir = System.getenv("XDG_CONFIG_CACHE");
        if (cacheDir == null) {
            String homeDir = System.getProperty("user.home");
            if (homeDir == null) {
                System.out.println("null");
            }
            cacheDir = homeDir + File.separator + ".cache";
        }
        System.out.println(cacheDir + File.separator);
    }
}
