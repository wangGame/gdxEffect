package com.music;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.tony.rider.actor.CommonButton;

public class MusicGroup extends Group {
    public Array<Action> actions = new Array<>();
    public MusicGroup(){
        Music music = Gdx.audio.newMusic(Gdx.files.internal("10134.mp3"));

        MusicAction addSound = new MusicAction(music);
        addSound.setName("1");
        music.play();
        addSound.setStart(0F);
        addSound.setEnd(1);
        addSound.setDuration(2);

        MusicAction minusSound = new MusicAction(music);
//        music.play();
        minusSound.setStart(0F);
        minusSound.setName("2");
        minusSound.setEnd(1);
        minusSound.setReverse(true);
        minusSound.setDuration(2);
//        actions.add(action);
//        RepeatAction forever = Actions.forever();

        Action as = Actions.sequence(
                addSound, minusSound
        );
        actions.add(as);
        CommonButton button = new CommonButton("button", -1, new CommonButton.MyRunnable() {
            @Override
            public void run(int index) {
                for (Action action1 : actions) {
                    action1.reset();
                }
            }
        });
        addActor(button);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for (Action action : actions) {
            action.act(delta);

        }
    }
}
