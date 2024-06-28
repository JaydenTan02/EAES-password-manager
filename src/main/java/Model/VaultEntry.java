/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Service.PasswordEncryptionService;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author drago
 */
public class VaultEntry {

    private String applicationName;
    private String applicationID;
    private String applicationPasswordString;
    private byte[] applicationPassword;
    private String DateCreated;
    
    public VaultEntry(String applicationName, String applicationID, String DateCreated , byte[] applicationPassword) {
        this.applicationName = applicationName;
        this.applicationID = applicationID;
        this.DateCreated = DateCreated;
        this.applicationPassword = applicationPassword;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }
    
    public byte[] getApplicationPassword() {
        return applicationPassword;
    }

    public void setApplicationPassword(byte[] applicationPassword) {
        this.applicationPassword = applicationPassword;
    }
    
    public String getApplicationPasswordString() {
        return applicationPasswordString;
    }

    public void setApplicationPasswordString(String applicationPasswordString) {
        this.applicationPasswordString = applicationPasswordString;
    }
    
    public String getDateCreated () {
        return DateCreated;
    }
    
    public void setDateCreated (String DateCreated) {
        this.DateCreated = DateCreated;
    }
}