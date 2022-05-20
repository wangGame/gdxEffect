package com.tony.rider.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class MaskTest extends Group {
    private Image img;
    private Image mask;
    private float base = 0;
    public MaskTest(){
        mask = new Image(new Texture("demo.png"));
        img = new Image(new Texture(Gdx.files.internal("sprite.png")));
        mask.addAction(Actions.forever(
                Actions.sequence(Actions.moveTo(80,0,19f),Actions.moveTo(0,0,0))));
        addActor(mask);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawAlphaMask(batch);
        //画前景色
        drawForeground(batch, 0, 0,
                (int) mask.getWidth()/2,
                (int)mask.getHeight()/2);
    }

    private void drawForeground(Batch batchPara, int clipX, int clipY ,int clipWidth ,int clipHeight) {
        Gdx.gl.glColorMask(true, true, true, true);
        batchPara.setBlendFunction(GL20.GL_DST_ALPHA, GL20.GL_ONE_MINUS_DST_ALPHA);
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor(clipX, clipY, clipWidth, clipHeight);
        img.draw(batchPara,1);
        batchPara.flush();
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }
    private void drawAlphaMask(Batch batch) {
        Gdx.gl.glColorMask(false, false, false, true);
        mask.draw(batch,1);
        batch.flush();
    }
}
