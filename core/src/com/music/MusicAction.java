package com.music;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Pool;

abstract public class MusicAction implements Pool.Poolable {
    /** The actor this action is attached to, or null if it is not attached. */
    protected Music actor;

    private Pool pool;

    /** Updates the action based on time. Typically this is called each frame by {@link Actor#act(float)}.
     * @param delta Time in seconds since the last frame.
     * @return true if the action is done. This method may continue to be called after the action is done. */
    abstract public boolean act (float delta);

    /** Sets the state of the action so it can be run again. */
    public void restart () {
    }

    /** Sets the actor this action is attached to. This also sets the {target} actor if it is null. This
     * method is called automatically when an action is added to an actor. This method is also called with null when an action is
     * removed from an actor.
     * <p>
     * When set to null, if the action has a {@link #setPool(Pool) pool} then the action is {@link Pool#free(Object) returned} to
     * the pool (which calls {@link #reset()}) and the pool is set to null. If the action does not have a pool, {@link #reset()} is
     * not called.
     * <p>
     * This method is not typically a good place for an action subclass to query the actor's state because the action may not be
     * executed for some time, eg it may be {@link DelayAction delayed}. The actor's state is best queried in the first call to
     * {@link #act(float)}. For a {@link TemporalAction}, use TemporalAction#begin(). */
    public void setActor (Music actor) {
        this.actor = actor;
        if (actor == null) {
            if (pool != null) {
                pool.free(this);
                pool = null;
            }
        }
    }

    /** @return null if the action is not attached to an actor. */
    public Music getActor () {
        return actor;
    }

    /** Resets the optional state of this action to as if it were newly created, allowing the action to be pooled and reused. State
     * required to be set for every usage of this action or computed during the action does not need to be reset.
     * <p>
     * The default implementation calls {@link #restart()}.
     * <p>
     * If a subclass has optional state, it must override this method, call super, and reset the optional state. */
    public void reset () {
        actor = null;
        pool = null;
        restart();
    }

    public Pool getPool () {
        return pool;
    }

    /** Sets the pool that the action will be returned to when removed from the actor.
     * @param pool May be null.
     * @see #setActor(Music) */
    public void setPool (Pool pool) {
        this.pool = pool;
    }

    public String toString () {
        String name = getClass().getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex != -1) name = name.substring(dotIndex + 1);
        if (name.endsWith("Action")) name = name.substring(0, name.length() - 6);
        return name;
    }
}

