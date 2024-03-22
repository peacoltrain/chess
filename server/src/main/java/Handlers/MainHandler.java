package Handlers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataAccess.DataAccessException;
import model.GameData;
import service.ClearService;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class MainHandler {

    public static String clear(Request request, Response response){
        //Call ClearService
        try{
            ClearService.clearDataBase();
        }catch(DataAccessException e){}

        //Set response status
        response.status(200);
        return "";
    }

    public static String list(Request request, Response response){
        //Create new JsonObject and gson
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();

        //get authToken
        String authToken = request.headers("authorization");

        //Try to get list of gameData
        try {
            //Call Service
            Collection<GameData> returnData = GameService.gameListService(authToken);

            //Declare JsonArray and make Json for every needed element
            JsonArray gamesArray = new JsonArray();
            for (GameData game : returnData) {
                JsonObject gameJson = new JsonObject();
                gameJson.addProperty("gameID", game.gameID);
                gameJson.addProperty("whiteUsername", game.whiteUsername);
                gameJson.addProperty("blackUsername", game.blackUsername);
                gameJson.addProperty("gameName", game.gameName);
                gamesArray.add(gameJson);
            }

            // Add the array of json to jsonObject
            jsonObject.add("games", gamesArray);
            return gson.toJson(jsonObject);
        } catch (DataAccessException e ) {
            //In the event an Exception is thrown
            response.status(401);
            String message = e.getMessage();
            jsonObject.addProperty("message", message);
            return gson.toJson(jsonObject);
        }
    }

    public static String create(Request request, Response response){
        //Create new JsonObject and gson
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();

        //Get authToken and make a GameData instance with necessary info
        String authToken = request.headers("authorization");
        GameData gameData = gson.fromJson(request.body(), GameData.class);

        try {
            //Try to create game
            GameData returnData = GameService.createService(authToken, gameData.gameName);
            jsonObject.addProperty("gameID", returnData.gameID);
            return gson.toJson(jsonObject);
        } catch (DataAccessException e) {
            //In the event an Exception is thrown
            response.status(401);
            String message = e.getMessage();
            jsonObject.addProperty("message", message);
            return gson.toJson(jsonObject);
        }
    }

    public static String join(Request request, Response response){
        //Create new JsonObject and gson
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();

        //Get authToken, gameID, and playerColor
        String authToken = request.headers("authorization");
        String playerColor = null;
        jsonObject = JsonParser.parseString(request.body()).getAsJsonObject();
        int gameID = jsonObject.get("gameID").getAsInt();
        if(jsonObject.has("playerColor")){
            playerColor = jsonObject.get("playerColor").getAsString();
        }

        try {
            GameService.joinService(authToken, playerColor, gameID);
            return "";
        } catch (DataAccessException e) {
            /*In the event an Exception is thrown, first get the message
            then set the corresponding status. Return Json*/
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
