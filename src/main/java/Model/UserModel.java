/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Controller.DatabaseUtil;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 *
 * @author drago
 */
public class UserModel {
    
    private int UserId;
    private String username;
    private String password;
    private SecretKeySpec key;
    private boolean MasterKeyProvided;

    // Constructor
    public UserModel(int UserId, String username, String password,SecretKeySpec key) {
        this.UserId = UserId;
        this.username = username;
        this.password = password;
        this.key = key;
    }

    public UserModel(String username) {
        this.username = username;
    }
    
    public void setAESKey (SecretKeySpec key){
        this.key = key;
    }
    
    // Getter methods
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public SecretKeySpec getSecretKey() {
        return key;
    }
    
    public void setMasterKeyProvided () {
        this.MasterKeyProvided = true;
    }
    
    public boolean getMasterKeyProvided () {
        return MasterKeyProvided;
    }
          
   
}
