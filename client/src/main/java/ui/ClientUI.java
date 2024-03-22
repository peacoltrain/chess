package ui;
import static ui.EscapeSequences.*;
import java.util.Scanner;
public class ClientUI {
    private final ClientService client;

    public ClientUI(String serverURL) {
        this.client = new ClientService(serverURL);
    }

    public void run() {
        System.out.println(EscapeSequences.WHITE_BISHOP + "Welcome to Chess. Type help to see options!" + EscapeSequences.BLACK_BISHOP);

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("  quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.read(line);
                System.out.print(result);
                System.out.print("\n");
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
    }

    private void printPrompt() {
        System.out.print( SET_TEXT_BOLD + SET_TEXT_COLOR_WHITE + "  [" + ((client.clientStatus == ClientStatus.USEROUT) ? "LOGGED_OUT":"LOGGED_IN") + "] " + ">>> " + SET_TEXT_COLOR_BLUE);
    }

}
