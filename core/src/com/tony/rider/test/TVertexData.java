package com.tony.rider.test;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class TVertexData {
    Vector2 pos = Vector2.Zero;
    Float u = 0f;
    Float v  = 0f;

    public TVertexData(Vector2 p,Float u,Float v){
        this.pos = new Vector2(p);
        this.u = u;
        this.v = v;
    }

    public String toString(){
        return "TVertexData(pos=$pos, u=$u, v=$v)";
    }

    public Boolean equals(TVertexData other){
        if (this == other) return true;
        if (other instanceof TVertexData) return false;

        if (pos != other.pos) return false;
        if (u != other.u) return false;
        if (v != other.v) return false;

        return true;
    }

    public int hashCode() {
        int result = pos.hashCode();
        result = 31 * result + u.hashCode();
        result = 31 * result + v.hashCode();

        return result;
    }

}
