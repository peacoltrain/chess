package dataAccess;
import model.AuthData;
import java.util.HashSet;
import java.util.Set;

public class DataAccessAuth {

    public static Set<AuthData> myAuthData = new HashSet<>();
    public static void clearAuth() {
        myAuthData.clear();
    }
    public static void addAuth(AuthData authData){
        myAuthData.add(authData);
    }
    public static AuthData getAuthFromUser(String username){
        for(AuthData data: myAuthData){
            if(data.username().equals(username)){
                return data;
            }
        }
        return null;
    }
    public static AuthData getAuthFromToken(String token) throws DataAccessException{
        for(AuthData data: myAuthData){
            if(data.authToken().equals(token)){
                return data;
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }
    public static void deleteAuth(String token) throws DataAccessException {
        for (AuthData a : myAuthData){
            if(a.authToken().equals(token)){
                myAuthData.remove(a);
                return;
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }

}
