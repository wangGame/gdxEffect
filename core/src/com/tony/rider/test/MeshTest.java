package com.tony.rider.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import net.mwplay.cocostudio.ui.widget.TImage;

import java.time.Year;

public class MeshTest extends Group {

    private Image mMask;
    private Image mImg;
    private FrameBuffer frameBuffer;
    private Image target;

    public MeshTest(){

        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
        mMask = new Image(new Texture("mask.png"));
        mImg = new Image(new Texture("sprite.png"));
        target = new Image(frameBuffer.getColorBufferTexture());

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        super.draw(batch, parentAlpha);
        frameBuffer.begin();

        batch.flush();
//        batch.setBlendFunction(GL20.GL_ONE,GL20.GL_ZERO);
        mMask.draw(batch,1);

        batch.flush();

        batch.setBlendFunction(GL20.GL_DST_ALPHA,GL20.GL_ONE_MINUS_DST_ALPHA);
        mImg.draw(batch,1);

        batch.flush();
        frameBuffer.end();

//        batch.setBlendFunction(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_DST_ALPHA);
        target.draw(batch,1);
        batch.flush();
    }
}
