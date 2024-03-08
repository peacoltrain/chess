package dataAccess;
import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.nio.file.DirectoryNotEmptyException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DataAccessMem implements DataAccess{

    public static Set<AuthData> myAuthData = new HashSet<>();
    public static Set<GameData> myGameData = new HashSet<GameData>();
    public static Set<UserData> myUserData = new HashSet<>();
    public void clearAuth() {
        myAuthData.clear();
    }
    public void addAuth(AuthData authData){
        myAuthData.add(authData);
    }
    public AuthData getAuthFromUser(String username){
        for(AuthData data: myAuthData){
            if(data.username().equals(username)){
                return data;
            }
        }
        return null;
    }
    public AuthData getAuthFromToken(String token) throws DataAccessException{
        for(AuthData data: myAuthData){
            if(data.authToken().equals(token)){
                return data;
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }
    public void deleteAuth(String token) throws DataAccessException {
        for (AuthData a : myAuthData){
            if(a.authToken().equals(token)){
                myAuthData.remove(a);
                return;
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public void clearGameData(){
        myGameData.clear();
    }
    public GameData createNewGame(String gameName){
        Random random = new Random();
        GameData myNewGame = new GameData(random.nextInt(900000) + 100000, null, null, gameName, new ChessGame());
        myGameData.add(myNewGame);
        return myNewGame;
    }
    public Collection<GameData> getGameList() {
        return myGameData;
    }
    public GameData getGameFromID(int gameID) throws DataAccessException {
        for (GameData game : myGameData) {
            if (game.gameID == gameID) {
                return game;
            }
        }
        throw new DataAccessException("Error: bad request");
    }
    public void addPlayer(GameData game, String username, String color) throws DataAccessException {
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

    public void clearUser() {
        myUserData.clear();
    }

    public void addUser(UserData data) {
        myUserData.add(data);
    }

    public UserData getUser(UserData data) throws DataAccessException {
        //A Exception is thrown IF no user matches
        for(UserData d: myUserData){
            if(d.username().equals(data.username())){
                return d;
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public Set<UserData> getUserSet(){
        return myUserData;
    }
    public Set<AuthData> getAuthSet(){
        return myAuthData;
    }
    public Set<GameData> getGameSet(){
        return myGameData;
    }
}
