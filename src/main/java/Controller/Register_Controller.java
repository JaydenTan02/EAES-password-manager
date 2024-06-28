/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import Service.PasswordEncryptionService;
import java.security.spec.InvalidKeySpecException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import javax.crypto.spec.SecretKeySpec;
import org.apache.tuweni.bytes.Bytes;
import static org.apache.tuweni.bytes.Bytes.wrap;
import org.apache.tuweni.crypto.SECP256K1;
import org.apache.tuweni.crypto.SECP256K1.PublicKey;
import static org.apache.tuweni.crypto.SECP256K1.decrypt;


/**
 *
 * @author drago
 */
public class Register_Controller {
   
    public static String registerUser(String username, String password, String input){
        try {
            // Hash the password before storing it
            String hashedPassword = hashPassword(password);
            DatabaseUtil DB = new DatabaseUtil();
            SECP_Controller ECC = new SECP_Controller();
            ECC.generatePublicKey(input);
            DB.ECCSaveDB(username, hashedPassword, ECC.getPublicKey().toHexString());
            String TextGenAES = Conca(username,password);
            byte[] key = createAESKeyForUser(TextGenAES);
            Bytes CipherKey = ECC.encryptKey(ECC.getPublicKey(), key);
            DB.storeCipherKey(username,CipherKey.toHexString());
            createTableForUser(username);
            return("User registered successfully.");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ae) {
            return (ae.getMessage());
        } catch (SQLException ex) {
            if (ex.getSQLState().startsWith("23")) {
                return("Username is used.");
            } else {
                return(ex.getMessage());
           }
        }catch (Exception e) {
            return(e.getMessage()); // Print the error message from the caught exception
        }
    }

    private static byte[] createAESKeyForUser(String username) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PasswordEncryptionService service =  new PasswordEncryptionService();
        byte[] KeyEncoded = service.generateSecretKey(username); 
        return KeyEncoded;
    }

    public static String hashPassword(String password) throws NoSuchAlgorithmException, Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        if(password.isBlank()){
            throw new Exception ("Please enter your password");
        }
        byte[] hashedBytes = md.digest(password.getBytes());
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : hashedBytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }

    private static void createTableForUser(String username) throws SQLException {
        try (Connection connection = Controller.DatabaseUtil.getConnection()){
            String query = "CREATE TABLE " + username + "vault (application_name VARCHAR(255),application_id VARCHAR(50),application_password VARbinary(256), Date_Created datetime DEFAULT GETDATE());";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.executeUpdate();
            }
        }
    }

    private static String Conca (String First,String Second) {
        
        Random rand = new Random();
        int randomNumber = rand.nextInt(8);
        String firstHalf = Second.substring(0, randomNumber);
        String secondHalf = Second.substring(randomNumber);
        String text1 = firstHalf + First ;
        String text = text1+ secondHalf;
        System.out.println(text);
        return text;
    }


}