package inf112.skeleton.game;

import inf112.skeleton.grid.*;

import java.util.ArrayList;

/**
 * Barricade is a wall blocking path in specified direction
 */
public class Barricade implements ITileObject {
    /**
     * Location loc is coordiante
     * ArrayList Direction is the directions it is blocking
     */
    private final int layer = 3;
    private Location loc;
    private ArrayList<Directions> facing;

    public Barricade(Location loc, Directions dir) {
        this.loc = loc;
        this.facing = new ArrayList<Directions>();
        this.facing.add(dir);
    }

    public Location getLocation() {
        return loc;
    }

    /**
     *  Adds direction to block
     */
    public void addBarricade(Directions dir) {
        if (facing.contains(dir)) {
            return;
        } else {
            facing.add(dir);
        }
    }

    /**
     * Checks if Barricade is blocking direction dir
     */
    public boolean isBlocking(Directions dir) {
        for (Directions face : facing) {
            if (face == dir) {
                return true;
            }
        }
        return false;
    }
}
