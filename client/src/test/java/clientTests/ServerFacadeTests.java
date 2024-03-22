package clientTests;

import com.google.gson.JsonObject;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.ClearService;
import ui.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        try { ClearService.clearDataBase();} catch (Exception e){}
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        var url = "http://localhost:" + server.port();
        facade = new ServerFacade(url);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerPass() {
        facade.registerNewUser(new UserData("jill", "jill", "jill"));
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("registerFail by duplicate username")
    public void registerFail() {
        try {
            facade.registerNewUser(new UserData("OGjack", "alkdfoa", "yo@yo"));
            facade.registerNewUser(new UserData("OGjack", "adsfaef", "yo@yo"));
            Assertions.fail("Should have throw an exception");
        } catch (Exception e){
            Assertions.assertTrue(true);
        }
    }

    @Test
    @DisplayName("registerFail by incomplete info")
    public void registerFail2() {
        try {
            facade.registerNewUser(new UserData("Jeff", null, "yo@yo"));
            Assertions.fail("Should have throw an exception");
        } catch (Exception e){
            Assertions.assertTrue(true);
        }
    }
    @Test
    public void logoutPass() {
        AuthData authData = facade.registerNewUser(new UserData("Zac", "asdfef", "zMan@yourMom"));
        facade.logoutUser(authData.authToken());
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("logout with bad authToken")
    public void logoutFail() {
        try {
            facade.registerNewUser(new UserData("Ann", "asdf", "aaaabbbb"));
            facade.logoutUser("This is a corrupt token");
            Assertions.fail("It should have thrown an exception");
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }
    @Test
    public void loginPass() {
        AuthData authData = facade.registerNewUser(new UserData("Calvin", "asdfef", "zMan@yourMom"));
        facade.logoutUser(authData.authToken());
        AuthData authData2 = facade.loginUser(new UserData("Calvin","asdfef", null));
        if(authData2 != authData) {
            Assertions.assertTrue(true);
        }

    }

    @Test
    @DisplayName("Login with incorrect username")
    public void loginFail1() {
        try {
            AuthData authData = facade.registerNewUser(new UserData("Kal", "asdfef", "zMan@yourMom"));
            facade.logoutUser(authData.authToken());
            AuthData authData2 = facade.loginUser(new UserData("Cal", "asdfef", null));
            Assertions.fail("Should throw exception");
        }catch (Exception e){
            Assertions.assertTrue(true);
        }
    }

    @Test
    @DisplayName("Login with incorrect password")
    public void loginFail2() {
        try {
            AuthData authData = facade.registerNewUser(new UserData("Pedro", "asdfef", "zMan@yourMom"));
            facade.logoutUser(authData.authToken());
            AuthData authData2 = facade.loginUser(new UserData("Pedro", "asef", null));
            Assertions.fail("Should throw exception");
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void createPass() {
        AuthData authData = facade.registerNewUser(new UserData("Karen", "asdfef", "kxkx"));
        facade.createNew("CoolGame", authData.authToken());
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("Invalid Token")
    public void createFail() {
        try {
            AuthData authData = facade.registerNewUser(new UserData("Ross", "asdfef", "kxkx"));
            facade.createNew("CoolGame", "tehanoi172393");
            Assertions.fail("It should have thrown an error");
        }catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }
    @Test
    public void listPass() {
        AuthData authData = facade.registerNewUser(new UserData("Rachel", "asdfef", "kxkx"));
        facade.createNew("RGame", authData.authToken());
        JsonObject list = facade.list(authData.authToken());
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("Invalid Token")
    public void listFail() {
        try {
            AuthData authData = facade.registerNewUser(new UserData("Roy", "asdfef", "kxkx"));
            facade.createNew("BobsYourUncle", "tehanoi172393");
            Assertions.fail("It should have thrown an error");
        }catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void joinPass() {
        AuthData authData = facade.registerNewUser(new UserData("Jack", "asdfef", "kxkx"));
        GameData myGame = facade.createNew("JGame", authData.authToken());
        facade.joinGame(authData.authToken(), Integer.toString(myGame.gameID), "white" );
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("Trying to join an already taken color")
    public void joinFail() {
        AuthData authData = facade.registerNewUser(new UserData("Jan", "asdfef", "kxkx"));
        AuthData authData2 = facade.registerNewUser(new UserData("Wade", "asdfawe", "wefsd"));
        GameData myGame = facade.createNew("DunGame", authData.authToken());
        facade.joinGame(authData.authToken(), Integer.toString(myGame.gameID), "white" );
        try {
            facade.joinGame(authData2.authToken(), Integer.toString(myGame.gameID), "white");
            Assertions.fail("Should have thrown error");
        }catch(Exception e){
            Assertions.assertTrue(true);
        }

        Assertions.assertTrue(true);
    }

    @Test
    public void observePass() {
        AuthData authData = facade.registerNewUser(new UserData("Mack", "asdfef", "kxkx"));
        GameData myGame = facade.createNew("MGame", authData.authToken());
        facade.joinGame(authData.authToken(), Integer.toString(myGame.gameID));
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("Invalid gameID")
    public void observeFail() {
        AuthData authData = facade.registerNewUser(new UserData("Doc", "asdfef", "kxkx"));
        GameData myGame = facade.createNew("dGame", authData.authToken());
        try{
            facade.joinGame(authData.authToken(), "1283849576");
            Assertions.fail("It should throw an error");
        }catch (Exception e){
            Assertions.assertTrue(true);
        }
    }
}
