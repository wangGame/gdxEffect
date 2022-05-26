package com.box2d;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Action;

/**
 * 重量随机
 */
public class BodyGravityAction extends Action {
    private World world;
    private Body body;
    private float gravityOffset;
    private boolean randomise;

    @Override
    public boolean act(float delta) {
        float gravityToApply = world.getGravity().y;
        if (randomise) {
            gravityToApply = gravityToApply + ((float) Math.random() * gravityOffset);
        }else{
            gravityToApply = gravityToApply + gravityOffset;
        }
        body.applyForceToCenter(0, gravityToApply, true);
        return false;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public float getGravityOffset() {
        return gravityOffset;
    }

    public void setGravityOffset(float gravityOffset) {
        this.gravityOffset = gravityOffset;
    }

    public boolean isRandomise() {
        return randomise;
    }

    public void setRandomise(boolean randomise) {
        this.randomise = randomise;
    }
}
