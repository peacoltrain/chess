package model;

import chess.ChessGame;

import java.nio.file.DirectoryNotEmptyException;
import java.util.zip.DataFormatException;

public class GameData {

    public final int gameID;
    public String whiteUsername;
    public String blackUsername;
    public final String gameName;
    public final chess.ChessGame game;

    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, chess.ChessGame game){
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public void setWhiteUsername(String username) throws DirectoryNotEmptyException {
        if(this.whiteUsername == null){
            this.whiteUsername = username;
            return;
        }
        throw new DirectoryNotEmptyException("Error: already taken");
    }
    public void setBlackUsername(String username) throws DirectoryNotEmptyException{
        if(this.blackUsername == null){
            this.blackUsername = username;
            return;
        }
        throw new DirectoryNotEmptyException("Error: already taken");
    }
}