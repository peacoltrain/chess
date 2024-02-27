package service;

import dataAccess.dataAccessAuth;
import dataAccess.dataAccessGame;
import dataAccess.dataAccessUser;

public class ClearService {

    public static void clearDataBase(){
        dataAccessUser.clearUser();
        dataAccessAuth.clearAuth();
        dataAccessGame.clearGameData();
    }

}
