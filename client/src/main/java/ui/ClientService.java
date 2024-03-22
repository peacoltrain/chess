package ui;

import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.GameData;
import model.UserData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static ui.EscapeSequences.*;


public class ClientService {
    private final ServerFacade server;
    private String authToken;
    public ClientStatus clientStatus = ClientStatus.USEROUT;
    Map<Integer, Integer> intMap = new HashMap<>();
    private final ChessPiece[][] testPieceMatrix = new ChessPiece[8][8];

    public ClientService(String serverUrl) {
        server = new ServerFacade(serverUrl);
        setMatrix();
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
        if(clientStatus == ClientStatus.USERIN) { return help(); }
        try {
            var token = server.loginUser(new UserData(Params[0], Params[1], null));
            authToken = token.authToken();
            clientStatus = ClientStatus.USERIN;
            return "  Logged in as " + SET_TEXT_COLOR_GREEN + token.username();
        } catch(Exception e){return "invalid login";}
    }

    private String logOut(){
        if(clientStatus == ClientStatus.USEROUT) { return help(); }
        server.logoutUser(authToken);
        authToken = null;
        clientStatus = ClientStatus.USEROUT;
        return "  You logged out. Come back soon!";
    }
    public String register(String... Params) {
        if(clientStatus == ClientStatus.USERIN) { return help(); }
        try{
            var token = server.registerNewUser(new UserData(Params[0],Params[1],Params[2]));
            authToken = token.authToken();
            clientStatus = ClientStatus.USERIN;
            return "  Registered and Logged in as " + SET_TEXT_COLOR_GREEN + token.username();
        }catch (Exception e) {return "Invalid parameters";}
    }

    private String create(String... Params) {
        if(clientStatus == ClientStatus.USEROUT) { return help(); }
        var game = server.createNew(Params[0], authToken);
        return "  You have created a NEW game. It's ID is " + Integer.toString(game.gameID);
    }

    private String list() {
        if(clientStatus == ClientStatus.USEROUT) { return help(); }
        JsonObject list = server.list(authToken);
        JsonElement jsonArray = list.get("games");
        return printList(jsonArray);
    }

    private String join(String... Params) {
        if(clientStatus == ClientStatus.USEROUT) { return help(); }
        try{
            list();
            int par = Integer.parseInt(Params[0]);
            server.joinGame(authToken, Integer.toString(intMap.get(par)), Params[1]);
        }catch (Exception e){ return "Failed to join game";}
        return printBoard();
    }

    private String observe(String... Params) {
        if(clientStatus == ClientStatus.USEROUT) { return help(); }
        try {
            list();
            int par = Integer.parseInt(Params[0]);
            server.joinGame(authToken, Integer.toString(intMap.get(par)));
        }catch(Exception e){ return "Failed to join game";}
        return printBoard();
    }

    private String printList(JsonElement jsonArray) {
        if (jsonArray != null && jsonArray.isJsonArray()) {
            JsonArray gamesArray = jsonArray.getAsJsonArray();

            // Process each game in the array
            StringBuilder result = new StringBuilder();
            int count = 1;
            for (JsonElement gameElement : gamesArray) {
                GameData gameData = new Gson().fromJson(gameElement, GameData.class);
                intMap.put(count, gameData.gameID);
                result.append("  ").append(count).append("  ");
                result.append(SET_TEXT_COLOR_RED).append(gameData.gameID).append("  ").append(SET_TEXT_COLOR_WHITE);
                result.append("White Player: ").append((gameData.whiteUsername == null) ? "<empty>" : gameData.whiteUsername).append("  ").append(SET_TEXT_COLOR_BLACK);
                result.append("Black Player: ").append((gameData.blackUsername == null) ? "<empty>" : gameData.blackUsername).append("  ").append(SET_TEXT_COLOR_BLUE);
                result.append("Game Name: ").append(gameData.gameName).append("\n");
                ++count;
            }
            return result.toString();
        }
        return "  No games to show.";
    }

