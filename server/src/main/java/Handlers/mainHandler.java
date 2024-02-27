package Handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.DataAccessException;
import model.GameData;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class mainHandler {
    static Gson gson = new Gson();
    static JsonObject jsonObject = new JsonObject();

    public static String clear(Request request, Response response){
        ClearService.clearDataBase();
        response.status(200);
        return "";
    }

    public static String list(Request request, Response response){
        String authToken = request.headers("authorization");
        try {
            Collection<GameData> returnData = GameService.GameListService(authToken);
            return gson.toJson(returnData);
        } catch (DataAccessException e ){
            response.status(401);
            String message = e.getMessage();
            jsonObject.addProperty("message", message);
            return gson.toJson(jsonObject);
        }
    }

    public static String create(Request request, Response response){
        String authToken = request.headers("authorization");
        GameData gameData = gson.fromJson(request.body(), GameData.class);

        try {
            GameData returnData = GameService.CreateService(authToken, gameData);
            jsonObject.addProperty("gameID", returnData.gameID);
            return gson.toJson(jsonObject);
        } catch (DataAccessException e) {
            response.status(401);
            String message = e.getMessage();
            jsonObject.addProperty("message", message);
            return gson.toJson(jsonObject);
        }
    }

    public static String join(Request request, Response response){
        String authToken = request.headers("authorization");
        String colorReq = jsonObject.get("playerColor").getAsString();
        int gameID = jsonObject.get("gameID").getAsInt();
        try {
            GameService.JoinService(authToken, colorReq, gameID);
            return "";
        } catch (DataAccessException e) {
            String message = e.getMessage();
            switch (message) {
                case "Error: bad request" -> response.status(400);
                case "Error: unauthorized" -> response.status(401);
                case "Error: already taken" -> response.status(403);
                default -> response.status(500);
            }
            jsonObject.addProperty("message", message);
            return gson.toJson(jsonObject);
        }
    }

}
