package com.colorblack;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.tony.rider.test.PixmapActor;

public class Demo extends Group {
    public Demo(){
        //10 convert 16
        Table table = new Table() {{
            for (int i = 1; i <= 256; i++) {
                ImageColor imageColor = new ImageColor(i);
                System.out.println(color.toFloatBits());
                color.a = 1;
                add(imageColor).pad(2);
                if (i%12==0){
                    row();
                }
            }
            pack();
        }};
        addActor(table);
    }

}
