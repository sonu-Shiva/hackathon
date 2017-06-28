package com.test.automation;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
public class jd {
   // JDBC driver name and database URL
   static String DB_URL = "jdbc:postgresql://hackathon.cuuuwyhnfkj0.us-west-2.rds.amazonaws.com:5432/hackathon_db";
   //  Database credentials
   String USER = "hackathon";
   String PASS = "hackathon";
   public static void jdMethod(int id) {
   Connection conn = null;
   Statement stmt = null;
   try{
      //STEP 2: Register JDBC driver
      Class.forName("org.postgresql.Driver");
      //STEP 3: Open a connection
      System.out.println("Connecting to a selected database...");
      conn = DriverManager.getConnection(DB_URL, "hackathon", "hackathon");
      System.out.println("Connected database successfully...");
//      //STEP 4: Execute a query
      System.out.println("Inserting records into the table...");
      stmt = conn.createStatement();
      System.out.println("Working Directory = " + System.getProperty("user.dir"));
      StringBuilder contentBuilder = new StringBuilder();
      try {
          BufferedReader in = new BufferedReader(new FileReader("C://Users//sandeepraju//Desktop//Workplace//CodeLessAutomation//Reports//ESF.html"));
          String str;
          while ((str = in.readLine()) != null) {
              contentBuilder.append(str);
          }
          in.close();
      } catch (IOException e) {
      }
      String content = contentBuilder.toString();
      String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
      PreparedStatement pstmt = 
          conn.prepareStatement("INSERT INTO test_cases_reports(time,report,use_case_id) VALUES (?,?,?)");
      pstmt.setString(1, timeStamp);
      pstmt.setString(2, content);
      pstmt.setInt(3,id);
      // this is your html string from step #1
      pstmt.executeUpdate();
      pstmt.close();
   }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
   }catch(Exception e){
      //Handle errors for Class.forName
      e.printStackTrace();
   }finally{
      //finally block used to close resources
      try{
         if(stmt!=null)
            conn.close();
      }catch(SQLException se){
      }// do nothing
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }//end finally try
   }//end try
   System.out.println("Goodbye!");
}//end main
}//end JDBCExample
