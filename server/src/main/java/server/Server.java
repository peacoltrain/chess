package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        createRoutes();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private static void createRoutes() {
        //Clear application
        Spark.delete("/db", (request, response) -> "Not yet finished");

        //Register User
        Spark.post("/user", (request, response) -> "Not yet finished");

        //Login
        Spark.post("/session", (request, response) -> "Not yet finished");

        //Logout
        Spark.delete("/session", (request, response) -> "Not yet finished");

        //List games
        Spark.get("/game", (request, response) -> "Not yet finished");

        //Create games
        Spark.post("/game", (request, response) -> "Not yet finished");

        //Join game
        Spark.put("/game", (request, response) -> "Not yet finished");

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
