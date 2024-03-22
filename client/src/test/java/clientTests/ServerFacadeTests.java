package clientTests;

import dataAccess.SqlDataAccess;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.ClearService;
import ui.ClientService;
import ui.ClientUI;
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
        AuthData authData2 = facade.logginUser(new UserData("Calvin","asdfef", null));
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
            AuthData authData2 = facade.logginUser(new UserData("Cal", "asdfef", null));
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
            AuthData authData2 = facade.logginUser(new UserData("Pedro", "asef", null));
            Assertions.fail("Should throw exception");
        } catch (Exception e) {
            Assertions.assertTrue(true);
        }
    }
}
