package com.tony.rider.screen.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.tony.rider.RiderGame;

public class BaseScreen implements Screen {
    protected Stage stage;
    protected Group rootView;
    protected String viewpath;
    protected float offsetY;
    protected float offsetX;
    protected boolean back;

    public BaseScreen(){
        stage = new Stage(getStageViewport(), getBatch());
        setOffset();
        Gdx.input.setInputProcessor(stage);
    }

    private void setOffset() {
    }

    private Batch getBatch() {
        return RiderGame.instence().getBatch();
    }

    private ExtendViewport getStageViewport() {
        return RiderGame.instence().getStageViewport();
    }

    public void touchDisable(){
        Gdx.input.setInputProcessor(null);
    }

    public void touchEnable(){
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        initTouch();
        initRootView();
    }

    private void initRootView() {

    }

    private void initTouch() {
        stage.addListener(BackInputListener());
        touchEnable();
    }

    private InputListener BackInputListener() {
        return new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if ((keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK)) {
                    back = true;
                }
                return super.keyDown(event, keycode);
            }
        };
    }

    @Override
    public void render(float delta) {
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
//        observer.clear();
//        CocosResource.unLoadFile(viewpath);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    public void addActor(Actor addActor){
        stage.addActor(addActor);
    }

    public void setScreen(BaseScreen screen) {
        RiderGame.instence().setScreen(screen);
    }

    public Stage getStage() {
        return stage;
    }

    public <T extends Actor> T findActor(String name){
        return rootView.findActor(name);
    }
}

