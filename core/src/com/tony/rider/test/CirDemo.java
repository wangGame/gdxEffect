package com.tony.rider.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CirDemo extends Group {
    private Image image;
    public CirDemo(){
        Pixmap pixmap = new Pixmap(Gdx.files.internal("BG.png"));
//
//        image = new Image(new Texture("BG.png"));
//        addActor(image);
//        TextureRegionDrawable drawable = (TextureRegionDrawable)image.getDrawable();
//
//        TextureData textureData = drawable.getRegion().getTexture().getTextureData();
//        textureData.prepare();
//        Pixmap pixmap = drawable.getRegion().getTexture().getTextureData().consumePixmap();
        int width = pixmap.getWidth();
        int height = pixmap.getHeight();
        int min = Math.min(width, height);
        Pixmap.Blending blending = pixmap.getBlending();
        pixmap.setBlending(Pixmap.Blending.None);
        for (int i = 0; i < width; i++) {
            for (int i1 = 0; i1 < height; i1++) {
                float i2 = (i-width/2.0F) *(i-width/2.0F) + (i1-height/2.0F) *(i1-height/2.0F);
                if (i2>(min / 8.0F )*(min / 8.0F )){
//                    System.out.println("-------------------");
                    pixmap.drawPixel(i,i1, Color.valueOf("#44FFFFFF").toIntBits());
//                    int pixel = pixmap.getPixel(i, i1);
                }
            }
        }
        pixmap.setBlending(blending);

        Image image = new Image(new Texture(pixmap));
        addActor(image);

    }
}
