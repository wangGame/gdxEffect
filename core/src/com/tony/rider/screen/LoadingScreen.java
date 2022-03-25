package com.tony.rider.screen;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tony.rider.screen.base.BaseScreen;
import com.tony.rider.test.EarserTest;

public class LoadingScreen extends BaseScreen {

    public LoadingScreen() {
        super();
    }

    @Override
    public void show() {
        super.show();
        EarserTest test = new EarserTest(stage);
        stage.addActor(test);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
