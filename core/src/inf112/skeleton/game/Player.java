package inf112.skeleton.game;


import inf112.skeleton.app.MapBuilder;
import inf112.skeleton.grid.Directions;
import inf112.skeleton.grid.GameBoard;
import inf112.skeleton.grid.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Player is the usercontrol of the game
 */
public class Player {
    /**
     * Player has a robot
     * Player can power down
     * Player has cards
     * Player can choose cards
     */
    private Location robotSpawnPoint;
    private Robo myRobo;
    private int nextFlagIndex = 1;
    private boolean powerDown = false;
    private List<Card> hand = new ArrayList<>();
    private HashMap<Integer, Card> chosenCards = new HashMap<>();
    private List<Card> restCards = new ArrayList<>();

    public Player(Location startPosition) {
        this.robotSpawnPoint = startPosition;
        this.myRobo = new Robo();
    }

    public void setRobot(Robo myRobo) {
        this.myRobo = myRobo;
    }


    public Robo getRobot() {
        return myRobo;
    }


    public void placeRobotAtSpawn(GameBoard gb) {
        if (gb.contains(myRobo)) {
            Location loc = gb.locationOf(myRobo);
            gb.clearLocation(loc);
        }
        gb.set(robotSpawnPoint, myRobo);
    }


    public Location getSpawnPoint() {
        return this.robotSpawnPoint;
    }

    public void newCheckPoint(Location location) {
        this.robotSpawnPoint = location;
    }


    public int getLife() {
        return myRobo.getLife();
    }

    public void addDamage(int d) {
        if (myRobo.getHealth() == 0) {
            decreaseLife();
            myRobo.resetHealth();
        } else myRobo.addDamage(d);
    }

    public void decreaseLife() {
        myRobo.decreaseLife();
    }

    public int getNumberOfDamages() {
        return 9 - myRobo.getHealth();
    }


    public int getNextFlagIndex() {
        return nextFlagIndex;
    }

    /**
     * Checks if flag has correct index.
     */
    public void checkFlagIndex(Flag flag) {
        if (flag.getIndex() == nextFlagIndex) {
            this.nextFlagIndex++;
        }
    }

    public boolean isPowerDown() {
        return powerDown;
    }

    public void announcePowerDown(GameBoard gameBoard) {
        myRobo.resetHealth();
        placeRobotAtSpawn(gameBoard);
        chosenCards.clear();
        //TODO reset direction to NORTH after placing back in spawn.
        powerDown = true;
        System.out.println(myRobo + " announced Power Down for next round !");
    }

    public void cancelPowerDown() {
        restCards.addAll(chosenCards.values());
        powerDown = false;
    }


    /**
     * Gives player cards to choose from.
     */
    public void setHand(HashSet<Card> cards) {
        hand.clear();
        hand.addAll(cards);
    }

    public List<Card> getHand() {

        //if(hand.isEmpty()) throw new IllegalCallerException("Player has no card in his hand ");
        return hand;
    }

    public HashMap<Integer, Card> getChosenCards() {
        return chosenCards;
    }

    public void addChosenCard(Card card, int place) {
        chosenCards.put(place, card);
    }

    /**
     * Checks how many cards the player should get given current life.
     */
    public int cardChoiceAmount() {
        int health = getRobot().getHealth();
        return Math.min(5, health);
    }

    /**
     * This method should check two conditions, the number of current chosen cards
     * and if the player still has time to choose
     *
     * @return true if player has time and place in chosenCards to choose, false otherwise
     */
    public boolean allowedToChooseCards() {
        // Should check if player still has time to choose
        // Should check if getChosenCards().size() < 5
        // in case there is some locked cards, the chosenCards is not empty
        //at the start of this round and this statement " getChosenCards().size() < cardChoiceAmount()"
        // will not do well in this case.
        return getChosenCards().size() < cardChoiceAmount();
    }


    /**
     * Move the robot out from a card, moving occurs on the GameBoard if the card is a moving card,
     * Otherwise change the robot direction
     *
     * @param movingCard the programming card
     * @param gb         GameBoard object where the moving is occurring
     */
    public void makeMove(Card movingCard, GameBoard gb) {
        Directions currentDir = myRobo.getDirection();
        switch (movingCard.type) {
            case MOVE1:
                gb.moveRobot(currentDir, myRobo);
                break;
            case MOVE2:
                gb.moveRobot(currentDir, myRobo);
                gb.moveRobot(currentDir, myRobo);
                break;
            case MOVE3:
                gb.moveRobot(currentDir, myRobo);
                gb.moveRobot(currentDir, myRobo);
                gb.moveRobot(currentDir, myRobo);
                break;
            case BACKUP:
                gb.moveRobot(currentDir.rotate(2), myRobo);
                break;
            case ROTLEFT:
                myRobo.rotate(-1);
                break;
            case ROTRIGHT:
                myRobo.rotate(+1);
                break;
            case UTURN:
                myRobo.rotate(2);
                break;
            default:
                throw new IllegalArgumentException(movingCard + " is not a valid card");

        }

    }

    /**
     * Adds cards according to rules.(dmg taken etc.)
     */
    public void updateCurrentCards() {
        restCards.clear();
        if (myRobo.getHealth() >= 5) {
            restCards.addAll(chosenCards.values());
            chosenCards.clear();
        } else {

            int freeCards = myRobo.getHealth() - 1;
            while (freeCards >= 0) {

                restCards.add(chosenCards.remove(freeCards));
                freeCards--;

            }
        }
    }

    public List<Card> getRestCards() {
        return restCards;
    }
}
