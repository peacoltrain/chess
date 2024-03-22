package ui;

import com.google.gson.Gson;
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
        return this.makeRequest("POST",path, AuthData.class, userData);
    }

    public AuthData logginUser(UserData userData){
        var path = "/session";
        return this.makeRequest("POST", path, AuthData.class, userData);
    }

    public void logoutUser(String token){
        this.makeRequest("DELETE", "/session", null, token);
    }


    private <T> T makeRequest(String method, String path, Class<T> responseClass, Object request) throws RuntimeException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setReadTimeout(5000);
            http.setRequestMethod(method);
            if(method.equals("POST")) { http.setDoOutput(true); }
            writeBody(request, http);
            var outStream = http.getOutputStream();
            http.connect();
            var status = http.getResponseCode();
            if(status != 200) { throw new IOException(); }
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            if(request instanceof String ) {
                http.setRequestProperty("Authorization", request.toString());
            }
            else {
                http.addRequestProperty("Content-Type", "application/json");
                String asJson = new Gson().toJson(request);
                try (OutputStream outputStream = http.getOutputStream()) {
                    byte[] input = asJson.getBytes(StandardCharsets.UTF_8);
                    outputStream.write(input, 0, input.length);
                }
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
