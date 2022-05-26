package inf112.skeleton.grid;

/**
 * Source is inf101 spring 20 semester oblig 2 solution
 */

public class Location {

    private final int row;
    private final int col;
    private int layer;

    public Location(int x, int y, int layer) {
        this.row = y;
        this.col = x;
        this.layer = layer;
    }

    public Location(int x, int y) {
        this.row = y;
        this.col = x;
        this.layer = -1;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getLayer() {
        return layer;
    }

    public boolean hasLayer() {
        return (getLayer() != -1);
    }

    public boolean setLayer(int layer1) {
        if (hasLayer()) return false;
        else {
            layer = layer1;
            return true;
        }
    }

    /**
     * Checks for same coordinate through layers.
     */
    public boolean sameRowCol(Location loc) {
        return ((getRow() == loc.getRow()) && (getCol() == loc.getCol()));
    }

    /**
     * Returns location for next move in Direction dir
     */
    public Location move(Directions dir) {
        return new Location(col + dir.getDx(), row + dir.getDy(), layer);
    }

    /**
     * Checks if row, column and layer is the same.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Location)) {
            return false;
        }
        Location loc = (Location) obj;
        return this.getRow() == loc.getRow() && this.getCol() == loc.getCol() && this.getLayer() == loc.getLayer();
    }

    @Override
    public String toString() {
        return "x= " + col + " y= " + row + " layer=" + layer;
    }

}