package webcrawler;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {
    // JDBC driver name and database URL
   String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
   String DB_URL = "jdbc:mysql://localhost:3306/mysql?zeroDateTimeBehavior=convertToNull";

   //  Database credentials
   String USER = "root";
   String PASS = "EngySamyElShorbagy";
   
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
             //PreparedStatement ps = conn.prepareStatement("CREATE DATABASE SearchEngine");
             //int result = ps.executeUpdate();
             //System.out.println("created database successfully...");
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

        
            
          String URLTable= "CREATE TABLE SearchEngine.Websites " +
                          "(Link_ID BIGINT NOT NULL AUTO_INCREMENT, " +
                          " Link VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL UNIQUE , " + 
                          " Document LONGTEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL , " +
                          " PRIMARY KEY ( Link_ID ))"; 


           PreparedStatement ps1 = conn.prepareStatement(URLTable);
           ps1.executeUpdate();

           System.out.println("Created URL table in given database...");
            

           String KeywordsTable= "CREATE TABLE SearchEngine.Keywords " +
                         "(Keyword_ID BIGINT NOT NULL AUTO_INCREMENT, " +
                         " Keyword VARCHAR(255) UNIQUE, " + 
                         " PRIMARY KEY ( Keyword_ID ))"; 
           String AlterKeywords = "ALTER TABLE SearchEngine.Keywords AUTO_INCREMENT = 1";
           
           PreparedStatement ps3 = conn.prepareStatement(KeywordsTable);
           ps3.executeUpdate();
           PreparedStatement ps5 = conn.prepareStatement(AlterKeywords);
           ps5.executeUpdate();

           System.out.println("Created Keywords table in given database...");

          
           String InfoTable= "CREATE TABLE SearchEngine.Information " +
                         "(Link_ID BIGINT not NULL," +
                         " Keyword_ID BIGINT not NULL," +
                         " FOREIGN KEY (Link_ID) REFERENCES Websites (Link_ID)," +
                         " FOREIGN KEY (Keyword_ID) REFERENCES Keywords (Keyword_ID)," + 
                         " Importance CHAR, " +
                         " Frequency DOUBLE, " + 
                         " PRIMARY KEY ( Link_ID , Keyword_ID ))"; 
           
            PreparedStatement ps6 = conn.prepareStatement(InfoTable);
            ps6.executeUpdate();
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
   ///////Insert into database///////
    void InsertURL (String URL, String Doc)
    {
       String Query= "INSERT INTO SearchEngine.Websites " +
                     "(Link, Document)" +
                     "VALUES" +
                     "(?, ?);"; 
       try 
       {
         PreparedStatement ps = conn.prepareStatement(Query);
         ps.setString(1, URL);
         ps.setString(2, Doc);
         ps.executeUpdate();
       } 
       catch (SQLException ex) 
       {
         Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
       }
        System.out.println("Inserted URL successfully...");
    }
    
    void InsertKeyword (String Keyword)
    {
       String Query= "INSERT INTO SearchEngine.Keywords " +
                     "(Keyword)" +
                     "VALUES" +
                     "(?);"; 
       try 
       {
         PreparedStatement ps = conn.prepareStatement(Query);
         ps.setString(1, Keyword);
         ps.executeUpdate();
       } 
       catch (SQLException ex) 
       {
         Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
       }
        System.out.println("Inserted Keyword successfully...");
    }
    
    void Insertinfo (Long LnkID, Long KeyID, char Imp, Double Freq)
    {
       String Query= "INSERT INTO SearchEngine.Information " +
                     "(Link_ID, Keyword_ID, Importance, Frequency)" +
                     "VALUES" +
                     "('"+LnkID+"', '"+KeyID+"', '"+Imp+"','"+Freq+"');"; 
       try 
       {
         PreparedStatement ps = conn.prepareStatement(Query);
         ps.executeUpdate();
       } 
       catch (SQLException ex) 
       {
         Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
       }
        System.out.println("Inserted Info successfully...");
    }
    
    Long SelectID () throws SQLException
    {
       String Query= "SELECT MAX(Keyword_ID) FROM SearchEngine.Keywords;" ;
       ResultSet rs = null;            
       try 
       {
         Statement stmt = conn.createStatement();
         rs = stmt.executeQuery(Query);

       } 
       catch (SQLException ex) 
       {
         Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
       }
        System.out.println("Selected Info Successfully....");
        Long val = null;
        
        if(rs.next())
        {
            val =  ((Number) rs.getObject(1)).longValue();
        }
        return val;
    }
    
    String SelectURL () throws SQLException
    {
       String Query= "SELECT Document FROM SearchEngine.Websites;" ;
       ResultSet rs = null;            
       try 
       {
         Statement stmt = conn.createStatement();
         rs = stmt.executeQuery(Query);

       } 
       catch (SQLException ex) 
       {
         Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
       }
        System.out.println("URL content retrieved Successfully....");
        String val = null;
        
        if(rs.next())
        {
            val = rs.getString("Document");
        }
        return val;
    }
    
    boolean SearchURL (String url) 
    {
       String Query= "SELECT Link_ID FROM SearchEngine.Websites WHERE Link=" + 
                     "('"+url+"');"; 
       
       ResultSet rs = null;  
       int count = 0; 
       try 
       {
            Statement st = conn.createStatement();
            rs = st.executeQuery(Query);
            
            //PreparedStatement ps = conn.prepareStatement(Query);
            //ps.setString(1, url);
            //rs=ps.executeQuery(Query);
            
             while (rs.next()) {
               ++count;
               // Get data from the current row and use it
           } 
       } 
       
       /*try 
       {
         PreparedStatement ps = conn.prepareStatement(Query);
         ps.setString(1, url);
         ps.executeUpdate();
       } */
       
       catch (SQLException ex) 
       {
         Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
       }
       
       return count == 0; 
    }
}