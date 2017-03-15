package stopWords;

import java.sql.*;

public class DataBase {
    // JDBC driver name and database URL
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   static final String DB_URL = "jdbc:mysql://localhost/STUDENTS";

   //  Database credentials
   static final String USER = "username";
   static final String PASS = "password";
   
   public static void main(String[] args) 
   {
        Connection conn = null;
        Statement stmt = null;
        try
        {
             //Register JDBC driver
             Class.forName("com.mysql.jdbc.Driver");

             //Open a connection
             System.out.println("Connecting to a selected database...");
             conn = DriverManager.getConnection(DB_URL, USER, PASS);
             System.out.println("Connected database successfully...");

             //Execute a query
             System.out.println("Creating table in given database...");
             stmt = conn.createStatement();

             String sql = "CREATE TABLE REGISTRATION " +
                          "(id INTEGER not NULL, " +
                          " first VARCHAR(255), " + 
                          " last VARCHAR(255), " + 
                          " age INTEGER, " + 
                          " PRIMARY KEY ( id ))"; 

             stmt.executeUpdate(sql);
             System.out.println("Created table in given database...");
         }
         catch(SQLException se)
         {
            //Handle errors for JDBC
            se.printStackTrace();
         }
         catch(Exception e)
         {
            //Handle errors for Class.forName
            e.printStackTrace();
         }
         finally //block used to close resources
         {
              try
              {
                 if(stmt!=null)
                    conn.close();
              }
              catch(SQLException se)
              {

              }
              try
              {
                 if(conn!=null)
                    conn.close();
              }
              catch(SQLException se)
              {

              }
         }
        
         System.out.println("Goodbye!");
    }
   
}
