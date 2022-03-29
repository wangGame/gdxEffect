package com.tony.rider.blend;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.crashinvaders.vfx.effects.BloomEffect;
import com.crashinvaders.vfx.effects.GaussianBlurEffect;
import com.crashinvaders.vfx.scene2d.VfxWidgetGroup;

public class BloomGroup extends Group {
    public BloomGroup() {
        setSize(720,1280);
        VfxWidgetGroup vfxGroup = new VfxWidgetGroup(Pixmap.Format.RGB888);
        vfxGroup.setFillParent(true);
        addActor(vfxGroup);
        vfxGroup.addActor(new Image(new Texture("sprite.png")));
        BloomEffect effect = new BloomEffect();
        effect.setBaseIntensity(3);
        effect.setBloomSaturation(4);
        vfxGroup.getVfxManager().addEffect(effect);
    }
}
