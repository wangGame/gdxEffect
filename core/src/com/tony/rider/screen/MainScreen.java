package com.tony.rider.screen;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.music.MusicGroup;
import com.tony.rider.RiderGame;
import com.tony.rider.screen.base.BaseScreen;
import com.tony.rider.statustion.Statustion;
import com.tony.rider.test.EarserTest;
import com.tony.rider.test.MashLine;
import com.tony.rider.test.MaskHairTest;
import com.tony.rider.test.MaskTest;
import com.tony.rider.test.PixmapTest;
import com.zhuanpan.ImageZhuan;

public class MainScreen extends BaseScreen {
    private int status=-1;
    public MainScreen(){

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        back();
        if (status!= Statustion.currentstatus) {
            status = Statustion.currentstatus;
            if (status == Statustion.one){
                EarserTest test = new EarserTest(stage);
                stage.addActor(test);
            }else if (status == Statustion.two){
                PixmapTest test = new PixmapTest();
                stage.addActor(test);
            }else if (status == Statustion.three){
                MaskTest maskTest = new MaskTest();
                stage.addActor(maskTest);
            }else if (status == Statustion.four){
                MaskHairTest mashLine = new MaskHairTest();
                stage.addActor(mashLine);
            }else if (status == Statustion.five){
                ImageZhuan zhuan = new ImageZhuan();
                stage.addActor(zhuan);
            }else if (status == Statustion.six){
                MusicGroup group = new MusicGroup();
                stage.addActor(group);
            }
        }
    }

    public void back(){
        if (back) {
            back = false;
            RiderGame.instence().setScreen(new LoadingScreen());
        }
    }
}
