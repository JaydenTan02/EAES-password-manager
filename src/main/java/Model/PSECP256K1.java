/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.math.BigInteger;
import java.security.Security;
import org.apache.tuweni.crypto.SECP256K1;
import org.apache.tuweni.crypto.SECP256K1.PublicKey;
import static org.apache.tuweni.crypto.SECP256K1.PublicKey.fromSecretKey;
import org.apache.tuweni.crypto.SECP256K1.SecretKey;
import static org.apache.tuweni.crypto.SECP256K1.SecretKey.fromInteger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author drago
 */
public class PSECP256K1 {
    
    public PSECP256K1 () {
        Security.addProvider(new BouncyCastleProvider()); 
    }
    
    // generateSecretKey // 
    public PublicKey generateKeyPair(String input) {
        String bigInt = convertToInt(input);
        BigInteger BigInt = new BigInteger(bigInt);
        
        SecretKey s1 = fromInteger(BigInt); 
        PublicKey p1 = fromSecretKey(s1);
        
        return p1;
    }
    
    private String convertToInt (String input) {
        String intFromAlp = "";
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            int ascii = (int) c;
            intFromAlp = intFromAlp + String.valueOf(ascii);
        }
        return intFromAlp;
    }
    
    public SecretKey generateSecrectKey(String input) {
        String bigInt = convertToInt(input);
        BigInteger BigInt = new BigInteger(bigInt);
        
        SecretKey s1 = fromInteger(BigInt);        
        return s1;
    }
    
    public boolean compareKeyPair(SecretKey s1,PublicKey p1) throws Exception {
        PublicKey p2 = fromSecretKey(s1);
        if (p2.equals(p1)){
            return true;
        } else {
            throw new Exception ("False Master Key");
        }
    }
}
