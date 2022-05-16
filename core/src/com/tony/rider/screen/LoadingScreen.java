package com.tony.rider.screen;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import com.tony.rider.Constant;
import com.tony.rider.actor.CommonButton;

import com.tony.rider.screen.base.BaseScreen;



public class LoadingScreen extends BaseScreen {

    public LoadingScreen() {
        super();
    }

    @Override
    public void show() {
        super.show();
        addActor(new Table(){{
            for (int i = 1; i <= 23; i++) {
                CommonButton commonButton = new CommonButton("example ",i);
                add(commonButton);
                if (i%3==0)row();
            }
            pack();
            setPosition( Constant.GAMEWIDTH/2,Constant.GAMEHIGHT/2, Align.center);
        }});





//        GameView gameView = new GameView();
//        stage.addActor(gameView);

//        EarserTest test = new EarserTest(stage);
//        stage.addActor(test);

//        MaskTest maskTest = new MaskTest();
//        PixmapTest test = new PixmapTest();
//        stage.addActor(test);

//        PixMapDemo pixMapDemo = new PixMapDemo();
//        addActor(pixMapDemo);


//        MashLine line = new MashLine();
//        addActor(line);

//        PolyShowShape showShape = new PolyShowShape();
//        addActor(showShape);
//        MaskTest maskTest = new MaskTest();
//        addActor(maskTest);

//        GaussianGroup group = new GaussianGroup();
//        addActor(group);

//
//        BloomGroup group = new BloomGroup();
//        addActor(group);


//        VfxWidgetGroup vfxGroup = new VfxWidgetGroup(Pixmap.Format.RGB888);
//        vfxGroup.setFillParent(true);  // Let the group fill entire stage.
//        addActor(vfxGroup);
//        vfxGroup.addActor(new Image(new Texture("sprite.png")));
//
//        // Here's how effects can be added:
//        GaussianBlurEffect effect = new GaussianBlurEffect();
//
//
//        stage.addAction(Actions.forever(
//                Actions.delay(0.1F,Actions.run(()->{
//                    xx ++;
//                    effect.setPasses(xx);
//                }))
//        ));
//
//        effect.setType(GaussianBlurEffect.BlurType.Gaussian5x5b);
//        vfxGroup.getVfxManager().addEffect(effect);
    }

    private int xx = 0;

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
