package Controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import Model.ConnectionManager;
import Model.UserModel;
import Model.VaultEntry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;



/**
 *
 * @author drago
 */
public class DatabaseUtil {
    public static String hostname= "localhost";
    public static String instancename= "DESKTOP-0ETH67E";
    public static String username= "Admin";
    public static String password= "Pa$$w0rd";
    public static String databasename= "asg";
    
    private static Connection connection;
    // Static method to get a database connection
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load JDBC driver: " + e.getMessage());
        }
        
        
        String JDBC_URL = "jdbc:sqlserver://" + hostname + ":1433" 
                    + ";instance=" + instancename + ";databaseName=" + databasename + ";encrypt=true;trustServerCertificate=true";
        connection = DriverManager.getConnection(JDBC_URL,username, password);
        return DriverManager.getConnection(JDBC_URL, username, password);
        
    }
    
    public List<VaultEntry> getAllEntries(String username) throws SQLException {
        List<VaultEntry> entries = new ArrayList<>();
        String query = "SELECT * FROM " + username + "vault";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String applicationName = resultSet.getString("application_name");
                String applicationID = resultSet.getString("application_id");
                byte[] applicationPassword = resultSet.getBytes("application_password");
                java.sql.Timestamp timestamp = resultSet.getTimestamp("Date_created");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(timestamp);
                
                VaultEntry entry = new VaultEntry(applicationName, applicationID, formattedDate , applicationPassword);
                entries.add(entry);
            }
        }
        return entries;
    }
    
    public List<VaultEntry> getAllDecodedEntries(String username) throws SQLException {
        List<VaultEntry> entries = new ArrayList<>();
        String query = "SELECT * FROM " + username + "vault";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String applicationName = resultSet.getString("application_name");
                String applicationID = resultSet.getString("application_id");
                byte[] applicationPassword = resultSet.getBytes("application_password");
                java.sql.Timestamp timestamp = resultSet.getTimestamp("Date_Created");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateFormat.format(timestamp);
                
                VaultEntry entry = new VaultEntry(applicationName, applicationID,formattedDate ,applicationPassword);
                entries.add(entry);
            }
        }
        return entries;
    }
    
    public void DeleteRow (String username,String ApplicationName, String ApplicationID, String password) throws SQLException {
        String sql = "Delete FROM " + username+ "vault where application_name = ? and application_id = ? and application_password = ?";             
       try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, ApplicationName);
                preparedStatement.setString(2, ApplicationID);
                preparedStatement.setString(3, password);
                preparedStatement.executeUpdate();
            }
    }
    
    public UserModel getUser(String name) throws SQLException {
           // Execute a query to retrieve data
            String sql = "SELECT * FROM EAESUser where Username= ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                          return new UserModel(resultSet.getString("username"));
                    }
                }    
        } 
        return null;
        }
    
    public String getHashedPass(String name) throws SQLException {
            getConnection();
            String sql = "SELECT encryptedPassword FROM EAESUser WHERE Username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("encryptedPassword");
                    }
                }
            }
        return null;
    }

    public boolean ECCSaveDB(String username, String password , String key) throws SQLException, Exception {
        getConnection();
        String sql = "INSERT INTO EAESUser (username, encryptedPassword ,ECCpublickey) VALUES (?,?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, key);
                preparedStatement.executeUpdate();
            } 
            return true;
    }
    
    public String GetECCKey (String name) throws SQLException {
         getConnection();
            String sql = "SELECT ECCpublickey FROM EAESUser WHERE Username = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("ECCpublickey");
                    }
                }
            }
        return null;
    }
    
    public String getAESKey (String name) throws SQLException {
        getConnection();
        String sql = "SELECT keyValue FROM KeyVault WHERE Username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                          String CipherKey = resultSet.getString("keyValue");
                          return CipherKey;
                    }  
                } 
        }  
        return null;
    }

    public void storeCipherKey(String username, String CipherKey) throws SQLException {
        getConnection();
        String sql = "INSERT INTO KeyVault (keyValue, username) VALUES (?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, CipherKey);
                preparedStatement.setString(2, username);
                preparedStatement.executeUpdate();
            }    
        
    }

    public static void storeCredentails (String name, String ApplicationName, String ApplicationID, byte[] encryptedPassword) throws SQLException{
        getConnection();
        String sql = "INSERT INTO "+ name + "vault (application_name, application_id, application_password) VALUES(?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, ApplicationName);
                preparedStatement.setString(2, ApplicationID);
                preparedStatement.setBytes(3, encryptedPassword);
                preparedStatement.executeUpdate();
                }
    }

    public void DeleteRow1(String username, String date_created) throws SQLException {
        String sql = "Delete FROM " + username+ "vault where Date_Created > ? and Date_Created < ? ";             
       try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, date_created);
                String New_Date_Created = addSecondsToDate(date_created, 5);
                preparedStatement.setString(2, New_Date_Created);
                preparedStatement.executeUpdate();
            }
    }
    
    public static String addSecondsToDate(String dateStr, int secondsToAdd) {
        // Define the date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            // Parse the date string into a Date object
            date = sdf.parse(dateStr);
        } catch ( Exception e) {
        }
        

        // Create a Calendar object and set the time to the Date object
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // Add the specified number of seconds
        calendar.add(Calendar.SECOND, secondsToAdd);

        // Convert the Calendar object back to a Date object
        Date newDate = calendar.getTime();

        // Format the new Date object back to a string
        return sdf.format(newDate);
    }
}
