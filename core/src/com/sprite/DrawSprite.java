package com.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;

public class DrawSprite extends Group {
    private Sprite sprite;
    private Sprite spritePos;

    public DrawSprite(){
        sprite = new Sprite(new Texture("white_cir.png"));
        spritePos = new Sprite(new Texture("white_cir.png"));
        spritePos.setPosition(100,100);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        sprite.draw(batch);
        spritePos.draw(batch);
    }
}
