package Handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class UserHandlers {

    public static String register(Request request, Response response){
        //Create new JsonObject and gson
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();

        //Get UserData and Check if valid request, return if invalid
        UserData registerData = gson.fromJson(request.body(),UserData.class);
        if(registerData.username() == null || registerData.password() == null){
            response.status(400);
            jsonObject.addProperty("message", "Error: bad request");
            return gson.toJson(jsonObject);
        }


        try{
            AuthData returnData = UserService.register(registerData);
            return gson.toJson(returnData);
        } catch (DataAccessException e) {
            //In the event an Exception is thrown
            response.status(403);
            String message = e.getMessage();
            jsonObject.addProperty("message", message);
            return gson.toJson(jsonObject);
        }

    }

    public static String login(Request request, Response response){
        //Create new JsonObject and gson
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();

        //get UserData from request
        UserData loginData = gson.fromJson(request.body(), UserData.class);
        try{
            AuthData returnData = UserService.login(loginData);
            return gson.toJson(returnData);
        } catch(DataAccessException e){
            //In the event an Exception is thrown
            response.status(401);
            String message = e.getMessage();
            jsonObject.addProperty("message", message);
            return gson.toJson(jsonObject);
        }
    }

    public static String logout(Request request, Response response){
        //Create new JsonObject and gson
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();

        //get authToken
        String authToken = request.headers("authorization");
        try {
            UserService.logout(authToken);
            return "";
        } catch (DataAccessException e) {
            //In the event an Exception is thrown
            response.status(401);
            String message = e.getMessage();
            jsonObject.addProperty("message", message);
            return gson.toJson(jsonObject);
        }
    }

}
