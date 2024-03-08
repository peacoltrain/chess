package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import javax.xml.crypto.Data;
import java.nio.file.DirectoryNotEmptyException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Random;

import static java.sql.Types.NULL;

public class SqlDataAccess implements DataAccess{

    public SqlDataAccess() throws DataAccessException {
        configureDatabase();
    }

    //Clear Methods
    public void clearAuth() throws DataAccessException {
        String sqlStatement = "DELETE FROM authTable";
        execute(sqlStatement);
    }
    public void clearGameData() throws DataAccessException{
        String sqlStatement = "DELETE FROM gameTable";
        execute(sqlStatement);
    }
    public void clearUser() throws DataAccessException{
        String sqlStatement = "DELETE FROM userTable";
        execute(sqlStatement);
    }
    public void addAuth(AuthData authData) throws DataAccessException {
        String sqlStatement = "INSERT INTO authTable (username, authToken) VALUES (?, ?)";
        execute(sqlStatement, authData.username(), authData.authToken());
    }
    public void deleteAuth(String token) throws DataAccessException {
        throw new RuntimeException("Not yet implemented");
    }
    public void addPlayer(GameData game, String username, String color) throws DataAccessException {
        String sqlStatement;
        if(color.equals("white") || color.equals("WHITE")) {
            sqlStatement = "UPDATE gameTable SET `whiteusername` = ? WHERE `gameID` = ?";
        }else {
            sqlStatement = "UPDATE gameTable SET `blackusername` = ? WHERE `gameID` = ?";
        }
        execute(sqlStatement, color, game.gameID);
    }

    public void addUser(UserData data) throws DataAccessException {
        String sqlStatement = "INSERT INTO userTable (username, password, email) VALUES (?, ?, ?)";
        execute(sqlStatement, data.username(), data.password(), data.email());
    }
    public AuthData getAuthFromUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String sqlStatement = "SELECT * FROM authTable WHERE username =?";
            try (var preparedStatement = conn.prepareStatement(sqlStatement)) {
                preparedStatement.setString(1, username);
                try (var returnValue = preparedStatement.executeQuery()) {
                    if (returnValue.next()) {
                        return new AuthData(returnValue.getString("username"), returnValue.getString("authToken"));
                    }
                    throw new DataAccessException("Error: unauthorized");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("get User test message");
        }
    }
    public AuthData getAuthFromToken(String token) throws DataAccessException{
        try (var conn = DatabaseManager.getConnection()) {
            String sqlStatement = "SELECT * FROM authTable WHERE authToken =?";
            try (var preparedStatement = conn.prepareStatement(sqlStatement)) {
                preparedStatement.setString(1, token);
                try (var returnValue = preparedStatement.executeQuery()) {
                    if (returnValue.next()) {
                        return new AuthData(returnValue.getString("username"), returnValue.getString("authToken"));
                    }
                    throw new DataAccessException("Error: unauthorized");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("get User test message");
        }
    }

    public GameData createNewGame(String gameName) throws DataAccessException{
        Gson gson = new Gson();
        Random random = new Random();
        GameData myNewGame = new GameData(random.nextInt(900000) + 100000, null, null, gameName, new ChessGame());
        try (var conn = DatabaseManager.getConnection()) {
            String sqlStatement = "INSERT INTO gameTable (gameId, whiteusername, blackusername, gameName, jsonGame) VALUES (?, Null, Null, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(sqlStatement)) {
                preparedStatement.setInt(1, myNewGame.gameID);
                preparedStatement.setString(2, myNewGame.gameName);
                preparedStatement.setString(3, gson.toJson(myNewGame.game));
                int rows = preparedStatement.executeUpdate();
                return myNewGame;
            }
        } catch (SQLException e) {
            throw new DataAccessException("get User test message");
        }
    }
    public Collection<GameData> getGameList() {
        throw new RuntimeException("Not yet implemented");
    }
    public GameData getGameFromID(int gameID) throws DataAccessException {
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            String sqlStatement = "SELECT * FROM gameTable WHERE gameID =?";
            try (var preparedStatement = conn.prepareStatement(sqlStatement)) {
                preparedStatement.setInt(1, gameID);
                try (var returnValue = preparedStatement.executeQuery()) {
                    if (returnValue.next()) {
                        return new GameData(returnValue.getInt("gameID"),returnValue.getString("whiteUsername"),returnValue.getString("blackUsername")
                        , returnValue.getString("gameName"), gson.fromJson(returnValue.getString("jsonGame"), ChessGame.class));
                    }
                    throw new DataAccessException("Error: unauthorized");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("get User test message");
        }
    }


    public UserData getUser(UserData data) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String sqlStatement = "SELECT username, password, email FROM userTable WHERE username =?";
            try (var preparedStatement = conn.prepareStatement(sqlStatement)) {
                preparedStatement.setString(1, data.username());
                try (var returnValue = preparedStatement.executeQuery()) {
                    if (returnValue.next()) {
                        return new UserData("test", "test", "test");
                    }
                    throw new DataAccessException("Error: unauthorized");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("get User test message");
        }
    }


    private void execute(String sqlStatement, Object... params) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS)){
                for(int i = 0; i < params.length; i++){
                    var par = params[i];
                    if (par instanceof String p) preparedStatement.setString(i + 1, p);
                    else if (par instanceof Integer p) preparedStatement.setInt(i + 1, p);
                    else if (par == null) preparedStatement.setNull(i + 1, NULL);

                }
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e){
            throw new DataAccessException("Not yet done");
        }
    }

    private final String[] creationStatements = {
            """
            CREATE TABLE IF NOT EXISTS authTable (
            `username` varchar(256) NOT NULL,
            `authToken` varchar(256) NOT NULL)
            """,
            """
            CREATE TABLE IF NOT EXISTS userTable (
            `username` varchar(256) NOT NULL,
            `password` varchar(256) NOT NULL,
            `email` varchar(256) NOT NULL,
            PRIMARY KEY (username))
            """,
            """
            CREATE TABLE IF NOT EXISTS gameTable (
            `gameId` INT PRIMARY KEY AUTO_INCREMENT,
            `whiteUsername` varchar(256),
            `blackUsername` varchar(256),
            `gameName` varchar(256) NOT NULL,
            `jsonGame` TEXT)
            """

    };
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : creationStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}
