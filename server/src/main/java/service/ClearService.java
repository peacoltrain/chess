package service;

import dataAccess.DataAccessAuth;
import dataAccess.DataAccessGame;
import dataAccess.DataAccessUser;

public class ClearService {

    public static void clearDataBase(){
        DataAccessUser.clearUser();
        DataAccessAuth.clearAuth();
        DataAccessGame.clearGameData();
    }

}
