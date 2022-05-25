package com.framebuffer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class FbDemo extends Group {
    public FbDemo(){
        Image image1 = new Image(new Texture("12.webp"));
//        group.addActor(image1);
//        group.setSize(720,1280);
        FrameBufferDemo demo = new FrameBufferDemo(image1);
        Texture texture = demo.getTexture();
        addActor(demo);
        Image image = new Image(texture);
        addActor(image);
    }
}
