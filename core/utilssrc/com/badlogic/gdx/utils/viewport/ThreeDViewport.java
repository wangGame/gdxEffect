package com.badlogic.gdx.utils.viewport;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;

public class ThreeDViewport extends Viewport{
    private float minWorldWidth, minWorldHeight;
    private float maxWorldWidth, maxWorldHeight;

    /** Creates a new viewport using a new {@link OrthographicCamera} with no maximum world size. */
    public ThreeDViewport (float minWorldWidth, float minWorldHeight) {
        this(minWorldWidth, minWorldHeight, 0, 0, new PerspectiveCamera());
    }

    /** Creates a new viewport with no maximum world size. */
    public ThreeDViewport (float minWorldWidth, float minWorldHeight, Camera camera) {
        this(minWorldWidth, minWorldHeight, 0, 0, camera);
    }
    /** Creates a new viewport with a maximum world size.
     * @param maxWorldWidth User 0 for no maximum width.
     * @param maxWorldHeight User 0 for no maximum height. */
    public ThreeDViewport (float minWorldWidth, float minWorldHeight, float maxWorldWidth, float maxWorldHeight, Camera camera) {
        this.minWorldWidth = minWorldWidth;
        this.minWorldHeight = minWorldHeight;
        this.maxWorldWidth = maxWorldWidth;
        this.maxWorldHeight = maxWorldHeight;
        setCamera(camera);
        camera.position.set( 10, 10, 10 );
        camera.lookAt( 0, 0, 0 );
        camera.near = 1f;
        camera.far = 300f;
        camera.update();
    }

    @Override
    public void update (int screenWidth, int screenHeight, boolean centerCamera) {
        // Fit min size to the screen.
        float worldWidth = minWorldWidth;
        float worldHeight = minWorldHeight;
        Vector2 scaled = Scaling.fit.apply(worldWidth, worldHeight, screenWidth, screenHeight);

        // Extend in the short direction.
        int viewportWidth = Math.round(scaled.x);
        int viewportHeight = Math.round(scaled.y);
        if (viewportWidth < screenWidth) {
            float toViewportSpace = viewportHeight / worldHeight;
            float toWorldSpace = worldHeight / viewportHeight;
            float lengthen = (screenWidth - viewportWidth) * toWorldSpace;
            if (maxWorldWidth > 0) lengthen = Math.min(lengthen, maxWorldWidth - minWorldWidth);
            worldWidth += lengthen;
            viewportWidth += Math.round(lengthen * toViewportSpace);
        } else if (viewportHeight < screenHeight) {
            float toViewportSpace = viewportWidth / worldWidth;
            float toWorldSpace = worldWidth / viewportWidth;
            float lengthen = (screenHeight - viewportHeight) * toWorldSpace;
            if (maxWorldHeight > 0) lengthen = Math.min(lengthen, maxWorldHeight - minWorldHeight);
            worldHeight += lengthen;
            viewportHeight += Math.round(lengthen * toViewportSpace);
        }

        setWorldSize(worldWidth, worldHeight);

        // Center.
        setScreenBounds((screenWidth - viewportWidth) / 2, (screenHeight - viewportHeight) / 2, viewportWidth, viewportHeight);

        apply(centerCamera);

    }

    public float getMinWorldWidth () {
        return minWorldWidth;
    }

    public void setMinWorldWidth (float minWorldWidth) {
        this.minWorldWidth = minWorldWidth;
    }

    public float getMinWorldHeight () {
        return minWorldHeight;
    }

    public void setMinWorldHeight (float minWorldHeight) {
        this.minWorldHeight = minWorldHeight;
    }

    public float getMaxWorldWidth () {
        return maxWorldWidth;
    }

    public void setMaxWorldWidth (float maxWorldWidth) {
        this.maxWorldWidth = maxWorldWidth;
    }

    public float getMaxWorldHeight () {
        return maxWorldHeight;
    }

    public void setMaxWorldHeight (float maxWorldHeight) {
        this.maxWorldHeight = maxWorldHeight;
    }
}
