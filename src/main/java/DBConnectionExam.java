/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author drago
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;



public class DBConnectionExam {
        public static void main(String[] args) throws ClassNotFoundException, SQLException{
            try{      
            Connection conn = Controller.DatabaseUtil.getConnection();

            Statement statement = conn.createStatement();
            
            String query = "Delete from DemoUser where username='Admin1'";
            statement.executeQuery(query);

            // Execute a query to retrieve data
//            String query = "SELECT * FROM Users_detail";

//            ResultSet resultSet = statement.executeQuery(query);
//            
//            while (resultSet.next()) {
//                // Assuming you have a column named "column_name" in your_table
//                String columnNameValue = resultSet.getString("name");
//                System.out.println("Column Name: " + columnNameValue);
//                var columnPasswordValue = resultSet.getString("password");
//                System.out.println("Column Name: " + columnPasswordValue);
//            }
            
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
