package inf112.skeleton.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Deck of Cards
 * <p>
 * Consists of:
 * backup: 6 kort (430 - 480)
 * u-turn: 6 kort (10 - 60)
 * rotate right: 18 kort (80-420, intervall 20
 * rotate left: 18 kort (70-410, intervall 20)
 * move 1: 18 kort (490 - 650, intervall 10)
 * move 2: 12 kort (670 - 780, intervall 10)
 * move 3: 6 kort (790 - 840, intervall 10)
 */

public class Deck {
    public List<Card> cardDeck;

    public Deck() {
        cardDeck = new ArrayList<>(84);
        makeGenericCard(CardType.BACKUP, 430, 480, 10);
        makeGenericCard(CardType.UTURN, 10, 60, 10);

        makeGenericCard(CardType.ROTRIGHT, 80, 420, 20);
        makeGenericCard(CardType.ROTLEFT, 70, 410, 20);

        makeGenericCard(CardType.MOVE1, 490, 660, 10);
        makeGenericCard(CardType.MOVE2, 670, 780, 10);
        makeGenericCard(CardType.MOVE3, 790, 840, 10);
    }

    /**
     * Method for creating all types of cards.
     */
    private void makeGenericCard(CardType cardType, int prStart, int prEnd, int prInterval) {
        for (int pr = prStart; pr <= prEnd; pr += prInterval) {
            Card myGenericCard = new Card(cardType, pr);
            cardDeck.add(myGenericCard);

        }
    }

    public void addRestCards(List<Card> cards) {
        for (Card c : cards) {
            if (cardDeck.size() < 84)
                cardDeck.add(c);
        }
    }
}
