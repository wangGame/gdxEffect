package inf112.skeleton.grid;

import inf112.skeleton.game.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * An extension of the Grid class
 * Gives increased functionality, mainly in the form of "layered" grids, such that it can represent a game board
 * The class holds a list of Grids
 * TODO write documents to all methods
 */

public class GameBoard {
    private List<Grid<ITileObject>> grids;
    private int rows;
    private int cols;
    private int layers;
    private HashMap<IRobot, Location> robotsOnBoard;
    private HashMap<ITileObject, Location> objectOnBoard;
    private final boolean debugMode = true;

    /**
     * Constructor
     * Constructs Board with given number rows, columns and layers.
     */
    public GameBoard(int rows, int cols, int layers) {
        this.rows = rows;
        this.cols = cols;
        grids = new ArrayList<>(layers);
        for (int i = 0; i < layers; i++) {
            ITileObject empty = new EmptyTile();
            Grid<ITileObject> tempGrid = new Grid<>(rows, cols, empty, i);
            grids.add(tempGrid);

        }
        this.layers = layers;
        robotsOnBoard = new HashMap<>();
        objectOnBoard = new HashMap<>();
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    /**
     * Returns amount of layers in GameBoard
     */
    public int getLayers() {
        return layers;
    }

    public Grid<ITileObject> getGridLayer(int layer) {
        return grids.get(layer);
    }

    /**
     * Return first layer of GameBoard
     */
    private Grid<ITileObject> getReferenceLayer() {
        return grids.get(0);
    }


    /**
     * Return location of Target
     */
    public Location locationOf(ITileObject target) {
        Location loc;
        if (target instanceof Robo) {
            loc = robotsOnBoard.get(target);
        } else {
            loc = objectOnBoard.get(target);
        }

        if (loc == null) {
            for (int i = 0; i < getLayers(); i++) {
                loc = getGridLayer(i).locationOf(target);
                if (loc != null) {
                    return loc;
                }
            }
        }
        return loc;
    }

    /**
     * Checks if obj is in any layer of gameBoard
     */
    public boolean contains(ITileObject obj) {

        for (int i = 0; i < getLayers(); i++) {
            if (getGridLayer(i).contains(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets location of obj in grid,
     * and stores its location
     */
    public void set(Location loc, ITileObject tile) {
        if (tile instanceof Robo) {
            getGridLayer(loc.getLayer()).set(loc, tile);
            robotsOnBoard.put((IRobot) tile, loc);
        } else {
            getGridLayer(loc.getLayer()).set(loc, tile);
            objectOnBoard.put(tile, loc);
        }
    }

    /**
     * Return object in exact loc (by layer)
     */
    public ITileObject get(Location loc) {
        return getGridLayer(loc.getLayer()).get(loc);
    }


    /**
     * Replaces loc with empty Tile.(by layer)
     */
    public void clearLocation(Location loc) {
        set(loc, new EmptyTile());
    }

    /**
     * Checks if obj1 and obj has same coordinates.
     */
    public boolean sameXYLocation(ITileObject obj1, ITileObject obj2) {
        Location loc1 = locationOf(obj1);
        Location loc2 = locationOf(obj2);
        return loc1.sameRowCol(loc2);
    }

    /**
     * Return list of all obj in coordinates
     */
    public List<ITileObject> getXYObjects(Location loc) {
        List<ITileObject> returnList = new ArrayList<>(layers);
        for (Grid<ITileObject> grid : grids) {
            returnList.add(grid.get(loc));
        }

        return returnList;
    }


    /**
     * Moves Robot by direction
     */
    public void moveRobot(Directions dir, IRobot robot) {
        Location currLoc = robotsOnBoard.get(robot);
        Location endLoc = currLoc.move(dir);

        if (!validCoordinate(endLoc)) {
            robot.addDamage(1);
            debugPrint("Robo: " + dir + " Out of bounds. " + endLoc.toString() + "| Added 1 dmg");
            //TODO maxDmg
        } else if (robotCanGo(robot, currLoc, dir)) {
            set(endLoc, robot);
            clearLocation(currLoc);
            debugPrint("Moved bot from " + currLoc.toString() + " to " + endLoc.toString());
        } else {
            debugPrint(robot.getName() + "can't move to " + endLoc.toString());
        }
    }


    /**
     * Checks if robot can go
     * NP: Not checking for valid Coordinate.
     */
    public boolean robotCanGo(IRobot robotOnMove, Location currLoc, Directions dir) {
        Location endLoc = currLoc.move(dir);
        return wallCheck(currLoc, dir) && otherBotCheck(robotOnMove, endLoc, dir);

    }

    /**
     * Checks for bot on tile
     * moves other robot
     */
    private boolean otherBotCheck(IRobot robot1, Location robot2loc, Directions dir) {
        if (validCoordinate(robot2loc) && get(robot2loc) instanceof Robo) {
            IRobot placidRobot = (IRobot) get(robot2loc);
            if (robotCanGo(placidRobot, robot2loc, dir)) {
                moveRobot(dir, placidRobot);
                debugPrint(robot1.getName() + " pushes " + placidRobot.getName());
                return true;
            } else return false;
        }
        return true;
    }


    /**
     * Checks if location is inside grid
     */
    public boolean validCoordinate(Location loc) {
        boolean validLayer = Math.max(-1, loc.getLayer()) == Math.min(loc.getLayer(), layers);
        if (!validLayer)
            throw new IllegalArgumentException("location should has valid layer between -1 and " + layers);

        return getReferenceLayer().validCoordinate(loc.getCol(), loc.getRow());
    }

    /**
     * Checks if there is a wall using a location and direction
     *
     * @return true if wall is in way
     */
    public boolean wallCheck(Location loc, Directions dir) {
        Location loc2 = loc.move(dir);
        List<ITileObject> XYObjects = getXYObjects(loc2);
        for (Object obj : XYObjects) {
            if (obj instanceof Barricade) {
                if (((Barricade) obj).isBlocking(dir.rotate(2))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Used by wall to either place a Barricade or update an existing Barricade
     *
     */
    private void placeBarricade(Location loc, Directions dir) {
        if (get(loc) instanceof Barricade) {
            Barricade b = (Barricade) get(loc);
            b.addBarricade(dir);
            set(loc, b);
        } else {
            set(loc, new Barricade(loc, dir));
        }
    }

    /**
     * Places a wall between two locations
     * A "wall" is composed of two Barricade objects, with each Barricade having at least one direction
     *
     */
    public void wall(Location loc, Directions dir) {
        placeBarricade(loc, dir);
        Location loc2 = loc.move(dir);
        Directions dir2 = dir.rotate(2);
        placeBarricade(loc2, dir2);
    }


    /**
     * Creates hard copy of GameBoard
     */
    public GameBoard copy() {
        Grid<ITileObject> tempGrid = getReferenceLayer();
        GameBoard gameBoardCopy = new GameBoard(tempGrid.numRows(), tempGrid.numCols(), getLayers());
        for (int i = 0; i < getLayers(); i++) {
            gameBoardCopy.grids.set(i, (Grid<ITileObject>) getGridLayer(i).copy());
        }
        return gameBoardCopy;
    }

    /**
     * If debugmode is true:
     * Allows Printing in methods
     */
    public void debugPrint(String debugString) {
        if (debugMode) {
            System.out.println(debugString);
        }
    }

}