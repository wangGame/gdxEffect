package com.tony.rider.blend;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.crashinvaders.vfx.effects.GaussianBlurEffect;
import com.crashinvaders.vfx.scene2d.VfxWidgetGroup;

public class GaussianGroup extends Group {
    public GaussianGroup() {
        setSize(720,1280);
        VfxWidgetGroup vfxGroup = new VfxWidgetGroup(Pixmap.Format.RGB888);
        vfxGroup.setFillParent(true);
        addActor(vfxGroup);
        vfxGroup.addActor(new Image(new Texture("sprite.png")));
        GaussianBlurEffect effect = new GaussianBlurEffect();
        effect.setPasses(19);
        effect.setAmount(1909);
        vfxGroup.getVfxManager().addEffect(effect);
    }
}
