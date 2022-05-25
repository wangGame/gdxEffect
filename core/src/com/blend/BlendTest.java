package com.blend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class BlendTest extends Group {
    private int[] srcBlend = {
            GL20.GL_ZERO,
            GL20.GL_ONE,
            GL20.GL_SRC_COLOR,
            GL20.GL_ONE_MINUS_SRC_COLOR,
            GL20.GL_DST_COLOR,
            GL20.GL_ONE_MINUS_DST_COLOR,
            GL20.GL_SRC_ALPHA,
            GL20.GL_ONE_MINUS_SRC_ALPHA,
            GL20.GL_DST_ALPHA,
            GL20.GL_ONE_MINUS_DST_ALPHA
    };

    private int[] dstBlend = {
            GL20.GL_ZERO,
            GL20.GL_ONE,
            GL20.GL_SRC_COLOR,
            GL20.GL_ONE_MINUS_SRC_COLOR,
            GL20.GL_DST_COLOR,
            GL20.GL_ONE_MINUS_DST_COLOR,
            GL20.GL_SRC_ALPHA,
            GL20.GL_ONE_MINUS_SRC_ALPHA,
            GL20.GL_DST_ALPHA,
            GL20.GL_ONE_MINUS_DST_ALPHA
    };

    public BlendTest(){
        Image image = new Image(new Texture("clean_brush.png"));
        addActor(image);
        image.setSize(100,100);
        Image image1 = new Image(new Texture("demo.png"));
        image1.setPosition(30,30);
        addActor(image1);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int src = batch.getBlendSrcFunc();
        int dst = batch.getBlendDstFunc();
        batch.setBlendFunction(srcBlend[0], dstBlend[2]);
        super.draw(batch, parentAlpha);
        batch.setBlendFunction(src,dst);
    }
}
