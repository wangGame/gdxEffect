package com.test.shaderstest;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class ShadersTestMain extends ApplicationAdapter {
	private SpriteBatch batch;
    private Texture colorTable;
    private Texture imgPixelGuy;
    private Sprite spritePixelGuy, spritePixelGuy2, spritePixelGuy3;
    private ShaderProgram shader;
    private String shaderVertIndexPalette, shaderFragIndexPalette;

    @Override
    public void create () {
    	batch = new SpriteBatch();
        colorTable =  new Texture("colortable.png"); // Texture with 6x6 pixels, each row is a palette (total: 6 palettes of 6 colors each)
        imgPixelGuy = new Texture("pixel_guy.png"); // Texture for our sprite
        // We'll apply our shader (simulating several palettes) to the following Sprites
        spritePixelGuy = new Sprite (imgPixelGuy);
        spritePixelGuy2 = new Sprite(imgPixelGuy); 
        spritePixelGuy3 = new Sprite(imgPixelGuy); 

        spritePixelGuy.setPosition(24, 50);
        spritePixelGuy2.setPosition(224, 50);
        spritePixelGuy3.setPosition(424, 50);

        // Apply one of the palettes to the first sprite.
        setPalette(spritePixelGuy, 0); // setPalette (Sprite sprite, int paletteNumber) 
        // Another option would be calling batch.setColor(paletteIndex,0,0,0); before calling batch.draw for each one of the sprites.

        // Apply other palettes to the other sprites
        setPalette(spritePixelGuy2, 1);
        setPalette(spritePixelGuy3, 5);

        // Load string values for Vertex and Fragment Shaders 
        shaderVertIndexPalette = Gdx.files.internal("shaders/indexpalette.vert").readString();
        shaderFragIndexPalette = Gdx.files.internal("shaders/indexpalette.frag").readString();

        ShaderProgram.pedantic = false; // important since we aren't using some uniforms and attributes that SpriteBatch expects

        shader = new ShaderProgram(shaderVertIndexPalette, shaderFragIndexPalette);
        if(!shader.isCompiled()) {
            System.out.println("Problem compiling shader :(");
        }
        else{
            batch.setShader(shader);

            // Bind and set the uniform for the colortable texture
            colorTable.bind(1);
            shader.begin();
            shader.setUniformi("colorTable", 1);
            shader.end();
        }
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Must return active texture unit to default of 0 before batch.end 
        //because SpriteBatch does not automatically do this. It will bind the
        //sprite's texture to whatever the current active unit is, and assumes
        //the active unit is 0
        Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0); 

        batch.begin(); //shader.begin is called internally by this line
        spritePixelGuy.draw(batch);
        spritePixelGuy2.draw(batch);
        spritePixelGuy3.draw(batch);
        batch.end(); //shader.end is called internally by this line
    }

    private void setPalette (Sprite sprite, int paletteNumber){
    	// We calcule the value for the paletteIndex. The +0.5 is so we are sampling from the center of each texel in the texture
    	float paletteIndex = (paletteNumber + 0.5f) / colorTable.getHeight();

    	// We set the float value for the palette index within the R channel, by calling sprite.setColor(R, G, B, A)
    	sprite.setColor(paletteIndex, 0, 0, 0);
    }
}