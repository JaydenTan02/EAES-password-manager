/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.PSECP256K1;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tuweni.crypto.SECP256K1.SecretKey;
import org.apache.tuweni.crypto.SECP256K1.PublicKey.*;
import javax.crypto.spec.SecretKeySpec;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.bytes.Bytes;
import static org.apache.tuweni.bytes.Bytes.wrap;
import org.apache.tuweni.crypto.SECP256K1.PublicKey;
import static org.apache.tuweni.crypto.SECP256K1.PublicKey.fromHexString;
import static org.apache.tuweni.crypto.SECP256K1.decrypt;
import static org.apache.tuweni.crypto.SECP256K1.encrypt;

/**
 *
 * @author drago
 */
public class SECP_Controller {
    PSECP256K1 ECC;
    String username;
    PublicKey key;
    
    public SECP_Controller () {
        this.ECC = new PSECP256K1();
    }

    public void generatePublicKey(String text) {
        this.key = ECC.generateKeyPair(text);
    }
    
    public SecretKey getSecretKey(String text) {
        return ECC.generateSecrectKey(text);
    }
    
    public PublicKey getPublicKey(){
        return this.key;
    }
    
    public void setUsername (String username) {
        this.username = username;
    }
    
    public boolean savetoDB (String Password) throws SQLException, Exception {
        DatabaseUtil DB = new DatabaseUtil();
        if(DB.ECCSaveDB (username,Password,key.toHexString())){
            return true;
        } else {
            throw new Exception("Registration account failed");
        }
    }
      
    public Bytes encryptKey (PublicKey p1,byte[] key){
        Bytes CipherKey = encrypt(p1,wrap(key));
        return CipherKey;
    }
    
    public SecretKeySpec decrptKey (String input,String username) throws SQLException{
        DatabaseUtil DB = new DatabaseUtil();
        String forDecrypt;
        forDecrypt = DB.getAESKey (username);
        Bytes plainKey = decrypt(getSecretKey(input),Bytes.fromHexString(forDecrypt));
        SecretKeySpec keySpec = new SecretKeySpec(plainKey.toArray(), "AES");
        return keySpec;        
    }
    
    public boolean checkMasterPasswordEqual(String username, String text) {
        DatabaseUtil DB = new DatabaseUtil();
        
        PublicKey inputPublic = ECC.generateKeyPair(text);
        try {
            String DBPublic = DB.GetECCKey(username);
            return inputPublic.equals(fromHexString(DBPublic));
        } catch (SQLException ex) {
            return false;
        }
    }

}
