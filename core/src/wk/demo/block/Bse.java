package wk.demo.block;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Bse extends Interpolation{
    private Array<Vector2> array;
    public Bse (Array<Vector2> array) {
        this.array = array;
    }

    public float apply (float a) {
        int v = (int)(a * 100.0F);
        return array.get(v).y/720;
    }
}
