package service;

import dataAccess.*;

public class ClearService {

    private static final DataAccess dataAccess;

    static {
        try {
            dataAccess = new SqlDataAccess();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clearDataBase() throws DataAccessException{
        dataAccess.clearUser();
        dataAccess.clearAuth();
        dataAccess.clearGameData();
    }

}
