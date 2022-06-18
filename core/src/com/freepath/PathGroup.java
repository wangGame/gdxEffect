package com.freepath;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.tony.rider.Constant;

public class PathGroup extends Group {
    private ShapeRenderer shapeRenderer;
    private Array<Vector2> vector2s = new Array<>();
    private float baseX = 0;
    private float baseY = 0;
    public PathGroup(){
        setSize(Constant.GAMEWIDTH,Constant.GAMEHIGHT);
        shapeRenderer = new ShapeRenderer();

        addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                vector2s.clear();
                vector2s.add(new Vector2(x,y));
                baseX = x;
                baseY = y;

                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                super.touchDragged(event, x, y, pointer);
                vector2s.add(new Vector2(event.getStageX(),event.getStageY()));
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

            }
        });
    }

    private Vector2 tets1 = new Vector2(10,10);
    private Vector2 tets2 = new Vector2(160,180);

    /**
     * 设置宽度之后，它的接口处不是很齐
     *
     * 可以自己来组合顶点
     * @param batch
     * @param parentAlpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        Gdx.gl.glLineWidth(10);
        for (int i = 1; i < vector2s.size; i++) {
            shapeRenderer.line(vector2s.get(i-1),vector2s.get(i));
        }
        shapeRenderer.end();
    }
}
