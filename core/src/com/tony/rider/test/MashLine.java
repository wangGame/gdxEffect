package com.tony.rider.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;

import java.util.HashSet;
import java.util.LinkedHashSet;

import wk.demo.block.screen.load.GameView;

public class MashLine extends Group {
    private float mRadius = 0.0f;
    private float mThickness = 0.0f;
    private Array<Vector2> mPoints = null;
    private Boolean bDrawOutlines = false;
    private Boolean bDrawConstruction = false;
    ShapeRenderer shapeRender = null;
    ShaderProgram shader = null;
    Mesh mesh = null;
    FloatArray vertextArr = null;
    Texture texture = null;
    HashSet<TVertexData> vertexSet = null;

    public MashLine() {
        setSize(720,1280);
        setTouchable(Touchable.disabled);
        mRadius = 5.0f;
        mPoints = new Array<Vector2>();
        bDrawOutlines = true;
        bDrawConstruction = true;
        shapeRender = new ShapeRenderer();
        shader = new ShaderProgram(Gdx.files.internal("surface_vertex.gles"), Gdx.files.internal("surface_fragment.gles"));
        vertextArr = new FloatArray();
        texture = new Texture("line.png");
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        mPoints = new Array<>();
        mThickness = texture.getHeight() / 2;
        vertexSet = new LinkedHashSet<>();





        addListener(new ClickListener() {
            Vector2 tmp = new Vector2();

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                tmp.setZero();
                tmp.set(x, y);
                mPoints.add(tmp.cpy());
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (mPoints.size > 1){
                    Gdx.app.log("xx", "point size:" + mPoints.size);
                    if (mesh != null) {
                        mesh.dispose();
                    }
                    MeshTools meshtools = new MeshTools();
                    vertextArr = meshtools.convert(mPoints, texture, mThickness);
                    int num = vertextArr.size / 4;
                    mesh = new Mesh(
                            true,
                            num,
                            num,
                            new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
                            new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord0")
                    );
                    mesh.setVertices(vertextArr.toArray());

                    ShortArray indexs = new ShortArray();
                    for (int i = 0; i < num; i++) {
                        indexs.add(i);
                    }
                    Gdx.app.log("", "" + indexs.size);
                    mesh.setIndices(indexs.toArray());
                    vertexSet.clear();
                    vertextArr.clear();
                }
            }
        });

    }

    public void setClear(){
        mPoints.clear();
    }

    public void setPoint(Vector2 vector2){
        mPoints.add(vector2.cpy());
        if (mPoints.size > 1) {
            MeshTools meshtools = new MeshTools();
            vertextArr = meshtools.convert(mPoints, texture, mThickness);
            int num = vertextArr.size / 4;
            mesh = new Mesh(
                    true,
                    num,
                    num,
                    new VertexAttribute(VertexAttributes.Usage.Position, 2, "a_position"),
                    new VertexAttribute(VertexAttributes.Usage.TextureCoordinates, 2, "a_texCoord0")
            );
            mesh.setVertices(vertextArr.toArray());

            ShortArray indexs = new ShortArray();
            for (int i = 0; i < num; i++) {
                indexs.add(i);
            }
            Gdx.app.log("", "" + indexs.size);
            mesh.setIndices(indexs.toArray());
            vertexSet.clear();
            vertextArr.clear();
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for (int i = 0; i < mPoints.size; i++) {
            Vector2 vec = mPoints.get(i);
            shapeRender.setColor(Color.RED);
            shapeRender.begin(ShapeRenderer.ShapeType.Filled);
            shapeRender.circle(vec.x, vec.y, mRadius);
            shapeRender.end();
        }
        for (int i = 0; i < mPoints.size; i++) {
            int a = i-1<0 ? 0 : i - 1;
            int b = i;
            int c = i + 1 >= mPoints.size ? mPoints.size - 1 : i + 1;
            int d = i + 2 >= mPoints.size ? mPoints.size - 1 : i + 2;
            drawSegment(mPoints.get(a), mPoints.get(b), mPoints.get(c), mPoints.get(d));
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (mesh != null) {
            Gdx.gl.glDepthMask(false);
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            Gdx.gl.glEnable(GL20.GL_TEXTURE_2D);
            texture.bind();
            shader.begin();
            shader.setUniformMatrix("u_worldView", batch.getProjectionMatrix());
            mesh.render(shader, GL20.GL_TRIANGLE_STRIP);
            shader.end();

            Gdx.gl.glDepthMask(true);
        }
    }


    void insertData( TVertexData data, FloatArray array) {
        Gdx.app.log("", data.toString());
        array.add(data.pos.x);
        array.add(data.pos.y);

        array.add(data.u);
        array.add(data.v);
    }

    void drawSegment(Vector2 p0, Vector2 p1,Vector2 p2 ,Vector2 p3 ) {
        if (p1.equals(p2)) return;

        Vector2 line = p2.cpy().sub(p1).nor();

        Vector2 normal = new Vector2(-line.y, line.x).nor();

        Vector2 tangent1 =  (p0 == p1) ? line : ((p1.cpy().sub(p0).nor().add(line)).nor());
        Vector2 tangent2 =  (p2 == p3) ? line : ((p3.cpy().sub(p2).nor().add(line)).nor());
        Vector2 miter1 = new Vector2(-tangent1.y, tangent1.x);
        Vector2 miter2 = new Vector2(-tangent2.y, tangent2.x);


        float length1 = mThickness / normal.dot(miter1);
        float length2 = mThickness / normal.dot(miter2);

        shapeRender.begin(ShapeRenderer.ShapeType.Line);

        if (bDrawConstruction) {
            Gdx.gl.glLineWidth(1.0f);
            shapeRender.setColor(Color.BLACK);
            shapeRender.line(p1, p2);
            // draw normals in stippled red
            shapeRender.setColor(Color.RED);
            Gdx.gl.glEnable(GL20.GL_LINE_STRIP);
            drawDashedLine(Color.RED, shapeRender, new  Vector2(5f, 5f), p1.cpy().sub(normal.cpy().scl(mThickness)), p1.cpy().add(normal.cpy().scl(mThickness)), 0.5f);
            drawDashedLine(Color.RED, shapeRender, new  Vector2(5f, 5f), p2.cpy().sub(normal.cpy().scl(mThickness)), p2.cpy().add(normal.cpy().scl(mThickness)), 0.5f);
            Gdx.gl.glDisable(GL20.GL_LINE_STRIP);

            shapeRender.setColor(Color.GRAY);
            Gdx.gl.glEnable(GL20.GL_LINE_STRIP);
            shapeRender.line(p1.cpy().sub(normal.cpy().scl(mThickness)), p2.cpy().sub(normal.cpy().scl(mThickness)));
            shapeRender.line(p1.cpy().add(normal.cpy().scl(mThickness)), p2.cpy().add(normal.cpy().scl(mThickness)));
            Gdx.gl.glDisable(GL20.GL_LINE_STRIP);
        }

        if (bDrawOutlines) {
            Gdx.gl.glLineWidth(1.5f);
            shapeRender.setColor(Color.BLACK);
            Vector2 v0 = p1.cpy().sub(miter1.cpy().scl(length1));
            Vector2 v1 = p2.cpy().sub(miter2.cpy().scl(length2));

            Vector2 v2 = p1.cpy().add(miter1.cpy().scl(length1));
            Vector2 v3 = p2.cpy().add(miter2.cpy().scl(length2));

            shapeRender.line(v0, v1);
            shapeRender.line(v2, v3);

            Gdx.gl.glEnable(GL20.GL_LINE_STRIP);
            drawDashedLine(Color.BLACK, shapeRender,new  Vector2(5f, 5f), p1.cpy().sub(miter1.cpy().scl(length1)), p1.cpy().add(miter1.cpy().scl(length1)), 0.5f);
            drawDashedLine(Color.BLACK, shapeRender, new Vector2(5f, 5f), p1.cpy().sub(miter1.cpy().scl(length1)), p2.cpy().add(miter2.cpy().scl(length2)), 0.5f);
            drawDashedLine(Color.BLACK, shapeRender,new Vector2(5f, 5f), p2.cpy().sub(miter2.cpy().scl(length2)), p2.cpy().add(miter2.cpy().scl(length2)), 0.5f);
            Gdx.gl.glDisable(GL20.GL_LINE_STRIP);
        }
        shapeRender.end();
    }

    void drawDashedLine(Color color ,ShapeRenderer renderer,Vector2 dashes ,Vector2 start,Vector2 end ,Float width) {
        if (dashes.x == 0.0f) {
            return;
        }
        float dirX = end.x - start.x;
        float dirY = end.y - start.y;

        float length = Vector2.len(dirX, dirY);
        dirX /= length;
        dirY /= length;

        float curLen = 0f;
        float curX = 0f;
        float curY = 0f;

        renderer.setColor(color);

        while (curLen <= length) {
            curX = start.x + dirX * curLen;
            curY = start.y + dirY * curLen;
            renderer.rectLine(curX, curY, curX + dirX * dashes.x, curY + dirY * dashes.x, width);
            curLen += dashes.x + dashes.y;

        }
    }

    private Vector2 save4() {
        this.x = ((Math.round(this.x * 10000f)) / 10000f);
        this.y = ((Math.round(this.y * 10000f)) / 10000f);
        return null;
    }
}

