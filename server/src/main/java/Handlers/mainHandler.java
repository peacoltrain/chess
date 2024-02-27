package Handlers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    public static String clear(Request request, Response response){
        ClearService.clearDataBase();
        response.status(200);
        return "";
    }

    public static String list(Request request, Response response){
        JsonObject jsonObject = new JsonObject();
        String authToken = request.headers("authorization");
        try {
            Collection<GameData> returnData = GameService.GameListService(authToken);

            // Create a JsonArray to store game objects
            JsonArray gamesArray = new JsonArray();

            // Iterate over each GameData object and extract desired fields
            for (GameData game : returnData) {
                JsonObject gameJson = new JsonObject();
                gameJson.addProperty("gameID", game.gameID);
                gameJson.addProperty("whiteUsername", game.whiteUsername);
                gameJson.addProperty("blackUsername", game.blackUsername);
                gameJson.addProperty("gameName", game.gameName);
                gamesArray.add(gameJson);
            }

            // Add the gamesArray to the jsonResponse object
            jsonObject.add("games", gamesArray);

            // Convert the jsonResponse object to a JSON string and return it
            return gson.toJson(jsonObject);


        } catch (DataAccessException e ){
            response.status(401);
            String message = e.getMessage();
            jsonObject.addProperty("message", message);
            return gson.toJson(jsonObject);
        }
    }

    public static String create(Request request, Response response){
        JsonObject jsonObject = new JsonObject();
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
        String playerColor = null;
        JsonObject jsonObject = JsonParser.parseString(request.body()).getAsJsonObject();
        if(jsonObject.has("playerColor")){
            playerColor = jsonObject.get("playerColor").getAsString();
        }
        try {
            GameService.JoinService(authToken, playerColor, jsonObject.get("gameID").getAsInt());
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
