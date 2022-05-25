package com.screenanima;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class App extends Group {
    public App(){
        Table table = new Table(){{

            Image image = new Image(new Texture("white_squ.png"));
            add(image);
            image.addListener(new ClickListener());
            add(new Image(new Texture("white_squ.png")));
            add(new Image(new Texture("white_squ.png")));
            add(new Image(new Texture("white_squ.png")));
            row();
            add(new Image(new Texture("white_squ.png")));
            add(new Image(new Texture("white_squ.png")));
            add(new Image(new Texture("white_squ.png")));
            add(new Image(new Texture("white_squ.png")));
            pack();
        }};
        addActor(table);
    }

}
