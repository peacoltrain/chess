package model;

import chess.ChessGame;

public class GameData {

    public final int gameID;
    String whiteUsername;
    String blackUsername;
    public final String gameName;
    public final chess.ChessGame game;

    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, chess.ChessGame game){
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public void setWhiteUsername(String username){
        this.whiteUsername = username;
    }
    public void setBlackUsername(String username){
        this.blackUsername = username;
    }

    public String getWhiteUsername(){
        return whiteUsername;
    }
    public String getBlackUsername(){
        return blackUsername;
    }
}