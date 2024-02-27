package service;

import dataAccess.DataAccessException;
import dataAccess.dataAccessAuth;
import dataAccess.dataAccessGame;
import model.AuthData;
import model.GameData;

import java.nio.file.DirectoryNotEmptyException;
import java.util.Collection;

public class GameService {

    public static Collection<GameData> GameListService(String authToken) throws DataAccessException {
        var exists = dataAccessAuth.getAuthFromToken(authToken);
        if(exists != null) {
            return dataAccessGame.getGameList();
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public static GameData CreateService(String authToken, GameData gameData) throws DataAccessException {
        var exists = dataAccessAuth.getAuthFromToken(authToken);
        if(exists != null) {
            return dataAccessGame.createNewGame(gameData.gameName);
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public static void JoinService(String authToken, String color, int gameID) throws DataAccessException {
        AuthData authData = dataAccessAuth.getAuthFromToken(authToken); //Throws exception if authToken doesn't exist
        GameData selectedGame = dataAccessGame.getGameFromID(gameID); //Throws exception if gameID doesn't exist
        if(color != null){
            dataAccessGame.addPlayer(selectedGame, authData.username(), color);
        }
    }

}
