package com.tony.rider.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.tony.rider.RiderGame;
import com.tony.rider.screen.MainScreen;
import com.tony.rider.statustion.Statustion;

public class CommonButton extends Group {
    private Label label;
    private Image buttonBg;
    private int status;

    public CommonButton(String v, int i){
        this.status = i;
        buttonBg = new Image(new NinePatch(new Texture("white_squ.png"),20,20,20,20));
        addActor(buttonBg);
        label = new Label("",new Label.LabelStyle(){{
            font = RiderGame.instence().getFont();
        }});
        label.setAlignment(Align.center);
        label.setText(v+i);
        label.pack();
        label.setColor(Color.BROWN);
        buttonBg.setWidth(40+label.getPrefWidth());
        setSize(buttonBg.getWidth(),buttonBg.getHeight());
        label.setPosition(getWidth()/2,getHeight()/2,Align.center);
        addActor(label);
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Statustion.currentstatus = status;
                RiderGame.instence().setScreen(new MainScreen());
            }
        });
    }

}
