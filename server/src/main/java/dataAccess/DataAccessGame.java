package dataAccess;
import chess.ChessGame;
import model.GameData;
import java.nio.file.DirectoryNotEmptyException;
import java.util.Random;
import java.util.*;

public class DataAccessGame {

    public static Set<GameData> myGameData = new HashSet<GameData>();

    public static void clearGameData(){
        myGameData.clear();
    }
    public static GameData createNewGame(String gameName){
        Random random = new Random();
        GameData myNewGame = new GameData(random.nextInt(900000) + 100000, null, null, gameName, new ChessGame());
        myGameData.add(myNewGame);
        return myNewGame;
    }
    public static Collection<GameData> getGameList() {
        return myGameData;
    }
    public static GameData getGameFromID(int gameID) throws DataAccessException {
        for (GameData game : myGameData) {
            if (game.gameID == gameID) {
                return game;
            }
        }
        throw new DataAccessException("Error: bad request");
    }
    public static void addPlayer(GameData game, String username, String color) throws DataAccessException {
        if(color.equals("WHITE") || color.equals("white")){
            try{
                game.setWhiteUsername(username);
            } catch (DirectoryNotEmptyException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
        else{
            try{
                game.setBlackUsername(username);
            } catch (DirectoryNotEmptyException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
    }
}
