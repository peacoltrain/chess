import chess.ChessGame;
import dataAccess.DataAccessException;
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
        try {
            UserService.register(new UserData("username1", "thisisnotsecure", "email@email.com"));
        } catch (DataAccessException e) {
            Assertions.fail("Not supposed to throw error");
        }

        Assertions.assertEquals(testSet, dataAccessUser.myUserData);

        //Clear sets
        ClearService.clearDataBase();
    }

    @Test
    @DisplayName("Register invalid users")
    public void registerInvalidUser() {
        Set<UserData> testSet = new HashSet<>();

        //Attempt to add incomplete user
        try {
            UserService.register(new UserData("username1", null, "email@email.com"));
        } catch (DataAccessException e) {}
        Assertions.assertEquals(testSet, dataAccessUser.myUserData);

        //Add valid user
        testSet.add(new UserData("username1","thisisnotsecure", "email@email.com"));
        try {
            UserService.register(new UserData("username1", "thisisnotsecure", "email@email.com"));
        } catch (DataAccessException e) { Assertions.fail("Shouldn't throw error");}
        Assertions.assertEquals(testSet, dataAccessUser.myUserData);

        try {
            UserService.register(new UserData("username1", "thisisnotsecure2", "email@email.com"));
        } catch (DataAccessException e) {}
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
        try{ UserService.register(new UserData("username1","thisisnotsecure", "email@email.com"));
        } catch (DataAccessException e) { Assertions.fail("Not supposed to error");}
        try {
            UserService.register(new UserData("username1", "thisISnotsecure", "email@email.com"));
            Assertions.fail("Should error");
        } catch (DataAccessException e) {}
        try {UserService.register(new UserData("username2", "15651312414", "email@email.com"));
        } catch (DataAccessException e) {Assertions.fail("Not supposed to error");}
        try {
            UserService.register(new UserData("username2", "15651312", "email@email.com"));
            Assertions.fail("Should error");
        } catch (DataAccessException e) {}
        try { UserService.register(new UserData("username3", "thisisnotsecure", "notrealemail"));
        } catch (DataAccessException e) { Assertions.fail("Shouln't throw error");}
        try {
            UserService.register(new UserData("username3", "thisisnotsecure", "notrealemail"));
            Assertions.fail("Should throw error");
        } catch (DataAccessException e) {}

        Assertions.assertEquals(testSet, dataAccessUser.myUserData);

        //Clear sets
        testSet.clear();
        ClearService.clearDataBase();
        Assertions.assertEquals(testSet, dataAccessUser.myUserData);
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
        try{ UserService.register(new UserData("username1","thisisnotsecure", "email@email.com"));
        } catch (DataAccessException e) { Assertions.fail("Not supposed to error");}
        try {
            UserService.register(new UserData("username1", "thisISnotsecure", "email@email.com"));
            Assertions.fail("Should error");
        } catch (DataAccessException e) {}
        try {UserService.register(new UserData("username2", "15651312414", "email@email.com"));
        } catch (DataAccessException e) {Assertions.fail("Not supposed to error");}
        try {
            UserService.register(new UserData("username2", "15651312", "email@email.com"));
            Assertions.fail("Should error");
        } catch (DataAccessException e) {}
        try { UserService.register(new UserData("username3", "thisisnotsecure", "notrealemail"));
        } catch (DataAccessException e) { Assertions.fail("Shouln't throw error");}
        try {
            UserService.register(new UserData("username3", "thisisnotsecure", "notrealemail"));
            Assertions.fail("Should throw error");
        } catch (DataAccessException e) {}

        //Compare Users and auth
        Assertions.assertEquals(testUserSet, dataAccessUser.myUserData);
        Assertions.assertEquals(testAuthSet.size(), dataAccessAuth.myAuthData.size());

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

        try{
            var atmpt1 = UserService.login(new UserData("incorrect","batmaniscool", "thebat@knight.com"));
            Assertions.fail("Expected to throw an error");
        }
        catch (DataAccessException e) {}

        try{
            var atmpt2 = UserService.login(new UserData("login1","batmanisawesome", "thebat@knight.com"));
            Assertions.fail("Expected to throw an error");
        } catch (DataAccessException e) {}

        try{
            var atmpt3 = UserService.login(new UserData("login1","batmaniscool", "thebat@knight.com"));
        } catch (DataAccessException e) { Assertions.fail("Got an Exception" + e); }
        ClearService.clearDataBase();
    }

    @Test
    @DisplayName("Make on multiple and then log in")
    public void logout() {
        Set<AuthData> testSet = new HashSet<>();
        AuthData test = new AuthData(UUID.randomUUID().toString(), "HelloThere");
        dataAccessAuth.addAuth(test);
        Assertions.assertNotNull(dataAccessAuth.myAuthData);
        try{
            UserService.logout(test.authToken());
        }catch (DataAccessException e){ Assertions.fail("Should not have thrown error");}
        Assertions.assertEquals(testSet, dataAccessAuth.myAuthData);
        ClearService.clearDataBase();
    }

    @Test
    @DisplayName("Create a game")
    public void createGame(){
        Set<GameData> testSet = new HashSet<>();
        AuthData testAuth = null;
        try {
            testAuth = UserService.register(new UserData("RandomUser", "12345678", "yahoo@gmail.com"));
        } catch (DataAccessException e) { Assertions.fail("Should not throw error");}
        testSet.add(new GameData(12345, null, null, "Empty Game", new ChessGame()));
        try{
            GameService.CreateService(testAuth.authToken(), new GameData(12345,null,null,"Empty Game", new ChessGame()));
        }catch(DataAccessException e){ Assertions.fail("Shouldn't throw error.");}
        Assertions.assertEquals(testSet.size(), dataAccessGame.getGameList().size());
        ClearService.clearDataBase();
    }

    @Test
    @DisplayName("Get a list of games")
    public void GameListTest() {
        List<GameData> testList = new ArrayList<>();
        AuthData testAuth = null;
        try { testAuth = UserService.register(new UserData("RandomUser", "12345678", "yahoo@gmail.com"));
        } catch (DataAccessException e) { Assertions.fail("Shouldn't throw error");}

        //Add games to testList
        testList.add(new GameData(12345, null, null, "Empty Game", new ChessGame()));
        testList.add(new GameData(67890, "JohnJack", null, "Half-full Game", new ChessGame()));
        testList.add(new GameData(45454, "Lucy", "Steve", "Full Game", new ChessGame()));

        try{
            GameService.CreateService(testAuth.authToken(), new GameData(173839,null,null,"Empty Game", new ChessGame()));
        }catch(DataAccessException e){ Assertions.fail("Shouldn't throw error.");}
        try{
            GameService.CreateService(testAuth.authToken(), new GameData(17283,null,null,"Half-full", new ChessGame()));
        }catch(DataAccessException e){ Assertions.fail("Shouldn't throw error.");}
        try{
            GameService.CreateService(testAuth.authToken(), new GameData(19485,null,null,"Full Game", new ChessGame()));
        }catch(DataAccessException e){ Assertions.fail("Shouldn't throw error.");}

        Assertions.assertEquals(testList.size(), dataAccessGame.getGameList().size());
        ClearService.clearDataBase();

    }

    @Test
    @DisplayName("Join a game")
    public void joinGame(){
        AuthData testAuth1 = null;
        AuthData testAuth2 = null;
        AuthData testAuth3 = null;

        try{ testAuth1 = UserService.register(new UserData("RandomUser1","12345678", "yahoo@gmail.com"));
        }catch (DataAccessException e) {Assertions.fail("Shouldn't throw error");}
        try{ testAuth2 = UserService.register(new UserData("RandomUser2","12345678", "yahoo@gmail.com"));
        }catch (DataAccessException e) {Assertions.fail("Shouldn't throw error");}
        try{ testAuth3 = UserService.register(new UserData("RandomUser3","12345678", "yahoo@gmail.com"));
        }catch (DataAccessException e) {Assertions.fail("Shouldn't throw error");}
        GameData game = null;
        try{
            game = GameService.CreateService(testAuth1.authToken(), new GameData(1234, null,null, "Empty", new ChessGame()));
        } catch (DataAccessException e) {Assertions.fail("Should not throw");}
        try {
            GameService.JoinService(testAuth1.authToken(), "white", game.gameID);
        } catch (DataAccessException e){Assertions.fail("Should not throw");}
        try{
            GameService.JoinService(testAuth2.authToken(), "black", game.gameID);
        } catch (DataAccessException e){Assertions.fail("Should not throw");}
        try{
            GameService.JoinService(testAuth3.authToken(), null, game.gameID);
        } catch (DataAccessException e){Assertions.fail("Should not throw");}
        Assertions.assertEquals(testAuth1.username(), game.whiteUsername);
        Assertions.assertEquals(testAuth2.username(), game.blackUsername);
        ClearService.clearDataBase();
    }
}