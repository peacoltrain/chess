package dataAccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public interface DataAccess {
    void clearAuth();
    void addAuth(AuthData authData);
    AuthData getAuthFromUser(String username);
    AuthData getAuthFromToken(String token) throws DataAccessException;
    void deleteAuth(String token) throws DataAccessException;
    void clearGameData();
    GameData createNewGame(String gameName);
    Collection<GameData> getGameList();
    GameData getGameFromID(int gameID) throws DataAccessException;
    void addPlayer(GameData game, String username, String color) throws DataAccessException;
    void clearUser();
    void addUser(UserData data);
    UserData getUser(UserData data) throws DataAccessException;
}
