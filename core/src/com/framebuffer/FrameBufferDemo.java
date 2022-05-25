package com.framebuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

//framebuffer使用
public class FrameBufferDemo extends Group{
    public FrameBuffer frameBuffer;

    public FrameBufferDemo(Image group){
        frameBuffer = new FrameBuffer(
                Pixmap.Format.RGBA8888,
                720, 1280,
                false);
        addActor(group);
    }

    public Texture getTexture(){
        return frameBuffer.getColorBufferTexture();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        frameBuffer.begin();
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.flush();
        super.draw(batch,parentAlpha);
        batch.flush();
        frameBuffer.end();
    }
}
