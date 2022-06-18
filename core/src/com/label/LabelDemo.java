package com.label;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;


public class LabelDemo extends Group {
    public LabelDemo(){
        Label label = new Label("[#000000FF]123[]4567789",new Label.LabelStyle(){
            {
                font = new BitmapFont(Gdx.files.internal("6px2bus_48_1.fnt"));
//                font = Asset.getAsset().getN_R_90_1();
                font.getData().markupEnabled=true;//开启变色

            }
        });
        addActor(label);
    }
}
