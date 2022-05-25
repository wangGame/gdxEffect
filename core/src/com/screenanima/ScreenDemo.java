package com.screenanima;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.tony.rider.screen.base.BaseScreen;

public class ScreenDemo extends BaseScreen {
    public ScreenDemo(){
        stage.addActor(new Image(new Texture("white_squ.png")));
    }
}
