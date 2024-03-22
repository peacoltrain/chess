import ui.ClientUI;

public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        new ClientUI(serverUrl).run();
    }
}