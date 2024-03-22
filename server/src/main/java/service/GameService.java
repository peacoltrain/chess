package service;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import java.util.Collection;

public class GameService {

    private final static DataAccess dataAccess;

    static {
        try {
            dataAccess = new SqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Collection<GameData> gameListService(String authToken) throws DataAccessException {
        dataAccess.getAuthFromToken(authToken);
        return dataAccess.getGameList();
    }

    public static GameData createService(String authToken, String gameName) throws DataAccessException {
        dataAccess.getAuthFromToken(authToken);
        return dataAccess.createNewGame(gameName);
    }

    public static void joinService(String authToken, String color, int gameID) throws DataAccessException {
        AuthData authData = dataAccess.getAuthFromToken(authToken); //Throws exception if authToken doesn't exist
        GameData selectedGame = dataAccess.getGameFromID(gameID); //Throws exception if gameID doesn't exist
        if(color != null){
            dataAccess.addPlayer(selectedGame, authData.username(), color);
        }
    }

}
