package service;

import dataAccess.DataAccessException;
import dataAccess.dataAccessAuth;
import dataAccess.dataAccessUser;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {

    public static AuthData register(UserData user) {
//        String username = user.username();
//        var exists = dataAccessUser.getUser(user);
//        if(exists == null){
//            dataAccessUser.addUser(user);
//            dataAccessAuth.addAuth(new AuthData(UUID.randomUUID().toString(),username));
//            return dataAccessAuth.getAuthFromUser(username);
//        }
        return null;
    }
    public static AuthData login(UserData user) throws DataAccessException {
        String username = user.username();
        UserData exists = dataAccessUser.getUser(user);
        if (exists.password().equals(user.password())) {
            dataAccessAuth.addAuth(new AuthData(UUID.randomUUID().toString(), username));
            return dataAccessAuth.getAuthFromUser(username);
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public static void logout(AuthData authToken) {
        dataAccessAuth.deleteAuth(authToken);
    }
}
