package dataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import java.util.Random;
import java.util.*;

public class dataAccessGame {

    public static Set<GameData> myGameData = new HashSet<GameData>();

    public static void clearGameData(){
        myGameData.clear();
    }

    public static GameData createNewGame(String gameName){
        Random random = new Random();
        myGameData.add(new GameData(random.nextInt(900000) + 100000, null, null, gameName, new ChessGame()));
        for(GameData game: myGameData){
            if(game.gameName.equals(gameName)){
                return game;
            }
        }
        return null;
    }

    public static Collection<GameData> getGameList() {
        return myGameData;
    }

    public static GameData getGameFromID(int gameID){
        for(GameData game: myGameData){
            if(game.gameID == gameID){
                return game;
            }
        }
        return null;
    }

    public static void addPlayer(int gameID, String color, String username) {
        for(GameData game: myGameData){
            if(game.gameID == gameID){
                if(color.equals("white")){ game.setWhiteUsername(username);}
                if(color.equals("black")){ game.setBlackUsername(username);}
            }
        }
    }
}
