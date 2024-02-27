package Handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.ClearService;
import service.UserService;
import spark.Request;
import spark.Response;

public class UserHandlers {
    public static String register(Request request, Response response){
        Gson gson = new Gson();
        ClearService.clearDataBase();
        response.status(200);
        return "";
    }

    public static String login(Request request, Response response){
        Gson gson = new Gson();
        UserData loginData = gson.fromJson(request.body(), UserData.class);
        try{
            AuthData returnData = UserService.login(loginData);
            return gson.toJson(returnData);
        } catch(DataAccessException e){
            response.status(401);
            String message = e.getMessage();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("message", message);
            return gson.toJson(jsonObject);
        }
    }

    public static String logout(Request request, Response response){
        Gson gson = new Gson();
        ClearService.clearDataBase();
        response.status(200);
        return "";
    }

}
