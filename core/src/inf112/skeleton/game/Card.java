package inf112.skeleton.game;

/**
 * Cards consisting of Robot actions
 */
public class Card {
    /**
     * Priority nr decides order of actions
     * Card type is the type of action(forward 2, left rotation etc.)
     */
    public int priorityNr;
    public CardType type;

    public Card(CardType type, int priorityNr) {
        this.priorityNr = priorityNr;
        this.type = type;
    }

    @Override
    public String toString() {
        return type.name();
    }
}
