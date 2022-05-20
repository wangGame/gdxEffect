package com.tony.rider.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Disposable;

public class MaskHairTest extends Group {

    private Image img;
    private Texture mask;
    private Pixmap maskPixmap;
    private Texture texture;
    private DrawablePixmap drawablePixmap;

    public MaskHairTest(){
        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GRAY);
        pixmap.fill();
        addActor(new Image(new Texture(pixmap)));

        maskPixmap = new Pixmap(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),Pixmap.Format.Alpha);
        texture = new Texture(maskPixmap);
        texture.setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);

        img = new Image(new Texture(Gdx.files.internal("demo.png")));
        addActor(img);

        mask = new Texture("sprite.png");

        drawablePixmap = new DrawablePixmap(mask);


    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

    }

    class DrawablePixmap implements Disposable {

        private float brushSize = 20;
        private Color clearColor = Color.CLEAR;
        private Color drawColor = Color.BLUE;
        private Pixmap pixmap;
        private boolean dirty;

        public DrawablePixmap(Texture texture){
            this.pixmap = new Pixmap(texture.getWidth(),texture.getHeight(),Pixmap.Format.Alpha);
            pixmap.setColor(drawColor);
            this.dirty = false;
        }

        public void update() {
            if (dirty) {
                texture.draw(pixmap, 0, 0);
                dirty = false;
            }
        }

        public void clear(){
            pixmap.setColor(clearColor);
            pixmap.fill();
            pixmap.setColor(drawColor);
            dirty = true;
        }

        public void drawLerped(Vector2 from, Vector2 to) {
            float dist = to.dst(from);
            /* Calc an alpha step to put one dot roughly every 1/8 of the brush
             * radius. 1/8 is arbitrary, but the results are fairly nice. */
            float alphaStep = brushSize / (8f * dist);
            float a = 0f;
            while (a < 1f) {
                Vector2 lerped = from.lerp(to, a);
                drawDot(lerped);
                a += alphaStep;
            }

            drawDot(to);
            dirty = true;
        }

        public void drawDot(Vector2 spot){
            pixmap.fillCircle((int)spot.x,(int)spot.y,(int)brushSize);
        }

        @Override
        public void dispose() {

        }
    }

}
