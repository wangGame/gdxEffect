package inf112.skeleton.grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Source is inf101 spring 20 semester oblig 2 solution
 */

public class Grid<T> implements IGrid<T> {
    private List<T> cells;
    private int rows;
    private int cols;
    private int layer;

    public Grid(int rows, int cols, T initElement, int layer) {
        if (cols <= 0 || rows <= 0)
            throw new IllegalArgumentException("number of rows and columns must be positive");

        this.rows = rows;
        this.cols = cols;
        this.layer = layer;
        cells = new ArrayList<>(rows * cols);
        for (int i = 0; i < rows * cols; i++) {
            cells.add(initElement);
        }
    }

    public Grid() {
    }

    @Override
    public int numRows() {
        return rows;
    }

    @Override
    public int numCols() {
        return cols;
    }

    @Override
    public int numLayers() {
        return layer;
    }

    @Override
    public void set(Location loc, T elem) {
        check(loc);

        cells.set(indexOf(loc), elem);
    }

    /**
     * Computes the right index in the list from a location in the inf112.skeleton.grid
     *
     * @param loc - the location in the inf112.skeleton.grid
     * @return the index in the list cells
     */
    private int indexOf(Location loc) {
        return loc.getCol() + loc.getRow() * cols;
    }

    /**
     * Computes the location corresponding to an index in the list
     *
     * @param index
     * @return
     */
    private Location locationFromIndex(int index) {
        if (index < 0 || index > cells.size())
            throw new IndexOutOfBoundsException("index is not a valid index of the board");
        return new Location(index % cols, index / cols, layer);
    }

    @Override
    public T get(Location loc) {
        check(loc);

        return cells.get(indexOf(loc));
    }

    @Override
    public IGrid<T> copy() {
        Grid<T> newGrid = new Grid<>(numRows(), numCols(), null, numLayers());
        copy(newGrid);
        return newGrid;
    }

    protected void copy(IGrid<T> newGrid) {
        for (Location loc : locations())
            newGrid.set(loc, get(loc));
    }

    /**
     * Checks that a given xy coordinate is within the bounds of the Grid
     *
     * @param x
     * @param y
     * @return
     */
    public boolean validCoordinate(int x, int y) {
        if (x < 0 || x >= cols || y < 0 || y >= rows)
            return false;
        return true;
    }

    /**
     * Checks if a given location is within the limits of the inf112.skeleton.grid
     *
     * @param loc
     * @return
     */
    public boolean validLocation(Location loc) {
        return validCoordinate(loc.getCol(), loc.getRow());
    }

    /**
     * Checks if a given xy coordinate is within the bounds of the Grid.
     * If not an exception is thrown
     */
    public void check(Location loc) {
        if (!validLocation(loc))
            throw new IndexOutOfBoundsException();
    }

    @Override
    public boolean contains(Object obj) {
        return this.cells.contains(obj);
    }

    @Override
    public Location locationOf(Object target) {
        int index = this.cells.indexOf(target);
        if (index < 0)
            return null;
        else
            return locationFromIndex(index);
    }

    @Override
    public Iterator<T> iterator() {
        return cells.iterator();
    }

    @Override
    public LocationIterator locations() {
        return new LocationIterator(this);
    }

    @Override
    public String toString() {
        return "layer= " + layer + "\n" + cells.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        Grid<T> grid = (Grid<T>) obj;
        return this.cells.equals(grid.cells) && this.layer == grid.layer;

    }
}
