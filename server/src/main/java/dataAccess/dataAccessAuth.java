package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class dataAccessAuth {

    public static Set<AuthData> myAuthData = new HashSet<>();

    private dataAccessAuth() {
    }

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

    public static AuthData getAuthFromToken(String token){
        for(AuthData data: myAuthData){
            if(data.authToken().equals(token)){
                return data;
            }
        }
        return null;
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
