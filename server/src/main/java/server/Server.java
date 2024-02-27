package server;

import Handlers.UserHandlers;
import Handlers.mainHandler;
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
        Spark.delete("/db", mainHandler::clear);

        //Register User
        Spark.post("/user", UserHandlers::register);

        //Login
        Spark.post("/session", UserHandlers::login);

        //Logout
        Spark.delete("/session", UserHandlers::logout);

        //List games
        Spark.get("/game", mainHandler::list);

        //Create games
        Spark.post("/game", mainHandler::create);

        //Join game
        Spark.put("/game", mainHandler::join);

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
