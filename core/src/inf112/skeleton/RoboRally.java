package inf112.skeleton;

import com.badlogic.gdx.Game;
import inf112.skeleton.screens.TitleScreen;

public class RoboRally extends Game {

    @Override
    public void create() {
        this.setScreen(new TitleScreen(this));
    }
}
