package inf112.skeleton.game;

/**
 * CardType spesifi describes Card
 * <p>
 * * Consists of:
 * * backup: 6 kort (430 - 480)
 * * u-turn: 6 kort (10 - 60)
 * <p>
 * * rotate right: 18 kort (80-420, intervall 20
 * * rotate left: 18 kort (70-410, intervall 20)
 * <p>
 * * move 1: 18 kort (490 - 650, intervall 10)
 * * move 2: 12 kort (670 - 780, intervall 10)
 * * move 3: 6 kort (790 - 840, intervall 10)
 */
public enum CardType {
    UTURN(10, 60, 10),
    BACKUP(430, 480, 10),
    ROTRIGHT(80, 420, 20),
    ROTLEFT(70, 410, 20),
    MOVE1(490, 660, 10),
    MOVE2(670, 780, 10),
    MOVE3(790, 840, 10);


    public final int prStart;
    public final int prEnd;
    public final int interval;

    CardType(int prStart, int prEnd, int interval) {

        this.prStart = prStart;
        this.prEnd = prEnd;
        this.interval = interval;
    }

}
