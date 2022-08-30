package com.revature.service;

import com.revature.dao.UserDao;
import com.revature.model.User;
import io.jsonwebtoken.Jwts;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;


/*
 * getUserEmailByEmail
 * getUserByInputEmail
 * sendToken
 * */
public class UserServiceTest {

    @Test
    public void testGetUserEmailByEmailPositive() {

        UserDao mockUserDao = mock(UserDao.class);
        String email = "jd80@a.ca";

        boolean expected = true;

        when(mockUserDao.getUserEmailByEmail(email)).thenReturn(true);
        UserService userService = new UserService(mockUserDao);

        boolean actual = userService.getUserEmailByEmail(email);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetUserEmailByEmailNegative() {
        UserDao mockUserDao = mock(UserDao.class);
        String email = "jd80@a.ca";

        boolean expected = false;

        when(mockUserDao.getUserEmailByEmail(email)).thenReturn(false);
        UserService userService = new UserService(mockUserDao);

        boolean actual = userService.getUserEmailByEmail(email);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testGetUserByInputEmailPositive()  {
        UserDao mockUserDao = mock(UserDao.class);
        String email = "mahwishubham@gmail.com";

        User user = new User(1, "John", "Doe", "mahwishubham@gmail.com", "Password123!", "555-555-5000", "1");
        when(
                mockUserDao.getUserByInputEmail(email)
        ).thenReturn(user);

        UserService userService = new UserService(mockUserDao);
        User actual = userService.getUserByInputEmail(email);

        Assertions.assertEquals(user, actual);
    }

    @Test
    public void testGetUserByInputEmailNegative() {
        UserDao mockUserDao = mock(UserDao.class);
        String email = "mahwishubham@gmail.com";

//        User user = new User(1, "John", "Doe", "mahwishubham@gmail.com", "Password123!", "555-555-5000", "1");
        User user = null;
        when(
                mockUserDao.getUserByInputEmail(email)
        ).thenReturn(null);

        UserService userService = new UserService(mockUserDao);
        User actual = userService.getUserByInputEmail(email);

        Assertions.assertEquals(user, actual);
    }

    @Test
    public void testSendTokenPositive() {
        UserDao mockUserDao = mock(UserDao.class);
        int userId = 1;
        String email = "mahwishubham@gmail.com";
        String jwtToken = Jwts.builder()
                .claim("last_name", "Shubham")
                .claim("userId", 1)
                .claim("email", email).
                setSubject("Mahwish")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(5L, ChronoUnit.MINUTES))).compact();

        boolean expected = true;

        when(
                mockUserDao.sendToken(jwtToken, userId)
        ).thenReturn(true);

        UserService userService = new UserService(mockUserDao);
        boolean actual = userService.sendToken(jwtToken, userId);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testForgetPasswordPositive() {
        UserDao mockUserDao = mock(UserDao.class);
        JSONObject inputEmail = new JSONObject();
        inputEmail.put("email", "mazizi.c@gmail.com");

        // mock getUserEmailByEmail dao function
        when(mockUserDao.getUserEmailByEmail(inputEmail.getString("email"))).thenReturn(true);

        // mock getUserByInputEmail dao function
        User user = new User(1, "John", "Doe", "mazizi.c@gmail.com", "Password123!", "555-555-5000", "1");
        when(
                mockUserDao.getUserByInputEmail(inputEmail.getString("email"))
        ).thenReturn(user);

        // mock sendToken dao function
        when(
                mockUserDao.sendToken("jwtToken", 1)
        ).thenReturn(true);

        UserService userService = new UserService(mockUserDao);

        boolean expected = true;
        boolean actual = userService.forgetPassword(inputEmail);

        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testForgetPasswordNoEmailNegative() {
        UserDao mockUserDao = mock(UserDao.class);
        JSONObject inputEmail = new JSONObject();
        inputEmail.put("email", "mazizi.c@gmail.com");

        // mock getUserEmailByEmail dao function
        when(mockUserDao.getUserEmailByEmail(inputEmail.getString("email"))).thenThrow(new RuntimeException("The email pertaining to the account has been sent an email. Please check email for reset link."));

        // mock getUserByInputEmail dao function
        User user = new User(1, "John", "Doe", "mazizi.c@gmail.com", "Password123!", "555-555-5000", "1");
        when(
                mockUserDao.getUserByInputEmail(inputEmail.getString("email"))
        ).thenReturn(user);

        // mock sendToken dao function
        when(
                mockUserDao.sendToken("jwtToken", 1)
        ).thenReturn(true);

        UserService userService = new UserService(mockUserDao);

        String expected = "The email pertaining to the account has been sent an email. Please check email for reset link.";
        try {
            userService.forgetPassword(inputEmail);

        } catch (RuntimeException e) {
            // Act
            String actual = e.getMessage();

            // Assert
            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    public void testForgetPasswordSendTokenNegative() {
        UserDao mockUserDao = mock(UserDao.class);
        JSONObject inputEmail = new JSONObject();
        inputEmail.put("email", "mazizi.c@gmail.com");

        // mock getUserEmailByEmail dao function
        when(mockUserDao.getUserEmailByEmail(inputEmail.getString("email"))).thenReturn(true);

        // mock getUserByInputEmail dao function
        User user = new User(1, "John", "Doe", "mazizi.c@gmail.com", "Password123!", "555-555-5000", "1");
        when(
                mockUserDao.getUserByInputEmail(inputEmail.getString("email"))
        ).thenReturn(user);

        // mock sendToken dao function
        when(
                mockUserDao.sendToken("jwtToken", 1)
        ).thenThrow(new RuntimeException("The email pertaining to the account has been sent an email. Please check email for reset link."));

        UserService userService = new UserService(mockUserDao);

        String expected = "The email pertaining to the account has been sent an email. Please check email for reset link.";
        try {
            userService.forgetPassword(inputEmail);

        } catch (RuntimeException e) {
            // Act
            String actual = e.getMessage();

            // Assert
            Assertions.assertEquals(expected, actual);
        }
    }

    @Test
    public void testResetPasswordPositive() {
        UserDao mockUserDao = mock(UserDao.class);
        String email = "mahwishubham@gmail.com";
        String password = "Password123!";


        // mock getUserEmailByEmail dao function
        when(mockUserDao.getUserEmailByEmail(email)).thenReturn(true);

        // mock updatePassword dao function
        when(
                mockUserDao.updatePassword(email, password)
        ).thenReturn(true);

        // mock deleteToken dao function
        when(
                mockUserDao.deleteToken(email)
        ).thenReturn(true);


        UserService userService = new UserService(mockUserDao);

        boolean expected = true;
        boolean actual = userService.resetPassword(email, password);

        Assertions.assertEquals(expected, actual);

    }

    @Test
    public void testResetPasswordNoEmailNegative() {
        UserDao mockUserDao = mock(UserDao.class);
        String email = "mahwishubham@gmail.com";
        String password = "Password123!";

        // mock getUserEmailByEmail dao function
        when(mockUserDao.getUserEmailByEmail(email)).thenThrow(new RuntimeException("OOPS something went wrong. Reset Link Expired"));

        // mock updatePassword dao function
        when(
                mockUserDao.updatePassword(email, password)
        ).thenReturn(true);

        // mock deleteToken dao function
        when(
                mockUserDao.deleteToken(email)
        ).thenReturn(true);


        UserService userService = new UserService(mockUserDao);

        String expected = "OOPS something went wrong. Reset Link Expired";

        try {
            userService.resetPassword(email, password);

        } catch (RuntimeException e) {
            // Act
            String actual = e.getMessage();

            // Assert
            Assertions.assertEquals(expected, actual);
        }


    }

    @Test
    public void testResetPasswordFailedUpdatePwdNegative() {
        UserDao mockUserDao = mock(UserDao.class);
        String email = "mahwishubham@gmail.com";
        String password = "Password123!";

        // mock getUserEmailByEmail dao function
        when(mockUserDao.getUserEmailByEmail(email)).thenReturn(true);

        // mock updatePassword dao function
        when(
                mockUserDao.updatePassword(email, password)
        ).thenThrow(new RuntimeException("OOPS something went wrong. Reset Link Expired"));

        // mock deleteToken dao function
        when(
                mockUserDao.deleteToken(email)
        ).thenReturn(true);


        UserService userService = new UserService(mockUserDao);

        String expected = "OOPS something went wrong. Reset Link Expired";

        try {
            userService.resetPassword(email, password);

        } catch (RuntimeException e) {
            // Act
            String actual = e.getMessage();

            // Assert
            Assertions.assertEquals(expected, actual);
        }


    }

    @Test
    public void testResetPasswordDeleteTokenNegative() {
        UserDao mockUserDao = mock(UserDao.class);
        String email = "mahwishubham@gmail.com";
        String password = "Password123!";

        // mock getUserEmailByEmail dao function
        when(mockUserDao.getUserEmailByEmail(email)).thenReturn(true);

        // mock updatePassword dao function
        when(
                mockUserDao.updatePassword(email, password)
        ).thenReturn(true);

        // mock deleteToken dao function
        when(
                mockUserDao.deleteToken(email)
        ).thenThrow(new RuntimeException("OOPS something went wrong. Reset Link Expired"));


        UserService userService = new UserService(mockUserDao);

        String expected = "OOPS something went wrong. Reset Link Expired";

        try {
            userService.resetPassword(email, password);

        } catch (RuntimeException e) {
            // Act
            String actual = e.getMessage();

            // Assert
            Assertions.assertEquals(expected, actual);
        }


    }
}