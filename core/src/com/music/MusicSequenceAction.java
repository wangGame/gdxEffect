package com.music;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.addAction;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.utils.Pool;

public class MusicSequenceAction extends ParallelAction {
    private int index;

    public MusicSequenceAction () {
    }

    public MusicSequenceAction (Action action1) {
        addAction(action1);
    }

    public MusicSequenceAction (Action action1, Action action2) {
        addAction(action1);
        addAction(action2);
    }

    public MusicSequenceAction (Action action1, Action action2, Action action3) {
        addAction(action1);
        addAction(action2);
        addAction(action3);
    }

    public MusicSequenceAction (Action action1, Action action2, Action action3, Action action4) {
        addAction(action1);
        addAction(action2);
        addAction(action3);
        addAction(action4);
    }

    public MusicSequenceAction (Action action1, Action action2, Action action3, Action action4, Action action5) {
        addAction(action1);
        addAction(action2);
        addAction(action3);
        addAction(action4);
        addAction(action5);
    }

    public boolean act (float delta) {
        if (index >= actions.size) return true;
        Pool pool = getPool();
        setPool(null); // Ensure this action can't be returned to the pool while executings.
        try {
            if (actions.get(index).act(delta)) {
//                if (actor == null) return true; // This action was removed.
                index++;
                if (index >= actions.size) return true;
            }
            return false;
        } finally {
            setPool(pool);
        }
    }

    @Override
    public void reset() {
        super.reset();
    }

    public void restart () {
        super.restart();
        index = 0;
    }
}
