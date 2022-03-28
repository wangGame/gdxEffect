package com.tony.rider.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class PixMapDemo extends Group {
    private Pixmap pixmap;
    private Image image;
    private Texture texture;
    public PixMapDemo(){
        pixmap = new Pixmap(Gdx.files.internal("sprite.png"));
        texture = new Texture(pixmap);
        image = new Image(texture);
        addActor(image);


        Pixmap.Blending blending = pixmap.getBlending();
        pixmap.setColor(0,0,0,0);
        pixmap.setBlending(Pixmap.Blending.None);
        pixmap.fillCircle(90,90,80);
        pixmap.setBlending(blending);


        Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D,texture.getTextureObjectHandle());
        Gdx.gl.glPixelStorei(GL20.GL_UNPACK_ALIGNMENT,1);
        Gdx.gl.glTexImage2D(GL20.GL_TEXTURE_2D,
                0,
                pixmap.getGLInternalFormat(),
                pixmap.getWidth(),
                pixmap.getHeight(),
                0,
                pixmap.getGLFormat(),
                pixmap.getGLType(),
                pixmap.getPixels());
    }




    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
