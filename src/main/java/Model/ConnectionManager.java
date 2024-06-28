/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author drago
 */
public class ConnectionManager {
    private static final String PROPERTIES_FILE_NAME = "D:/db.properties";
    private static Properties properties;

    static {
        properties = new Properties();
       
        String basePath;
        //            basePath = getBasePathP();
//            String propertiesFilePath = basePath + File.separator + PROPERTIES_FILE_NAME;
try (FileInputStream input = new FileInputStream(PROPERTIES_FILE_NAME )) {
    properties.load(input);
} catch (IOException e) {
    e.printStackTrace();
}
        
        
    }
    
     private static String getBasePathP() throws IOException {
        // Get the directory where the Java class files are located
        String basePath = ConnectionManager.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        // Remove the "file:" prefix if present
        if (basePath.startsWith("file:")) {
            basePath = basePath.substring(5);
        }
        // Decode the URL-encoded path (if encoded)
        basePath = java.net.URLDecoder.decode(basePath, "UTF-8");
        // Get the parent directory
        basePath = new File(basePath).getParent();
        return basePath;
    }
    
    public static String getDatabaseHostname() {
        return properties.getProperty("db.hostname");
    }

    public static String getDatabaseInstanceName() {
        return properties.getProperty("db.sqlinstancename");
    }

    public static String getDatabaseUsername() {
        return properties.getProperty("db.username");
    }

    public static String getDatabasePassword() {
        return properties.getProperty("db.password");
    }

    public static String getDatabaseName() {
        return properties.getProperty("db.sqldatabase");
    }
    
    public static String getBashPath() {
        try {
            return getBasePathP();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
