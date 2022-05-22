package com.music;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;


public class MusicVolumonAction extends MusicTemporalAction {
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

    public MusicVolumonAction(Music music){
        this.music = music;
    }

    public MusicVolumonAction(Music music,
                              String name,
                              float start,
                              float end,
                              float duration
                              ){
        this.music = music;
        this.name = name;
        this.start = start;
        this.end = end;
        setDuration(duration);
    }

    @Override
    public void reset() {
        super.reset();
        music.setVolume(start);
    }

    protected void update(float percent) {
        float v = (start+(end - start)* percent) ;
        music.setVolume(v);
        System.out.println(start+"---"+v);
    }
}
