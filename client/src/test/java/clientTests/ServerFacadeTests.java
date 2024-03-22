package clientTests;

import dataAccess.SqlDataAccess;
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
            facade.registerNewUser(new UserData("OGjack", null, "yo@yo"));
            Assertions.fail("Should have throw an exception");
        } catch (Exception e){
            Assertions.assertTrue(true);
        }
    }

}
