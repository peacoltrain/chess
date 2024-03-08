package dataAccess;

import chess.ChessGame;
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
    public void addAuth(AuthData authData){
        throw new RuntimeException("Not yet implemented");
    }
    public AuthData getAuthFromUser(String username){
        throw new RuntimeException("Not yet implemented");
    }
    public AuthData getAuthFromToken(String token) throws DataAccessException{
        throw new RuntimeException("Not yet implemented");
    }
    public void deleteAuth(String token) throws DataAccessException {
        throw new RuntimeException("Not yet implemented");
    }
    public GameData createNewGame(String gameName){
        throw new RuntimeException("Not yet implemented");
    }
    public Collection<GameData> getGameList() {
        throw new RuntimeException("Not yet implemented");
    }
    public GameData getGameFromID(int gameID) throws DataAccessException {
        throw new RuntimeException("Not yet implemented");
    }
    public void addPlayer(GameData game, String username, String color) throws DataAccessException {
        throw new RuntimeException("Not yet implemented");
    }

    public void addUser(UserData data) {
        throw new RuntimeException("Not yet implemented");
    }

    public UserData getUser(UserData data) throws DataAccessException {
        throw new RuntimeException("Not yet implemented");
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

                var keys = preparedStatement.getGeneratedKeys();
            }
        } catch (SQLException e){
            throw new DataAccessException("Not yet done");
        }
    }


    private final String[] creationStatements = {
            """
            CREATE TABLE IF NOT EXISTS authTable (
            `id` INT PRIMARY KEY AUTO_INCREMENT,
            `username` varchar(256) NOT NULL,
            `authToken` varchar(256) NOT NULL)
            """,
            """
            CREATE TABLE IF NOT EXISTS userTable (
            `id` INT PRIMARY KEY AUTO_INCREMENT,
            `username` varchar(256) NOT NULL,
            `password` varchar(256) NOT NULL,
            `email` varchar(256) NOT NULL)
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
