package service;

import dataAccess.DataAccessException;
import dataAccess.dataAccessAuth;
import dataAccess.dataAccessUser;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {

    public static AuthData register(UserData user) {
        String username = user.username();
        var exists = dataAccessUser.getUser(username);
        if(exists == null){
            dataAccessUser.addUser(user);
            dataAccessAuth.addAuth(new AuthData(UUID.randomUUID().toString(),username));
            return dataAccessAuth.getAuthFromUser(username);
        }
        return null;
    }
    public static AuthData login(UserData user) {
        String username = user.username();
        String password = user.password();
        UserData exists = dataAccessUser.getUser(username);
        if(exists != null){
            if(exists.password().equals(password)){
                dataAccessAuth.addAuth(new AuthData(UUID.randomUUID().toString(), username));
                return dataAccessAuth.getAuthFromUser(username);
            }
        }
        return null;
    }

    public static void logout(AuthData authToken) {
        dataAccessAuth.deleteAuth(authToken);
    }
}
