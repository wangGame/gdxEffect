package com.tony.rider.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

public class EarserTest extends Group {
    private Image brush = null;
    private Image cachu = null;
    private FrameBuffer frameBuffer = null;
    private TextureRegion region ;

    public EarserTest(Stage stage) {
        setSize(1080,1920);
        // 笔刷是一张20*20的透明图片
        brush = new Image(new Texture("cirwhite.png"));
        //需要擦除的图片
        cachu = new Image(new Texture(Gdx.files.internal("BG.png")));
        //framebuffer
        frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        region = new TextureRegion(frameBuffer.getColorBufferTexture());
        region.flip(false,true);
        //先把被擦除的图片渲染进frameBuffer
        stage.addListener(new DragListener(){
            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                brush.setPosition(event.getStageX()*2-brush.getHeight()/2,event.getStageY()*2-brush.getHeight()/2);
            }
        });
        addActor(brush);
        addActor(cachu);
    }

    private boolean isDraw;
    public void drawFrame(Batch batch){
        if (isDraw) {
            return;
        }
        isDraw = true;
        frameBuffer.begin();
        batch.flush();
        cachu.draw(batch,1);
        batch.flush();
        frameBuffer.end();
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawFrame(batch);
        drawFrameBrush(batch);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        batch.draw(region, 0f, 0f);
    }

    private void drawFrameBrush(Batch batch) {
        frameBuffer.begin();
        batch.flush();
        batch.setBlendFunction(GL20.GL_ZERO, GL20.GL_ONE_MINUS_SRC_ALPHA);
//        batch.enableBlending();
        brush.draw(batch,1);
        batch.flush();
        frameBuffer.end();
    }
}
