//package inf112.skeleton.multiplayer;
//
//
//import inf112.skeleton.game.Card;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class Packets {
//
//    public Packets() {
//    }
//
//    /**
//     * Sent by host to all the players
//     * Which map it is
//     * What playerNumber the client receiving is
//     * How many players there are in total this game
//     *
//     */
//    public JSONObject initializeGameJSon(int mapSelection, int playerNumber, int playerAmount) throws JSONException {
//        JSONObject initializeGame = new JSONObject();
//        initializeGame.put("type", "initializeGame");
//        initializeGame.put("mapSelection", mapSelection);
//        initializeGame.put("playerNumber", playerNumber);
//        initializeGame.put("playerAmount", playerAmount);
//        return initializeGame;
//    }
//
//    /**
//     * Host sends a client a hand; a list of cards
//     *
//     */
//    public JSONObject playerHandJSON(ArrayList<Card> cards) throws JSONException {
//        JSONObject playerHand = new JSONObject();
//        playerHand.put("type", playerHand);
//        playerHand.put("playerHand", cards);
//        return playerHand;
//    }
//
//    /**
//     * Client requests a hand from host
//     *
//     */
//    public JSONObject handRequestJSON(int playerNumber) throws JSONException {
//        JSONObject handRequest = new JSONObject();
//        handRequest.put("type", "handRequest");
//        handRequest.put("playerNumber", playerNumber);
//        return handRequest;
//    }
//
//    /**
//     * This should return a hashset of the cards all the players
//     * And another hashset with the intent to power down boolean of all players
//     * but only if all players have registered their cards or powered down (I think?)
//     *
//     */
//    public JSONObject allPlayersActionsJson(HashMap<Integer, Boolean> intentToPowerDown,
//                                       HashMap<Integer, ArrayList<Card>> playerCards) throws JSONException {
//        JSONObject roundAction = new JSONObject();
//        roundAction.put("type", "handRequest");
//        roundAction.put("playerPowerDown", intentToPowerDown);
//        roundAction.put("playerCards", playerCards);
//        return roundAction;
//    }
//
//    /**
//     * When a player has chosen their cards to play, or announced their intent to power down, this should be used
//     * It is sent to the host where it adds the list of cards to a hashset to the corresponding playerNumber
//     * This should only be sent if the cards selected are not breaking game rules (checked client side)
//     * or if the player chooses to power down
//     *
//     */
//    public JSONObject playerActionJson(int playerNumber, boolean intentToPowerDown,
//                                       ArrayList<Card> playerCards) throws JSONException {
//        JSONObject playerAction = new JSONObject();
//        playerAction.put("type", "playerAction");
//        playerAction.put("playerNumber", playerNumber);
//        playerAction.put("playerPowerDown", intentToPowerDown);
//        playerAction.put("playerCards", playerCards);
//        return playerAction;
//    }
//
//}