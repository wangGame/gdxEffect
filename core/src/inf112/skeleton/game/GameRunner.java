package inf112.skeleton.game;

import inf112.skeleton.grid.GameBoard;
import inf112.skeleton.grid.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * A gamesimulation from the backend side
 */
public class GameRunner {
    static RoundHandler rh;
    static Player player1;
    static Player player2;
    static Player player3;
    static List<Player> players;
    static List<Flag> flags;
    static GameBoard gb;
    static int rows, cols;
    static Game game;


    public static void setUp() {
        rows = 50;
        cols = 50;
        gb = new GameBoard(rows, cols, 5);

        player1 = new Player(new Location(2, 0, 2));
        player2 = new Player(new Location(20, 0, 2));
        player3 = new Player(new Location(48, 0, 2));

        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        players.add(player3);
        Flag flag1 = new Flag(1);
        Flag flag2 = new Flag(2);
        flags = new ArrayList<>();
        flags.add(flag1);
        flags.add(flag2);
        game = new Game(true, gb, flags, players);

        game.randomSetUp(players, flags, gb);


//        player1.setRobot(new Robot("robot1"));
//        player2.setRobot(new Robot("robot2"));
//        player3.setRobot(new Robot("robot3"));
//
//
//        for (Player p : players) p.placeRobotAtSpawn(gb);
//
//
//        gb.set(new Location(5, 5, 1), flag1);
//        gb.set(new Location(10, 5, 1), flag2);
//       // rh = new RoundHandler(gb);
    }

    public static void main(String[] args) {
        setUp();
        Game game = new Game(true, gb, flags, players);
        game.startGame(true);

    }
}
