package inf112.skeleton.app;


import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.RoboRally;
import inf112.skeleton.screens.GameScreen;
import inf112.skeleton.app.GameInit;


public class Board extends InputAdapter {

    private final GameInit gameInit;

    private SpriteBatch batch;
    private BitmapFont font;

    public TiledMap map;
    private TiledMapTileLayer boardLayer, playerLayer, holeLayer, flagLayer, laserLayer, movementLayer, barriersLayer;

    private OrthogonalTiledMapRenderer myRenderer;
    public OrthographicCamera myCam;

    private TiledMapTileLayer.Cell playerCell;
    private TiledMapTileLayer.Cell playerWonCell;
    private TiledMapTileLayer.Cell playerDiedCell;

    private Vector2 playerPos;
    private int xPos = 0, yPos = 0;

    public String mapName;

    private final InputMultiplexer inputMultiplexer;
    private final int playerLimit;
    private final GameScreen gameScreen;


    public Board(RoboRally game, String map_filename,int playerLimit) {
        this.playerLimit = playerLimit;

        mapName = map_filename;
        this.gameInit = new GameInit(map_filename, playerLimit);

        inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(this);
        Gdx.input.setInputProcessor(inputMultiplexer);

        gameScreen = new GameScreen(gameInit, this, inputMultiplexer);
        game.setScreen(gameScreen);

    }
}