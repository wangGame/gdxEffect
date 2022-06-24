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
    Music music;
    public MusicGroup(){
        music = Gdx.audio.newMusic(Gdx.files.internal("10134.mp3"));

        MusicVolumonAction addSound = new MusicVolumonAction(music);
        addSound.setName("1");
        music.play();
        addSound.setStart(0F);
        addSound.setEnd(1);
        addSound.setDuration(0.5F);

        MusicVolumonAction minusSound = new MusicVolumonAction(music);
//        music.play();
        minusSound.setStart(1F);
        minusSound.setName("2");
        minusSound.setEnd(0);
        minusSound.setDuration(0.5F);


        MusicVolumonAction midSound = new MusicVolumonAction(music);
//        music.play();
        midSound.setStart(0F);
        midSound.setName("3");
        midSound.setEnd(0);
        midSound.setReverse(true);
        midSound.setDuration(10);


//        actions.add(action);
//        RepeatAction forever = Actions.forever();

//        Action as = Actions.musicSequenceAction(
//                minusSound,midSound ,addSound
//        );
//        actions.add(as);
        CommonButton button = new CommonButton("button", -1, new CommonButton.MyRunnable() {
            @Override
            public void run(int index) {
                for (Action action1 : actions) {
                    System.out.println("reset");
                    action1.restart();
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

    public void close() {
        music.stop();
    }
}
