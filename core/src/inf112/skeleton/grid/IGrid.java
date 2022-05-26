package inf112.skeleton.grid;


/**
 * Source is inf101 spring 20 semester oblig 2 solution
 */

public interface IGrid<T> extends Iterable<T> {

    /**
     * @return The height of the inf112.skeleton.grid.
     */
    int numRows();

    /**
     * @return The width of the inf112.skeleton.grid.
     */
    int numCols();

    int numLayers();

    /**
     * Set the contents of the cell in the given Location.
     *
     * @param loc     - The location we want to change.
     * @param element - The contents the cell is to have.
     */
    void set(Location loc, T element);

    /**
     * Get the contents of the cell in the given location.
     * The location must be within the inf112.skeleton.grid, otherwise an IndecOutOfBoundsException is thrown.
     *
     * @param loc - The Location we want to get.
     * @return The element stored in that location.
     */
    T get(Location loc);

    /**
     * Make a copy
     *
     * @return A shallow copy of the inf112.skeleton.grid, with the same elements
     */
    IGrid<T> copy();

    /**
     * Searches the inf112.skeleton.grid for an element
     *
     * @param target - element to find
     * @return the location at which that element is stored, if no such element is found null is returned.
     */
    Location locationOf(T target);

    /**
     * Returns an Iterable over all valid locations in this inf112.skeleton.grid
     *
     * @return
     */
    public Iterable<Location> locations();

    /**
     * Searches the inf112.skeleton.grid for an element
     *
     * @param obj - the element to search for
     * @return true if the element was found, false otherwise
     */
    public boolean contains(Object obj);

}