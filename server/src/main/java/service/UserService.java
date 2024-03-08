package service;
import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

public class UserService {

    private static final DataAccess dataAccess;

    static {
        try {
            dataAccess = new SqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static AuthData register(UserData user) throws DataAccessException {
        try {
            dataAccess.getUser(user);
        } catch (DataAccessException e) {
            if(user.password() != null) {
                dataAccess.addUser(user);
                dataAccess.addAuth(new AuthData(user.username(), UUID.randomUUID().toString()));
                return dataAccess.getAuthFromUser(user.username());
            }
        }
        throw new DataAccessException("Error: already taken");
    }
    public static AuthData login(UserData user) throws DataAccessException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String username = user.username();
        UserData exists = dataAccess.getUser(user);
        if (encoder.matches(user.password(), exists.password())) {
            AuthData returnAuth = new AuthData(username, UUID.randomUUID().toString());
            dataAccess.addAuth(returnAuth);
            return returnAuth;
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public static void logout(String authToken) throws DataAccessException {
        dataAccess.deleteAuth(authToken);
    }
}
