package service;

import dataAccess.DataAccessException;
import dataAccess.dataAccessAuth;
import dataAccess.dataAccessUser;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {

    public static AuthData register(UserData user) throws DataAccessException {
        UserData exists = null;
        try {
            exists = dataAccessUser.getUser(user);
        } catch (DataAccessException e) {
            dataAccessUser.addUser(user);
            dataAccessAuth.addAuth(new AuthData(user.username(), UUID.randomUUID().toString()));
            return dataAccessAuth.getAuthFromUser(user.username());
        }
        throw new DataAccessException("Error: already taken");
    }
    public static AuthData login(UserData user) throws DataAccessException {
        String username = user.username();
        UserData exists = dataAccessUser.getUser(user);
        if (exists.password().equals(user.password())) {
            AuthData returnAuth = new AuthData(username, UUID.randomUUID().toString());
            dataAccessAuth.addAuth(returnAuth);
            return returnAuth;
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public static void logout(String authToken) throws DataAccessException {
        dataAccessAuth.deleteAuth(authToken);
    }
}
