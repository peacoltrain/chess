import chess.ChessGame;
import dataAccess.dataAccessAuth;
import dataAccess.dataAccessGame;
import dataAccess.dataAccessUser;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.GameService;
import service.UserService;

import java.util.*;

public class serviceTest {


    @Test
    @DisplayName("Register and clear one new user")
    public void registerNewUser() {
        Set<UserData> testSet = new HashSet<>();
        testSet.add(new UserData("username1","thisisnotsecure", "email@email.com"));
        UserService.register(new UserData("username1","thisisnotsecure", "email@email.com"));

        Assertions.assertEquals(testSet, dataAccessUser.myUserData);

        //Clear sets
        ClearService.clearDataBase();
    }

    @Test
    @DisplayName("Register multiple users w/ duplicates")
    public void multiRegister() {
        Set<UserData> testSet = new HashSet<>();
        //Test users
        testSet.add(new UserData("username1","thisisnotsecure", "email@email.com"));
        testSet.add(new UserData("username2","15651312414", "email@email.com"));
        testSet.add(new UserData("username3","thisisnotsecure", "notrealemail"));

        //Services valid
        UserService.register(new UserData("username1","thisisnotsecure", "email@email.com"));
        UserService.register(new UserData("username1","thisISnotsecure", "email@email.com"));
        UserService.register(new UserData("username2","15651312414", "email@email.com"));
        UserService.register(new UserData("username2","15651312", "email@email.com"));
        UserService.register(new UserData("username3","thisisnotsecure", "notrealemail"));
        UserService.register(new UserData("username3","thisisnotsecure", "notrealemail"));

        Assertions.assertEquals(testSet, dataAccessUser.myUserData);

        //Clear sets
        testSet.clear();
        ClearService.clearDataBase();
        Assertions.assertEquals(testSet, dataAccessUser.myUserData);
        ClearService.clearDataBase();
    }

    @Test
    @DisplayName("Register multiple users w/ duplicates and check Tokens")
    public void multiRegisterAndAuth() {
        Set<UserData> testUserSet = new HashSet<>();
        Set<AuthData> testAuthSet = new HashSet<>();
        //Test users
        testUserSet.add(new UserData("username1","thisisnotsecure", "email@email.com"));
        testUserSet.add(new UserData("username2","15651312414", "email@email.com"));
        testUserSet.add(new UserData("username3","thisisnotsecure", "notrealemail"));

        testAuthSet.add(new AuthData(UUID.randomUUID().toString(), "username1"));
        testAuthSet.add(new AuthData(UUID.randomUUID().toString(), "username2"));
        testAuthSet.add(new AuthData(UUID.randomUUID().toString(), "username3"));


        //Services
        UserService.register(new UserData("username1","thisisnotsecure", "email@email.com"));
        UserService.register(new UserData("username1","thisISnotsecure", "email@email.com"));
        UserService.register(new UserData("username2","15651312414", "email@email.com"));
        UserService.register(new UserData("username2","15651312", "email@email.com"));
        UserService.register(new UserData("username3","thisisnotsecure", "notrealemail"));
        UserService.register(new UserData("username3","thisisnotsecure", "notrealemail"));

        //Compare Users and auth
        Assertions.assertEquals(testUserSet, dataAccessUser.myUserData);

        Set<String> testUsernames = new HashSet<>();
        Set<String> serviceUsernames = new HashSet<>();
        for(AuthData test: testAuthSet){
            testUsernames.add(test.username());
        }
        for(AuthData service: dataAccessAuth.myAuthData){
            serviceUsernames.add(service.username());
        }
        Assertions.assertEquals(testUsernames, serviceUsernames);

        //Clear sets
        testUserSet.clear();
        testAuthSet.clear();
        ClearService.clearDataBase();
        Assertions.assertEquals(testUserSet, dataAccessUser.myUserData);
        Assertions.assertEquals(testAuthSet, dataAccessAuth.myAuthData);
        ClearService.clearDataBase();
    }

    @Test
    @DisplayName("Make on user and then log in")
    public void loginSingular() {
        dataAccessUser.addUser(new UserData("login1","batmaniscool", "thebat@knight.com"));
        Assertions.assertEquals(1, dataAccessUser.myUserData.size());
        Assertions.assertEquals(0, dataAccessAuth.myAuthData.size());

        var atmpt1 = UserService.login(new UserData("incorrect","batmaniscool", "thebat@knight.com"));
        Assertions.assertNull(atmpt1);

        var atmpt2 = UserService.login(new UserData("login1","batmanisawesome", "thebat@knight.com"));
        Assertions.assertNull(atmpt2);

        var atmpt3 = UserService.login(new UserData("login1","batmaniscool", "thebat@knight.com"));
        Assertions.assertNotNull(atmpt3);
        ClearService.clearDataBase();
    }

    @Test
    @DisplayName("Make on user and then log in")
    public void logout() {
        Set<AuthData> testSet = new HashSet<>();
        AuthData test = new AuthData(UUID.randomUUID().toString(), "HelloThere");
        dataAccessAuth.addAuth(test);
        Assertions.assertNotNull(dataAccessAuth.myAuthData);
        UserService.logout(test);
        Assertions.assertEquals(testSet, dataAccessAuth.myAuthData);
        ClearService.clearDataBase();
    }

    @Test
    @DisplayName("Create a game")
    public void createGame(){
        Set<GameData> testSet = new HashSet<>();
        AuthData testAuth = UserService.register(new UserData("RandomUser","12345678", "yahoo@gmail.com"));
        testSet.add(new GameData(12345, null, null, "Empty Game", new ChessGame()));
        GameService.CreateService(testAuth.authToken(), "Empty Game");
        Assertions.assertEquals(testSet.size(), dataAccessGame.getGameList().size());
        ClearService.clearDataBase();
    }

    @Test
    @DisplayName("Get a list of games")
    public void GameListTest() {
        List<GameData> testList = new ArrayList<>();
        AuthData testAuth = UserService.register(new UserData("RandomUser","12345678", "yahoo@gmail.com"));

        //Add games to testList
        testList.add(new GameData(12345, null, null, "Empty Game", new ChessGame()));
        testList.add(new GameData(67890, "JohnJack", null, "Half-full Game", new ChessGame()));
        testList.add(new GameData(45454, "Lucy", "Steve", "Full Game", new ChessGame()));

        GameService.CreateService(testAuth.authToken(), "Empty Game");
        GameService.CreateService(testAuth.authToken(), "Half-full Game");
        GameService.CreateService(testAuth.authToken(), "Full Game");

        Assertions.assertEquals(testList.size(), dataAccessGame.getGameList().size());
        ClearService.clearDataBase();

    }

    @Test
    @DisplayName("Join a game")
    public void joinGame(){
        AuthData testAuth1 = UserService.register(new UserData("RandomUser1","12345678", "yahoo@gmail.com"));
        AuthData testAuth2 = UserService.register(new UserData("RandomUser2","12345678", "yahoo@gmail.com"));
        AuthData testAuth3 = UserService.register(new UserData("RandomUser3","12345678", "yahoo@gmail.com"));
        GameData game = GameService.CreateService(testAuth1.authToken(), "Empty Game");
        GameService.JoinService(testAuth1.authToken(), "white", game.gameID);
        GameService.JoinService(testAuth2.authToken(), "black", game.gameID);
        GameService.JoinService(testAuth3.authToken(), null, game.gameID);
        Assertions.assertNotNull(game.getBlackUsername());
        Assertions.assertNotNull(game.getWhiteUsername());
        ClearService.clearDataBase();
    }
}