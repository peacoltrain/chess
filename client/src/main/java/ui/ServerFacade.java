package ui;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.UserData;
import model.AuthData;
import model.GameData;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ServerFacade {

    private final String serverUrl;
    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData registerNewUser(UserData userData){
        var path = "/user";
        return this.makeRequest("POST",path, AuthData.class, userData, null);
    }

    public AuthData loginUser(UserData userData){
        var path = "/session";
        return this.makeRequest("POST", path, AuthData.class, userData, null);
    }

    public void logoutUser(String token){
        this.makeRequest("DELETE", "/session", null, null, token);
    }

    public GameData createNew(String gameData, String token){
        GameData tempGame = new GameData(0, null, null, "CoolGame", new ChessGame());
        return this.makeRequest("POST", "/game", GameData.class, tempGame, token);
    }

    public JsonObject list(String token){
        return this.makeRequest("GET", "/game", JsonObject.class, null, token);
    }

    public void joinGame(String authToken, String... Params){
        JsonObject jsonRequest = new JsonObject();
        if(Params.length == 2) {jsonRequest.addProperty("playerColor", Params[1]);}
        jsonRequest.addProperty("gameID", Integer.parseInt(Params[0]));
        this.makeRequest("PUT", "/game", null, jsonRequest, authToken);
    }


    private <T> T makeRequest(String method, String path, Class<T> responseClass, Object request, String authentication) throws RuntimeException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setReadTimeout(5000);
            http.setRequestMethod(method);
            if(method.equals("POST")) { http.setDoOutput(true); }
            writeBody(request, authentication ,http);
            //var outStream = http.getOutputStream();
            http.connect();
            var status = http.getResponseCode();
            if(status != 200) { throw new IOException(); }
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private static void writeBody(Object request, String auth, HttpURLConnection http) throws IOException {
        if(auth != null) {
            http.setRequestProperty("Authorization", auth);
        }
        if(request instanceof JsonObject){
            String jsonString = request.toString();
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "application/json");
            try (OutputStream outputStream = http.getOutputStream()) {
                byte[] input = jsonString.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }
            return;
        }
        if(request != null){
            http.addRequestProperty("Content-Type", "application/json");
            String asJson = new Gson().toJson(request);
            try (OutputStream outputStream = http.getOutputStream()) {
                byte[] input = asJson.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
}
