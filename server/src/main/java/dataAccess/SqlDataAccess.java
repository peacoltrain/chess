package dataAccess;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public class SqlDataAccess {

    public SqlDataAccess() throws DataAccessException{
        configureDatabase();
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
            'jsonGame' TEXT)
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

        }
    }
}
