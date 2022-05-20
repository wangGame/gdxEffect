package com.zhuanpan;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

public class ImageZhuan extends Group {

    public ImageZhuan(){
        Image image = new Image(new Texture("12.webp")){
            private int time = 10;
            private float xxx = 0;
            private int xx = 0;
            boolean f= false;

            @Override
            public void act(float delta) {
                super.act(delta);
                xxx+=delta;
                if (xx>-1) {
                    if (xx < 400&&!f) {
                        xx++;
                        rotateBy(xx*0.1F);
                    } else {
                        f = true;
                        xx--;
                        rotateBy(xx*0.1F);
                    }
                }else{
                    setRotation(getRotation());

                }
                System.out.println(getRotation());
            }
        };
        addActor(image);
        image.setOrigin(Align.center);
    }
}
