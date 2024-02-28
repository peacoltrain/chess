package server;

import Handlers.UserHandlers;
import Handlers.MainHandler;
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
        Spark.delete("/db", MainHandler::clear);

        //Register User
        Spark.post("/user", UserHandlers::register);

        //Login
        Spark.post("/session", UserHandlers::login);

        //Logout
        Spark.delete("/session", UserHandlers::logout);

        //List games
        Spark.get("/game", MainHandler::list);

        //Create games
        Spark.post("/game", MainHandler::create);

        //Join game
        Spark.put("/game", MainHandler::join);

    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
