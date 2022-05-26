package inf112.skeleton.app;

public class GameInit {

    private final MapBuilder mapBuilder;

    public GameInit(String map_filename, int playerLimit) {
        mapBuilder = new MapBuilder(map_filename, playerLimit);


    }

    public MapBuilder getMapBuilder() {
        return this.mapBuilder;
    }
}
