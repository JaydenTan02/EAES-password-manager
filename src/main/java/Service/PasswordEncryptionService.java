/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import javax.crypto.Cipher;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
/**
 *
 * @author drago
 */

public class PasswordEncryptionService {

    private SecretKeySpec secretKey;

    public PasswordEncryptionService() throws NoSuchAlgorithmException, InvalidKeySpecException {

    }
    
    public void setSecretKey (SecretKeySpec secretKey) {
        this.secretKey = secretKey;
    }
    
    public SecretKeySpec getSecretKey () {
        return secretKey;
    }
    
    public byte[] generateSecretKey(String text) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        
        KeySpec spec = new PBEKeySpec(text.toCharArray(), salt, 600000, 256); // AES-256 
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] key = f.generateSecret(spec).getEncoded();

        return key;
    }

    public byte[] encryptPassword(String password) throws Exception {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        
        byte[] ivBytes = new byte[16];
        random.nextBytes(ivBytes);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);
        
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encValue = c.doFinal(password.getBytes());
        
        byte[] finalCiphertext = new byte[encValue.length+2*16];
        System.arraycopy(ivBytes, 0, finalCiphertext, 0, 16);
        System.arraycopy(salt, 0, finalCiphertext, 16, 16);
        System.arraycopy(encValue, 0, finalCiphertext, 32, encValue.length);
        
        return finalCiphertext;
    }
        
    public String decryptPassword(SecretKeySpec keySpec, byte[] finalCiphertext) throws NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{

        // Extract IV, salt, and encrypted value from finalCiphertext
        byte[] ivBytes = Arrays.copyOfRange(finalCiphertext, 0, 16);
        byte[] salt = Arrays.copyOfRange(finalCiphertext, 16, 32);
        byte[] encryptedValue = Arrays.copyOfRange(finalCiphertext, 32, finalCiphertext.length);
       
        // Decrypt the ciphertext
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(ivBytes));
        byte[] decryptedValue = c.doFinal(encryptedValue);

        // Convert decrypted byte array to String
        String decryptedString = new String(decryptedValue);
     
        return decryptedString;
    }

}

