package com.tony.rider.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;

public class PixmapTest extends Group {
    private PixmapActor pixmapActor;
    private int count;

    public PixmapTest(){
        pixmapActor = new PixmapActor(new Pixmap(Gdx.files.internal("sprite.png")));
        addActor(pixmapActor);
        pixmapActor.eraseCircle(200, 200, 100);
    }

    @Override
    public void act(float delta) {
        count ++;
        if (count == 30) {
            count = 0;
            pixmapActor.eraseCircle(MathUtils.random(10, 500), MathUtils.random(10, 500), MathUtils.random(10, 50));
        }
    }
}
