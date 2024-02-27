package service;

import dataAccess.dataAccessAuth;
import dataAccess.dataAccessGame;
import model.GameData;

import java.util.Collection;

public class GameService {

    public static Collection<GameData> GameListService(String authToken) {
        var exists = dataAccessAuth.getAuthFromToken(authToken);
        if(exists != null) {
            return dataAccessGame.getGameList();
        }
        return null;
    }

    public static GameData CreateService(String authToken, String gameName) {
        var exists = dataAccessAuth.getAuthFromToken(authToken);
        if(exists != null) {
            return dataAccessGame.createNewGame(gameName);
        }
        return null;
    }

    public static void JoinService(String authToken, String color, int gameID) {
        var exists = dataAccessGame.getGameFromID(gameID);
        if(exists != null){
            if(color != null){
                if(color.equals("white")){
                    if(exists.getWhiteUsername() == null){
                        exists.setWhiteUsername(dataAccessAuth.getAuthFromToken(authToken).username());
                    }
                }
                else{
                    if (exists.getBlackUsername() == null) {
                        exists.setBlackUsername((dataAccessAuth.getAuthFromToken(authToken).username()));
                    }
                }
            }
        }
    }

}
