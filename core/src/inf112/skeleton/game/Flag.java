package inf112.skeleton.game;

/**
 * Flags are checkpoints.
 * The goal is to reach every flag in a specific order
 */
public class Flag implements ITileObject {
    /**
     * Flag index is flag place in order.
     */
    private final int flagIndex;

    public Flag(int flagindex) { //Start with the first flag having flagIndex = 1
        this.flagIndex = flagindex;
    }


    public boolean playerHasVisited(Player player) {
        return (player.getNextFlagIndex() > flagIndex);
    }


    public int getIndex() {
        return this.flagIndex;
    }

    @Override
    public String toString() {
        return "flag index " + flagIndex;
    }

}
