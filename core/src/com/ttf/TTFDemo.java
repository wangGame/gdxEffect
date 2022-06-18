package com.ttf;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class TTFDemo extends Group {
    public TTFDemo(){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("test.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 72;
        parameter.characters = "我们最大的敌人";
        BitmapFont font12 = generator.generateFont(parameter); // font size 12 pixels

        Label label = new Label("我们最大的敌人!",new Label.LabelStyle(){{
            font = font12;
        }});
        addActor(label);
    }
}
