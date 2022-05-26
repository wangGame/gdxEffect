package inf112.skeleton.game;

import inf112.skeleton.grid.GameBoard;

import java.util.*;

public class RoundHandler {
    public Deck deck;
    public GameBoard gameBoard;
    private boolean debugMode = true;
    private HashMap<Player, List<Card>> receivedCards;


    public RoundHandler(GameBoard gameboard) {
        this.gameBoard = gameboard;
        deck = new Deck();
        receivedCards = new HashMap<>();
    }


    public int DetermineTheNumberOfCards(Player player) {
        if (player.isPowerDown()) {
            debugPrint("0 cards for " + player.getRobot());

            return 0;
        }
        debugPrint(player.getRobot().getHealth() + " cards for player" + player.getRobot());
        return player.getRobot().getHealth();
    }

    public void dealProgramCards(List<Player> players) {

        Collections.shuffle(deck.cardDeck);

        for (Player player : players) {

            HashSet<Card> hand = new HashSet<>();
            int numOfCards = DetermineTheNumberOfCards(player);

            while (numOfCards > 0) {
                if (deck.cardDeck.size() == 0)
                    throw new IllegalStateException(" Deck should have cards for every new round");
                hand.add(deck.cardDeck.remove(0));
                numOfCards--;
            }
            player.setHand(hand);

        }
    }

    public void receiveCards(Player player, List<Card> sendCards) {
        receivedCards.put(player, sendCards);

    }

    /**
     * Manages the players selection of cards
     */
    public void chooseCardsManager(Player player) { //Må kalles før spiller velger noen kort for den runden
        //player must have hand here
        if (player.getHand().isEmpty()) {
            throw new NoSuchElementException("The player has no cards in their hand");
        }

        if (!receivedCards.isEmpty()) {
            while (player.allowedToChooseCards() && receivedCards.get(player).size() != 0) {
                Card card = receivedCards.get(player).remove(0);
                addChosenCard(player, card);
            }
        }

        while (player.allowedToChooseCards()) {
            addChosenCard(player, chooseRandomCard(player));
        }
    }

    private void addChosenCard(Player player, Card card) {

        if (!player.allowedToChooseCards()) {
            return;
        }
        int place = player.getChosenCards().size();
        player.addChosenCard(card, place);


    }

    /**
     * This method should be called when a player has no more time
     * to choose programming cards, or if player has chosen less than allowed number of cards
     */
    public Card chooseRandomCard(Player player) {
        //TODO show the random chosen cards in the game screen
        Random r = new Random();
        int bound = player.getHand().size();
        List<Card> toChoose = new ArrayList<>(player.getHand());
        return toChoose.remove(r.nextInt(bound));

    }

    /**
     * Perform  actions in 5 phases according to programing cards
     */
    public void performMovements(List<Player> players) {
        List<Player> activePlayer = new ArrayList<>();
        for (Player p : players) {
            if (p.getLife() > 0 && p.getChosenCards().size() == 5)
                activePlayer.add(p);
        }
        int phase = 0;
        while (phase < 5) {
            performOneCardMovement(phase, activePlayer);
            phase++;
        }
    }

    public void performOneCardMovement(int phase, List<Player> players) {

        PriorityQueue<Player> prioritetPlayers =
                new PriorityQueue<>((p1, p2) -> p2.getChosenCards().get(phase).priorityNr - p1.getChosenCards().get(phase).priorityNr);

        prioritetPlayers.addAll(players);

        while (!prioritetPlayers.isEmpty()) {
            Player p = prioritetPlayers.poll();
            Card nextCard = p.getChosenCards().get(phase);
            p.makeMove(nextCard, gameBoard);

        }
    }

    /**
     * Check if robot has touched flag or repair sites
     */
    public void touchCheckpoints(List<Flag> flags, List<Player> players) {
        checkWrenchSpace();
        flagCheck(flags, players);
    }

    private void checkWrenchSpace() {

    }

    /**
     * Check if any player has visited an actual next flag
     */
    private void flagCheck(List<Flag> flags, List<Player> players) {
        for (Player player : players) {
            debugPrint("Checking if " + player.getRobot() + " has touched any flag!");
            for (Flag flag : flags) {

                if (gameBoard.sameXYLocation(player.getRobot(), flag)) {
                    player.checkFlagIndex(flag);
                    if (flag.playerHasVisited(player)) {
                        player.newCheckPoint(gameBoard.locationOf(flag));
                        debugPrint("Yes!! " + player.getRobot() + " har visited flag " + flag);
                    } else {
                        debugPrint("No, " + player.getRobot() + " has not reached any flag yet!");
                    }
                }
            }
        }

    }

    /**
     * Repairs robot on wrenchSpace and update the current cards for each player
     */
    public void cleanUP(List<Player> players) {
        debugPrint("Cleaning Up!");
        updateRobotsSpawnPoint(players);
        cleanOrLockeCards(players);

    }

    private void updateRobotsSpawnPoint(List<Player> players) {
        for (Player p : players) {
            if (p.isPowerDown()) {
                p.cancelPowerDown();
                debugPrint(p.getRobot() + " Canceled Power Down! ");
            }
        }
    }

    private void cleanOrLockeCards(List<Player> players) {
        for (Player p : players) {
            p.updateCurrentCards();
            debugPrint(p.getRobot() + " har  " + p.getNumberOfDamages() + " damages And " + p.getLife() + " life");
            debugPrint(p.getRobot() + " has\n " + p.getChosenCards() + "\n to the next round");
        }
    }


    public void collectCards(List<Player> players) {
        debugPrint("collecting the cards!");
        for (Player p : players) {

            deck.addRestCards(p.getHand());
            deck.addRestCards(p.getRestCards());

            p.getHand().clear();
        }


    }

    /**
     * If debugmode is true:
     * Allows Printing in methods
     */
    private void debugPrint(String debugString) {
        if (debugMode) {
            System.out.println(debugString);
        }
    }
}
