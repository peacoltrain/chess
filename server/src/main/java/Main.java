import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(); // Create an instance of Server
        server.run(8080);
    }
}