    private void setMatrix(){
        //Populate Pawns;
        for(int i = 0; i < 8; ++i){
            testPieceMatrix[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            testPieceMatrix[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }

        testPieceMatrix[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        testPieceMatrix[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        testPieceMatrix[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        testPieceMatrix[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        testPieceMatrix[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        testPieceMatrix[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        testPieceMatrix[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        testPieceMatrix[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);

        testPieceMatrix[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        testPieceMatrix[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        testPieceMatrix[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        testPieceMatrix[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        testPieceMatrix[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        testPieceMatrix[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        testPieceMatrix[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        testPieceMatrix[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
    }

    private String printBoard(){
        String[] charsF = {"a","b","c","d","e","f","g","h"};
        String[] charsB = {"h","g","f","e","d","c","b","a"};
        StringBuilder result = new StringBuilder();

        //Regular
        result.append(EMPTY);
        for(String c: charsF){
            result.append(EMPTY).append(c).append(" ");
        }
        result.append("\n");
        for(int i = 7; i >= 0; --i){
            result.append("   ").append(i+1).append(" ");
            for(int j = 0; j <= 7; ++j){
                if((i + j) % 2 != 0){result.append("\u001b[48;5;222m");}
                if((i + j) % 2 == 0){result.append("\u001b[48;5;95m");}
                result.append(" ").append(convertPieceToString(testPieceMatrix[i][j])).append(" ");
            }
            result.append(RESET_BG_COLOR).append(SET_TEXT_COLOR_BLUE).append(" ").append(i+1).append(" \n");
        }
        result.append(EMPTY);
        for(String c: charsF){
            result.append(EMPTY).append(c).append(" ");
        }
        result.append("\n\n");

        //Backward
        result.append(EMPTY);
        for(String c: charsB){
            result.append(EMPTY).append(c).append(" ");
        }
        result.append("\n");
        for(int i = 0; i <= 7; ++i){
            result.append("   ").append(i+1).append(" ");
            for(int j = 7; j >= 0; --j){
                if((i + j) % 2 != 0){result.append("\u001b[48;5;222m");}
                if((i + j) % 2 == 0){result.append("\u001b[48;5;95m");}
                result.append(" ").append(convertPieceToString(testPieceMatrix[i][j])).append(" ");
            }
            result.append(RESET_BG_COLOR).append(SET_TEXT_COLOR_BLUE).append(" ").append(i+1).append(" \n");
        }
        result.append(EMPTY);
        for(String c: charsB){
            result.append(EMPTY).append(c).append(" ");
        }
        result.append("\n");

        return result.toString();
    }

    private String convertPieceToString(ChessPiece piece){
        if(piece == null){ return EMPTY; }
        else if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return switch (piece.getPieceType()){
                case PAWN -> SET_TEXT_COLOR_WHITE + WHITE_PAWN;
                case KING -> SET_TEXT_COLOR_WHITE + WHITE_KING;
                case QUEEN -> SET_TEXT_COLOR_WHITE + WHITE_QUEEN;
                case KNIGHT -> SET_TEXT_COLOR_WHITE + WHITE_KNIGHT;
                case ROOK -> SET_TEXT_COLOR_WHITE + WHITE_ROOK;
                case BISHOP -> SET_TEXT_COLOR_WHITE + WHITE_BISHOP;
            };
        }
        else {
            return switch (piece.getPieceType()){
                case PAWN -> SET_TEXT_COLOR_BLACK + BLACK_PAWN;
                case KING -> SET_TEXT_COLOR_BLACK + BLACK_KING;
                case QUEEN -> SET_TEXT_COLOR_BLACK + BLACK_QUEEN;
                case KNIGHT -> SET_TEXT_COLOR_BLACK + BLACK_KNIGHT;
                case ROOK -> SET_TEXT_COLOR_BLACK + BLACK_ROOK;
                case BISHOP -> SET_TEXT_COLOR_BLACK + BLACK_BISHOP;
            };
        }
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
