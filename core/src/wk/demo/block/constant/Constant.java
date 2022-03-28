package wk.demo.block.constant;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Constant {
    public static final int BLOCK = 1;
    public static final int TARGET = 0;

    public static AssetManager assetManager;
    public static float width;
    public static float height;
    public static Viewport viewport;
    public static OrthographicCamera camera;
    public static ShapeRenderer sr;
}
