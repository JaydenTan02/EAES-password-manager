/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import Model.UserModel;

/**
 *
 * @author drago
 */
public class Login_Controller {
     
    public static UserModel login(String username, char[] passwordChar) throws NoSuchAlgorithmException, SQLException {
        DatabaseUtil dbManager = new DatabaseUtil();
            // Hash the provided password
            
            String password = arrayToString(passwordChar);
            String hashedPassword = hashPassword(password);

            // Fetch the hashed password from the database
            String storedHashedPassword = dbManager.getHashedPass(username);
            // Compare the hashed passwords
            if (storedHashedPassword != null && storedHashedPassword.equals(hashedPassword)) {
                return dbManager.getUser(username);
            } else {
                return null;
            }

    }

    
    private static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes());
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hashedBytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }
    
    private static String arrayToString(char[] charArray) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : charArray) {
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }
}

