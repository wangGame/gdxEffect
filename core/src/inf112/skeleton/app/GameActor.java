package inf112.skeleton.app;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameActor extends Actor {
    private final OrthogonalTiledMapRenderer myRenderer;
    private OrthographicCamera myCam;
    public int mapWidth;
    public int mapHeight;


    public GameActor(GameInit gameInit, float unitScale) {
        TiledMap map = gameInit.getMapBuilder().getMap();
        mapWidth = gameInit.getMapBuilder().mapWidth;
        mapHeight = gameInit.getMapBuilder().mapHeight;
        setWidth(1 / 300f * mapWidth * unitScale);
        setHeight(1 / 300f * mapHeight * unitScale);
        myRenderer = new OrthogonalTiledMapRenderer(map, unitScale);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();
        myCam = new OrthographicCamera();
        myCam.setToOrtho(false, (mapWidth), mapHeight);
        myCam.position.set(mapWidth / 2F, mapHeight / 4F, 0.0F);
        myCam.update();
        myRenderer.setView(myCam);
        myRenderer.render();

        myCam.update();
        batch.begin();
    }
}
