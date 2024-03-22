package ui;

import model.UserData;

import java.util.Arrays;
import static ui.EscapeSequences.*;


public class ClientService {
    private final String serverUrl;
    private final ServerFacade server;
    private String authToken;
    public ClientStatus clientStatus = ClientStatus.USEROUT;

    public ClientService(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }
    public String read(String inputString){
        var parse = inputString.toLowerCase().split(" ");
        var userCommand = (parse.length > 0) ? parse[0] : "help";
        var params = Arrays.copyOfRange(parse, 1, parse.length);
        return switch (userCommand){
            case "login" -> login(params);
            case "register" -> register(params);
            case "logout" -> logOut();
            case "create" -> create(params);
            case "list" -> list();
            case "join" -> join(params);
            case "observe" -> observe(params);
            case "quit" -> "  quit";
            default -> help();
        };
    }

    private String login(String... Params){
        var token = server.loginUser(new UserData(Params[0],Params[1], null));
        authToken = token.authToken();
        return "  Logged in as " + SET_TEXT_COLOR_GREEN + token.username();
    }

    private String logOut(){
        if(clientStatus == ClientStatus.USEROUT) { return help(); }
        server.logoutUser(authToken);
        authToken = null;
        return "  You logged out. Come back soon!";
    }
    public String register(String... Params) {
        var token = server.registerNewUser(new UserData(Params[0],Params[1],Params[2]));
        authToken = token.authToken();
        return "  Registered and Logged in as " + SET_TEXT_COLOR_GREEN + token.username();
    }

    private String create(String... Params) {
        if(clientStatus == ClientStatus.USEROUT) { return help(); }
        var game = server.createNew(Params[0], authToken);
        return "  You have created a NEW game. It's ID is " + Integer.toString(game.gameID);
    }

    private String list() {
        return "Not yet done";
    }

    private String join(String... Params) {
        return "Not yet done";
    }

    private String observe(String... Params) {
        return "Not yet done";
    }

    private String help() {
        if(clientStatus == ClientStatus.USEROUT){
            return SET_TEXT_COLOR_MAGENTA + "  register <USERNAME> <PASSWORD> <EMAIL> " + SET_TEXT_COLOR_YELLOW + "- to make an account" + "\n" +
                    SET_TEXT_COLOR_MAGENTA + "  login <USERNAME> <PASSWORD> " + SET_TEXT_COLOR_YELLOW + "- login to existing account" + "\n" +
                    SET_TEXT_COLOR_MAGENTA + "  quit " + SET_TEXT_COLOR_YELLOW + "- end program" + "\n" +
                    SET_TEXT_COLOR_MAGENTA + "  help " + SET_TEXT_COLOR_YELLOW + "- show commands" + "\n";
        }
        else{
            return SET_TEXT_COLOR_MAGENTA + "  create <NAME> " + SET_TEXT_COLOR_YELLOW + "- make a new game" + "\n" +
                    SET_TEXT_COLOR_MAGENTA + "  list " + SET_TEXT_COLOR_YELLOW + "- get list of all current games" + "\n" +
                    SET_TEXT_COLOR_MAGENTA + "  join <ID> [WHITE|BLACK|<empty>] " + SET_TEXT_COLOR_YELLOW + "- join a game" + "\n" +
                    SET_TEXT_COLOR_MAGENTA + "  observe <ID> " + SET_TEXT_COLOR_YELLOW + "- watch a game" + "\n" +
                    SET_TEXT_COLOR_MAGENTA + "  logout " + SET_TEXT_COLOR_YELLOW + "- exit your account" + "\n" +
                    SET_TEXT_COLOR_MAGENTA + "  quit " + SET_TEXT_COLOR_YELLOW + "- end program" + "\n" +
                    SET_TEXT_COLOR_MAGENTA + "  help " + SET_TEXT_COLOR_YELLOW + "- show commands" + "\n";
        }
    }
}
