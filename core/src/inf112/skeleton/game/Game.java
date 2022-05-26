package inf112.skeleton.game;


import inf112.skeleton.grid.*;

import java.util.*;

//import static sun.swing.MenuItemLayoutHelper.max;

/**
 * Starts the backend side of the game
 */
public class Game {
    private boolean gameActive;
    public RoundHandler rh;
    protected final List<Flag> flags;
    protected final List<Player> players;
    protected final HashSet<Player> deadPlayers;
    private boolean mocMode = true;


    public Game(boolean gameActive, GameBoard gameBoard, List<Flag> flags, List<Player> players) {
        this.flags = flags;
        this.gameActive = gameActive;
        this.players = players;
        this.deadPlayers = new HashSet<>();
        this.rh = new RoundHandler(gameBoard);
    }

    /**
     * A random setup used for testing game logic
     */

    public void randomSetUp(List<Player> players, List<Flag> flags, GameBoard gb) {
        if (flags.isEmpty() || players.isEmpty()) {
            throw new NullPointerException("Need players and flags to setup");
        }

        int i = 1;
        for (Player p : players) {
            p.setRobot(new Robo("robot_" + i));
            p.placeRobotAtSpawn(gb);
            i++;
        }

        int rows = gb.getRows();
        int cols = gb.getCols();
        List<Integer> xCoordinates = new ArrayList<>();
        for (int j = 1; j < cols - 1; j++) {
            xCoordinates.add(j);
        }
        Collections.shuffle(xCoordinates);
        int yFlag = rows - 1;
        int layerFlag = 2;
        i = 1;
        for (Flag f : flags) {
            gb.set(new Location(xCoordinates.get(i), yFlag, layerFlag), f);
            i++;
        }
    }

    //Q: hva er det minimum man trenger for å kjøre et spill (mvp)
    //A: GameBoard, Robot, Player, Flag,

    public void startGame(boolean gameActive) {

       //while (gameActive) {
            mocPrint("Would you like start a new Round? y/n");
            Scanner answer = new Scanner(System.in);
            if (!answer.hasNext() || !answer.next().toLowerCase().startsWith("y")) {
                mocPrint("End the game !\n God Bay!");
                gameActive = false;
                return;
            } else {
                for(Player player: players)
                    mocPrint(player.getRobot() + " has : " + player.getHand());
                mocPrint("Dealing the programing cards:");
                rh.dealProgramCards(players);

                //Let players register cards
                for (Player player : players) {
                    mocPrint(player.getRobot() + " get: " + player.getHand());
                    if (player.isPowerDown()) {
                        mocPrint(player.getRobot() + " is Power Down");
                        continue;
                    }

                    rh.chooseCardsManager(player);
                    mocPrint(player.getRobot() + " has chosen: " + player.getChosenCards());
                    //Let players choose whether to continue running or power down

                }
                mocPrint("Moving the robots");
                rh.performMovements(players);

                rh.touchCheckpoints(flags, players);
                rh.cleanUP(players);
                rh.collectCards(players);
                timeToPowerDown();
                isRobotDead();
                isGameOver();
            }
       // }
        return;

    }

    /**
     * temporary method
     */
    private void timeToPowerDown() {
        for (Player p : players) {
            if (p.getRobot().getHealth() < 3) {
                p.announcePowerDown(rh.gameBoard);
            }
        }
    }

    public void isGameOver() {
        for (Player p : players) {
            if (p.getNextFlagIndex() > flags.size()) {
                gameActive = false;
                mocPrint("Game over, winner is: " + p.getRobot());
            }
        }
    }

    public void isRobotDead() {
        for (Player player : players) {
            if (player.getRobot().getHealth() < 1) {
                player.decreaseLife();
                if (player.getLife() < 1) {
                    deadPlayers.add(player);
                    players.remove(player);
                    if (players.isEmpty()) {
                        gameActive = false;
                    }
                }
                player.getRobot().resetHealth();
                player.placeRobotAtSpawn(rh.gameBoard);
            }
        }
    }

    /**
     * If debugmode is true:
     * Allows Printing in methods
     */
    protected void mocPrint(String debugString) {
        if (mocMode) {
            System.out.println(debugString);
        }
    }

    public List<Player> getPlayers() {
        return players;
    }
    public List<Flag> getFlags() {
        return flags;
    }
}