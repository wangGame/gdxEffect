package inf112.skeleton.game;


import inf112.skeleton.grid.Directions;


/**
 * Robot is a TileObject moved player on the board
 */
public class Robo implements IRobot {
    /**
     * Has 9 health. Loss life if 0
     * Has 3 lives
     * Has direction which can change
     * Has a name
     */
    private int health;
    private int life;
    private Directions direction;
    private String name;

    public Robo() {
        this.health = 9;
        this.life = 3;
        this.direction = Directions.NORTH;
        this.name = "Default name";
    }

    public Robo(String name) {
        this.health = 9;
        this.life = 3;
        this.direction = Directions.NORTH;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void resetHealth() {
        this.health = 9;
    }

    public int getHealth() {
        return health;
    }

    public int getLife() {
        return this.life;
    }

    public void addDamage(int damage) {
        if (health == 0) {
            decreaseLife();
            resetHealth();
        } else
            health -= damage;
    }

    public void decreaseLife() {
        if (life > 0) this.life -= 1;
    }

    public Directions getDirection() {
        return this.direction;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

    /**
     * Rotates robot
     *
     * @param amountToRotate Rotate 90 dg right = + 1
     *                       Rotate 90 dg left = - 1
     *                       Backwards = +2
     */
    public void rotate(int amountToRotate) {
        this.direction = direction.rotate(amountToRotate);
    }

    @Override
    public String toString() {
        return this.name;
    }
}