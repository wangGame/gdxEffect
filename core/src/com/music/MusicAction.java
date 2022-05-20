package com.music;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

public class MusicAction extends TemporalAction {
    private Music music;
    private float start;
    private float end;
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }

    public float getStart() {
        return start;
    }

    public void setStart(float start) {
        this.start = start;
    }

    public float getEnd() {
        return end;
    }

    public void setEnd(float end) {
        this.end = end;
    }

    public MusicAction(Music music){
        this.music = music;
    }

    @Override
    public void reset() {
        super.reset();
        music.setVolume(start);
    }

    protected void update(float percent) {
        float v = (Math.abs(end - start)) * percent;
        music.setVolume(v);
        System.out.println(v);
    }
}
