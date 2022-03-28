package com.tony.rider.screen;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.tony.rider.screen.base.BaseScreen;
import com.tony.rider.test.EarserTest;
import com.tony.rider.test.MashLine;
import com.tony.rider.test.MaskTest;
import com.tony.rider.test.MeshTest;
import com.tony.rider.test.PixMapDemo;
import com.tony.rider.test.PixmapTest;

public class LoadingScreen extends BaseScreen {

    public LoadingScreen() {
        super();
    }

    @Override
    public void show() {
        super.show();
//        EarserTest test = new EarserTest(stage);
//        stage.addActor(test);
//        MaskTest maskTest = new MaskTest();
//        PixmapTest test = new PixmapTest();
//        stage.addActor(test);

//        PixMapDemo pixMapDemo = new PixMapDemo();
//        addActor(pixMapDemo);


        MashLine line = new MashLine();
        addActor(line);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
