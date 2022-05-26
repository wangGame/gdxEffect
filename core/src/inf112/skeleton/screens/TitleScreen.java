package inf112.skeleton.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.skeleton.RoboRally;
import inf112.skeleton.app.Board;


public class TitleScreen extends ScreenAdapter {
    private final Stage stage;
    private Music music;

    public TitleScreen(RoboRally game) {
        // Music
        music = Gdx.audio.newMusic(Gdx.files.internal("assets/audio/titlescreen_music.ogg"));
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
        // Creating stage and table to be filled with UI elements.
        Table table = new Table();
        table.setFillParent(true);
        stage = new Stage(new ScreenViewport());
        // Enables stage input.
        Gdx.input.setInputProcessor(stage);

        // Loading Textures
        Texture roboRallyLogoTexture = new Texture(Gdx.files.internal("menuElements/roboRallyLogo.png"));
        Image roboRallyLogo = new Image(roboRallyLogoTexture);

        // Using a skin as template for UI elements.
        Skin skin = new Skin(Gdx.files.internal("menuElements/roboRally.json"));

        // List boxes.
        SelectBox<String> mapChoiceBox = new SelectBox<String>(skin);
        String[] mapChoiceList = {"map001.tmx", "map002.tmx"};
        mapChoiceBox.setItems(mapChoiceList);

        SelectBox<String> playerCountChoice = new SelectBox<String>(skin);
        String[] playerCountList = {"1", "2", "3", "4"};
        playerCountChoice.setItems(playerCountList);

        // Creating clickable buttons below.

        /* Multiplayer button disposes of everything that's not needed and creates a game with a playerCount value.
         * Makes new Board which then initiates the game and lastly sets screen to GameScreen.
         */
        Button multiPlayerButton = new TextButton("Multiplayer", skin);
        multiPlayerButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                Integer playerCount = Integer.valueOf(playerCountChoice.getSelected());
                new Board(game, mapChoiceBox.getSelected(), playerCount);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        //Singleplayer button, same as multiplayer, but the playerCount value passed is == 1 for one player.

        Button singlePlayerButton = new TextButton("Singleplayer", skin);
        singlePlayerButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                Integer playerCount = 1;
                new Board(game, mapChoiceBox.getSelected(), playerCount);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        // Exit button, exits the application a "correct way".

        Button exitButton = new TextButton("Exit Game", skin);
        exitButton.addListener(new InputListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                System.exit(0);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        // Adding elements to the table and the table then to stage as actor.

        table.add(roboRallyLogo).top().colspan(4).padBottom(100).padTop(50);
        table.row().padBottom(30);

        table.add(singlePlayerButton).prefHeight(50).prefWidth(200).colspan(2);
        table.add(mapChoiceBox).colspan(2);
        table.row().padBottom(30);
        table.add(multiPlayerButton).prefHeight(50).prefWidth(200).colspan(2);
        table.add(playerCountChoice).colspan(1);
        table.row().padBottom(30);

        table.add(exitButton).prefHeight(50).prefWidth(200).colspan(2);
        table.row().padBottom(30);

        table.setFillParent(true);
        stage.addActor(table);
    }

    // Renders game at 30 frames per second.
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(112 / 255f, 128 / 255F, 144 / 255F, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    // Disposes when called on, for example to turn off the music when switching to GameScreen.
    @Override
    public void dispose() {
        super.dispose();
        music.dispose();
    }
}