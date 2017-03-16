package stemmer;

import java.sql.*;

public class DataBase {
    // JDBC driver name and database URL
   String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   String DB_URL = "jdbc:mysql://localhost/STUDENTS";

   //  Database credentials
   String USER = "username";
   String PASS = "password";
   
   Connection conn = null;
   Statement stmt = null;
   
   void connect() 
   {
        try
        {
             //Register JDBC driver
             Class.forName(JDBC_DRIVER);

             //Open a connection
             System.out.println("Connecting to a selected database...");
             conn = DriverManager.getConnection(DB_URL, USER, PASS);
             System.out.println("Connected database successfully...");
         }
         
         catch(Exception e)
         {
            //Handle errors for Class.forName
            e.printStackTrace();
         }
         
    }
    void createTables()
    {
        try
        {
            //Execute a query
            System.out.println("Creating tables in given database...");
            stmt = conn.createStatement();

            String URLTable= "CREATE TABLE Websites " +
                          "(Link_ID BIGINT not NULL, " +
                          " Link VARCHAR(255), " + 
                          " Document LONGTEXT, " +
                          " PRIMARY KEY ( Link_ID ))"; 

            stmt.executeUpdate(URLTable);
            System.out.println("Created URL table in given database...");

            String KeywordsTable= "CREATE TABLE Keywords " +
                         "(Keyword_ID BIGINT not NULL, " +
                         " Keyword VARCHAR(255), " + 
                         " PRIMARY KEY ( Keyword_ID ))"; 

            stmt.executeUpdate(KeywordsTable);
            System.out.println("Created Keywords table in given database...");

            String InfoTable= "CREATE TABLE Information " +
                         "(FOREIGN KEY (Link_ID) REFERENCES 'Websites' (Link_ID)," +
                         " FOREIGN KEY (Keyword_ID) REFERENCES 'Keywords' (Keyword_ID)" + 
                         " Importance CHAR, " +
                         " Frequency DOUBLE, " + 
                         " PRIMARY KEY ( Link_ID , Keyword_ID ))"; 

            stmt.executeUpdate(InfoTable);
            System.out.println("Created Information table in given database...");
        }
        catch(SQLException se)
        {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    }
    
    void disconnect()
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
        
        System.out.println("Goodbye!");
    }
   
}
