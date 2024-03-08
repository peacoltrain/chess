package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public interface DataAccess {
    void clearAuth() throws DataAccessException;
    void addAuth(AuthData authData) throws DataAccessException;
    AuthData getAuthFromUser(String username) throws DataAccessException;
    AuthData getAuthFromToken(String token) throws DataAccessException;
    void deleteAuth(String token) throws DataAccessException;
    void clearGameData() throws DataAccessException;
    GameData createNewGame(String gameName) throws DataAccessException;
    Collection<GameData> getGameList();
    GameData getGameFromID(int gameID) throws DataAccessException;
    void addPlayer(GameData game, String username, String color) throws DataAccessException;
    void clearUser() throws DataAccessException;
    void addUser(UserData data) throws DataAccessException;
    UserData getUser(UserData data) throws DataAccessException;
}
