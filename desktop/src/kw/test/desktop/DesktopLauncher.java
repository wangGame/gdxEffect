package kw.test.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.swipe.SwiperImproved;
import com.tony.rider.RiderGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "tony Rider Game";
        config.width = 360;
        config.height = 640;
        config.x = 600;
        config.y = 100;

        new LwjglApplication(new RiderGame(), config);
//        new LwjglApplication(new SwiperImproved(), config);

    }
}