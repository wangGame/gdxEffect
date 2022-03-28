package wk.demo.block.screen.load;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import wk.demo.block.Bziercurve;
import wk.demo.block.constant.Constant;
import wk.demo.block.screen.base.BaseScreen;

public class LoadingScreen extends BaseScreen {

    private Array<Image> array = new Array<>();
    public LoadingScreen(Bziercurve game) {
        super(game);
    }

    @Override
    public void showView() {
        super.showView();
        GameView view = new GameView();
        addActor(view);
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
            }
        });
    }



    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
