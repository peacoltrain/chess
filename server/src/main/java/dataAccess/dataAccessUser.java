package dataAccess;


import model.AuthData;
import model.UserData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class dataAccessUser {

    public static Set<UserData> myUserData = new HashSet<>();

    private dataAccessUser(){}
    public static void clearUser() {
        myUserData.clear();
    }

    public static void addUser(UserData data) {
        myUserData.add(data);
    }

    public static UserData getUser(String username) {
        for (UserData data : myUserData) {
            if (data.username().equals(username)) {
                return data;
            }
        }
        return null;
    }

    public static Collection<UserData> getAllUsers(){
        return myUserData;
    }
}