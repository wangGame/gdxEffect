package com.tony.rider.screen;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.blend.BlendTest;
import com.colorblack.Demo;
import com.framebuffer.FbDemo;
import com.music.MusicGroup;
import com.screenanima.App;
import com.sprite.DrawSprite;
import com.test.shaderstest.ShadersTestMain;
import com.tony.rider.RiderGame;
import com.tony.rider.screen.base.BaseScreen;
import com.tony.rider.statustion.Statustion;
import com.tony.rider.test.CirDemo;
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
            }else if (status == Statustion.seven){
                BlendTest test = new BlendTest();
                stage.addActor(test);
            }else if (status == Statustion.eight){
                App app = new App();
                stage.addActor(app);
            }else if (status == Statustion.nine){
                FbDemo fbDemo = new FbDemo();
                stage.addActor(fbDemo);
            }else if (status == Statustion.ten){
                com.i18Boundle.App app = new com.i18Boundle.App();
                stage.addActor(app);
            }else if (status == Statustion.eleven){
                Demo demo = new Demo();
                stage.addActor(demo);
            }else if (status == Statustion.twen){
                ShadersTestMain main = new ShadersTestMain();
                stage.addActor(main);
            }else if (status == Statustion.thired){
                DrawSprite sprite = new DrawSprite();
                stage.addActor(sprite);
            }else if (status == Statustion.fourteen){
                CirDemo cirDemo = new CirDemo();
                stage.addActor(cirDemo);
            }
        }
    }

    public void back(){
        if (back) {
            back = false;
            stage.clear();
            RiderGame.instence().setScreen(new LoadingScreen());
        }
    }
}
