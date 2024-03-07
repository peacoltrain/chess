package dataAccess;
import model.UserData;
import java.util.HashSet;
import java.util.Set;


public class DataAccessUser {

    public static Set<UserData> myUserData = new HashSet<>();

    public static void clearUser() {
        myUserData.clear();
    }

    public static void addUser(UserData data) {
        myUserData.add(data);
    }

    public static UserData getUser(UserData data) throws DataAccessException {
        //A Exception is thrown IF no user matches
        for(UserData d: myUserData){
            if(d.username().equals(data.username())){
                return d;
            }
        }
        throw new DataAccessException("Error: unauthorized");
    }
}