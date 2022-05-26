package inf112.skeleton.grid;

import java.util.Arrays;
import java.util.List;

/**
 * Source is inf101 spring 20 semester oblig 2 solution
 */

public enum Directions {
    NORTH(0, 1, 0),
    EAST(1, 0, 1),
    SOUTH(0, -1, 2),
    WEST(-1, 0, 3);

    /**
     * The four cardinal directions: {@link #NORTH}, {@link #SOUTH}, {@link #EAST},
     * {@link #WEST}.
     */
    public static final List<Directions> FOUR_DIRECTIONS = Arrays.asList(EAST, NORTH, WEST, SOUTH);

    private final int dx;
    private final int dy;
    private final int dir;

    private Directions(int dx, int dy, int dir) {
        this.dx = dx;
        this.dy = dy;
        this.dir = dir;
    }


    /**
     * @return The change to your X-coordinate if you were to move one step in this
     * direction
     */
    public int getDx() {
        return dx;
    }

    /**
     * @return The change to your Y-coordinate if you were to move one step in this
     * direction
     */
    public int getDy() {
        return dy;
    }

    /**
     * @return The value of the direction the robot is facing
     */
    public int getDir() {
        return dir;
    }

    /**
     * Rotates direction 90 degrees clockwise for every int amountToRotate
     *
     * @param amountToRotate
     * @return
     */
    public Directions rotate(int amountToRotate) {
        int newDirection = (dir + amountToRotate) % 4;
        switch (newDirection) {
            case 1:
                return EAST;
            case 2:
                return SOUTH;
            case 3:
                return WEST;
            default:
                return NORTH;
        }
    }
}
