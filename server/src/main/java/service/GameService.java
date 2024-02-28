package service;

import dataAccess.DataAccessException;
import dataAccess.DataAccessAuth;
import dataAccess.DataAccessGame;
import model.AuthData;
import model.GameData;
import java.util.Collection;

public class GameService {

    public static Collection<GameData> gameListService(String authToken) throws DataAccessException {
        DataAccessAuth.getAuthFromToken(authToken);
        return DataAccessGame.getGameList();
    }

    public static GameData createService(String authToken, GameData gameData) throws DataAccessException {
        DataAccessAuth.getAuthFromToken(authToken);
        return DataAccessGame.createNewGame(gameData.gameName);
    }

    public static void joinService(String authToken, String color, int gameID) throws DataAccessException {
        AuthData authData = DataAccessAuth.getAuthFromToken(authToken); //Throws exception if authToken doesn't exist
        GameData selectedGame = DataAccessGame.getGameFromID(gameID); //Throws exception if gameID doesn't exist
        if(color != null){
            DataAccessGame.addPlayer(selectedGame, authData.username(), color);
        }
    }

}
