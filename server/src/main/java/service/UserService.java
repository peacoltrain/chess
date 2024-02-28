package service;
import dataAccess.DataAccessException;
import dataAccess.DataAccessAuth;
import dataAccess.DataAccessUser;
import model.AuthData;
import model.UserData;
import java.util.UUID;

public class UserService {

    public static AuthData register(UserData user) throws DataAccessException {
        try {
            DataAccessUser.getUser(user);
        } catch (DataAccessException e) {
            if(user.password() != null) {
                DataAccessUser.addUser(user);
                DataAccessAuth.addAuth(new AuthData(user.username(), UUID.randomUUID().toString()));
                return DataAccessAuth.getAuthFromUser(user.username());
            }
        }
        throw new DataAccessException("Error: already taken");
    }
    public static AuthData login(UserData user) throws DataAccessException {
        String username = user.username();
        UserData exists = DataAccessUser.getUser(user);
        if (exists.password().equals(user.password())) {
            AuthData returnAuth = new AuthData(username, UUID.randomUUID().toString());
            DataAccessAuth.addAuth(returnAuth);
            return returnAuth;
        }
        throw new DataAccessException("Error: unauthorized");
    }

    public static void logout(String authToken) throws DataAccessException {
        DataAccessAuth.deleteAuth(authToken);
    }
}
