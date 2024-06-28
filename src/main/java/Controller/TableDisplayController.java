/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.VaultEntry;
import java.sql.SQLException;
import java.util.List;
import Model.VaultTableModel;
import Model.UserModel;
import Service.PasswordEncryptionService;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author drago
 */
public class TableDisplayController {
    
    UserModel user;
    
    public VaultTableModel DisplayContentTable (UserModel user) throws SQLException {
        VaultTableModel model = new VaultTableModel();
        DatabaseUtil dbManager = new DatabaseUtil();
        List<VaultEntry> entries = dbManager.getAllEntries(user.getUsername()); // Retrieve data from the database
        model.setData(entries);         // Set the data in the table model
        return model;
    }
    
    public VaultTableModel DisplayDecodedContentTable (UserModel user) throws InvalidKeySpecException, Exception, SQLException {
        VaultTableModel model = new VaultTableModel();
        DatabaseUtil dbManager = new DatabaseUtil();
        List<VaultEntry> entries = dbManager.getAllDecodedEntries(user.getUsername()); // Retrieve data from the database
        List<VaultEntry> newEntries = ConvertEntries(entries,user.getSecretKey());
        model.setData(newEntries);         // Set the data in the table model
        return model;
    }
    
    public List<VaultEntry> ConvertEntries (List<VaultEntry> entries, SecretKeySpec secretKey) throws NoSuchAlgorithmException, InvalidKeySpecException, Exception {
        PasswordEncryptionService service = new PasswordEncryptionService();
        for (VaultEntry entry : entries) {
            String DecodedPassword = service.decryptPassword(secretKey,entry.getApplicationPassword());
            entry.setApplicationPasswordString(DecodedPassword);
        }
        return entries;
    }
}
