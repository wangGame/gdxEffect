package com.tony.rider.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class PixmapActor  extends Actor {

    private Pixmap pixmap;
    private Texture texture;
    private TextureRegion region;
    public final Color color = new Color();
    private boolean flipX,flipY;

    public PixmapActor(Pixmap pixmap) {
        this.pixmap = pixmap;
        texture = new Texture(pixmap);
        this.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        region = new TextureRegion(texture);
        setSize(region.getRegionWidth(), region.getRegionHeight());
    }

    public int getPixel(Vector2 position) {
        return getPixel(position.x, position.y);
    }

    public int getPixel(float x, float y) {
        return pixmap.getPixel((int) x, (int) y);
    }

    public void setPixel(float x, float y, int value) {
        Color.rgba8888ToColor(color, value);
        pixmap.setColor(color);
    }

    public void eraseCircle(int x, int y, int radius) {
        y = (int) (getHeight()-y);

        if (pixmap != null) {
            Pixmap.Blending blending = pixmap.getBlending();
            pixmap.setColor(0,0,0,0);
            pixmap.setBlending(Pixmap.Blending.None);

            pixmap.fillCircle(x, y, radius);
            pixmap.setBlending(blending);

            //需要给texture重新的设置数据
            Gdx.gl.glBindTexture(GL20.GL_TEXTURE_2D, texture.getTextureObjectHandle());
            Gdx.gl.glPixelStorei(GL20.GL_UNPACK_ALIGNMENT, 1);
            Gdx.gl.glTexImage2D(GL20.GL_TEXTURE_2D, 0, pixmap.getGLInternalFormat(), pixmap.getWidth(), pixmap.getHeight(), 0,
                    pixmap.getGLFormat(), pixmap.getGLType(), pixmap.getPixels());
        }
    }

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }
    public void setFlipY(boolean flipY) {
        this.flipY = flipY;
    }
    public boolean isFlipX() {
        return flipX;
    }
    public boolean isFlipY() {
        return flipY;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = batch.getColor();
        batch.setColor(getColor());
        batch.draw(texture, getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation(),
                0, 0,
                texture.getWidth(),
                texture.getHeight(),
                flipX, flipY);
        batch.setColor(color);
    }

}
