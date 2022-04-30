package wk.demo.block.screen.load;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.tony.rider.test.MashLine;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import wk.demo.block.Bse;
import wk.demo.block.BseInterpolation;
import wk.demo.block.constant.Constant;
import wk.demo.block.utils.ShapeDraw;

public class GameView extends Group {
    private ShapeDraw shapeDraw;
    MashLine line ;
    public GameView(){
        setDebug(true);
        setSize(Constant.width,720);
        xx();
        line = new MashLine();
        addActor(line);
        this.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (y>720)return;
                super.clicked(event, x, y);
                long l = System.currentTimeMillis() - lastTime;
                lastTime = System.currentTimeMillis();
                if (l<1000){
                    Image image = new Image(new Texture("white_cir.png"));
                    addActor(image);
                    image.setPosition(x,y);
                    image.addListener(imgaeListener);
                    array.add(image);
                    array1.clear();
                    controlPoint.clear();
                    for (int i = 0; i < array.size; i++) {
                        controlPoint.add(array.get(i).getPosition());
                    }
                    jisuan(controlPoint);

                }
            }
        });

        Image image = new Image(new Texture("white_squ.png"));
        image.setPosition(getWidth() - 260,830);
        addActor(image);


        Image image1 = new Image(new Texture("white_squ.png"));
        addActor(image1);
        image1.setY(830);


        Image image3 = new Image(new Texture("white_squ.png"));
        addActor(image3);
        image3.setY(830);
        image3.setX(0,Align.center);
        image3.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
               image1.setScale(1,1);
            }
        });

        image.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
//                image1.addAction(Actions.scaleTo(3,3,3, new Bse(array1)));
                BseInterpolation bseInterpolation = new BseInterpolation();
                bseInterpolation.setCurve(0.25F, 0, 0.75F, 1F);
                image1.addAction(Actions.scaleTo(3,3,3, bseInterpolation));
                save();
            }
        });


    }
    Image image;
    private boolean start = false;
    private long lastTime = Integer.MIN_VALUE;
    private ClickListener imgaeListener = new ClickListener(){
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            return super.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            super.touchDragged(event, x, y, pointer);
            event.getTarget().setPosition(event.getStageX(),event.getStageY());
            array1.clear();
            controlPoint.clear();
            for (int i = 0; i < array.size; i++) {
                controlPoint.add(array.get(i).getPosition());
            }
            jisuan(controlPoint);

            line.setClear();

            for (int i = 0; i < array1.size; i++) {
                Vector2 vector2 = array1.get(i);
                line.setPoint(vector2);
            }}

        @Override
        public void clicked(InputEvent event, float x, float y) {
            super.clicked(event, x, y);
        }
    };

    private Array<Vector2> controlPoint = new Array<Vector2>();
    private Array<Image> array = new Array<>();
    private Array<Vector2> array1 = new Array<>();
    Vector2 sss = new Vector2(360,0);
    public void xx() {
//        [{x:69,y:732},{x:366,y:111},{x:300,y:774},{x:681,y:684}]
//        controlPoint.add(new Vector2(69, 732)); //起点
//        controlPoint.add(new Vector2(366, 111)); //控制点
//        controlPoint.add(new Vector2(300, 774)); //控制点
//        controlPoint.add(new Vector2(681, 684)); //终点

        controlPoint.add(new Vector2(0, 0)); //起点
        controlPoint.add(sss);
        controlPoint.add(new Vector2(710, 0)); //终点
        for (Vector2 vector2 : controlPoint) {
            Image image = new Image(new Texture("white_cir.png"));
            addActor(image);
            image.setPosition(vector2.x,vector2.y);
            image.addListener(imgaeListener);
            array.add(image);
        }
        jisuan(controlPoint);
        shapeDraw = new ShapeDraw();
        addActor(shapeDraw);
        shapeDraw.setArray(array1);
        shapeDraw.setLine(controlPoint);
    }

    public void jisuan(Array<Vector2> controlPoint){
        int n = controlPoint.size - 1; //
        int i, r;
        float u;
        // u的步长决定了曲线点的精度
        for (u = 0; u <= 1; u += 0.01) {
            Vector2 p[] = new Vector2[n + 1];
            for (i = 0; i <= n; i++) {
                p[i] = new Vector2(controlPoint.get(i));
            }
            for (r = 1; r <= n; r++) {
                for (i = 0; i <= n - r; i++) {
                    p[i].x = (1 - u) * p[i].x + u * p[i + 1].x;
                    p[i].y = (1 - u) * p[i].y + u * p[i + 1].y;
                }
            }
            array1.add(p[0]);
        }
    }

    public void save(){
        try {
            FileWriter stream = new FileWriter(new File("./text.txt"));
            for (Vector2 vector2 : controlPoint) {
                stream.write(vector2.x+", "+vector2.y+" ");
            }
            stream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private float timess = 0;
    private int index= 0;
    private int deIndex = 1;
    @Override
    public void act(float delta) {
        super.act(delta);
        timess += delta;
        if (start) {
            if (timess > 0.1F){
                sss.y = sss.y +5;
                array1.clear();
                jisuan(controlPoint);
                if (index>=array1.size-1)deIndex =-1;
                if (index<=0)deIndex = 1;
                Vector2 vector2 = array1.get(index);
                image.setPosition(vector2.x,vector2.y,Align.center);
                timess = 0;
                index+=deIndex;
            }
        }
    }
}
