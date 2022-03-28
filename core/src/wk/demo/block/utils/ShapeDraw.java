package wk.demo.block.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import wk.demo.block.constant.Constant;

public class ShapeDraw extends Actor {
    ShapeRenderer sr;
    private Array<Vector2> array;
    public ShapeDraw() {
        this.sr = Constant.sr;
    }

    public void setArray(Array<Vector2> array) {
        this.array = array;
    }

    public Array<Vector2> line = new Array<>();

    public void setLine(Array<Vector2> line) {
        this.line = line;
    }

    private Vector2 lastPosition = null;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        sr.setProjectionMatrix(batch.getProjectionMatrix());
        sr.setTransformMatrix(batch.getTransformMatrix());
        Gdx.gl20.glLineWidth(1 / Constant.camera.zoom);
        sr.begin(ShapeRenderer.ShapeType.Line);
        for (Vector2 vector2 : array) {
//            sr.circle(vector2.x, vector2.y, 10);
            sr.rect(vector2.x,vector2.y,10,1);
        }
        for (Vector2 vector2 : line) {
            if (lastPosition!=null){
//                sr.line(vector2,lastPosition);
                sr.rectLine(vector2,lastPosition,10);
            }
            lastPosition = vector2;
        }
        lastPosition = null;
        sr.end();
//        setWidth(getWidth() + 1);
        batch.begin();
    }
}

//    @Override
//    public void create() {
//        w = Gdx.graphics.getWidth();
//        batch = new SpriteBatch();
//        pixmap = new Pixmap(2 * w, 2 * w, Pixmap.Format.RGBA8888);
//        pixmap.setColor(Color.BLACK);
//
//        pixmap.drawCircle(w, w, w);
//        texture = new Texture(pixmap);
//        pixmap.dispose();
//
//        sprite = new Sprite(texture);
//    }
//
//    @Override
//    public void render() {
//        Gdx.gl.glClearColor(1, 1, 1, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        batch.begin();
//        sprite.setPosition(-w / 2, -w);
//        sprite.draw(batch);
//        batch.end();
//    }