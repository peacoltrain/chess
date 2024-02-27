package Handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import service.ClearService;
import service.UserService;
import spark.Request;
import spark.Response;

public class mainHandler {

    public static String clear(Request request, Response response){
        Gson gson = new Gson();
        ClearService.clearDataBase();
        response.status(200);
        return "";
    }

    public static String list(Request request, Response response){
        Gson gson = new Gson();
        ClearService.clearDataBase();
        response.status(200);
        return "";
    }

    public static String create(Request request, Response response){
        Gson gson = new Gson();
        ClearService.clearDataBase();
        response.status(200);
        return "";
    }

    public static String join(Request request, Response response){
        Gson gson = new Gson();
        ClearService.clearDataBase();
        response.status(200);
        return "";
    }

}
