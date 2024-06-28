/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Service.PasswordEncryptionService;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Model.UserModel;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

/**
 *
 * @author drago
 */
public class Store_Password_Controller {
    private final PasswordEncryptionService passwordEncryptionService;
    private UserModel User; 

    public Store_Password_Controller() throws InvalidKeySpecException {
        PasswordEncryptionService service = null;
        try {
            service = new PasswordEncryptionService();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Store_Password_Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.passwordEncryptionService = service;
    }
    
    public void setUser (UserModel existingUser) {
        this.User = existingUser;
    }
    
    public void handleEncRequest(String ApplicationName, String ApplicationID, String password) throws SQLException, Exception {
            passwordEncryptionService.setSecretKey(User.getSecretKey());
            String name = User.getUsername();
            byte[] encryptedPassword = passwordEncryptionService.encryptPassword(password);   
            DatabaseUtil.storeCredentails(name,ApplicationName, ApplicationID, encryptedPassword);
    }

}

