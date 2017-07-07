package com.test.automation;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class Model{
public static File file;
public synchronized static void triggerSelenium(String ucid,String browser){
	
	
    Statement stmt=null;
    Statement stmt1=null;
    Statement stmt2=null;
    PreparedStatement pStmt=null;
	ResultSet rs=null;
	ResultSet rs1=null;
	ResultSet rs2=null;
	String useCaseName=null;
	WebDriver driver=null;
	ExtentReports eReport = null;
	ExtentTest testReport;
	String dateVar = null;
	String path = null;
	List<String> ucidl=new ArrayList<String>();
	int usecase_id = 0;
	
	//Browser triggereded on browser var value
    if(browser.equalsIgnoreCase("firefox")){
	driver = new FirefoxDriver();
	}
	else if(browser.equalsIgnoreCase("ie")){
		System.setProperty("webdriver.ie.driver",Property.getPropertyValue("IEDRIVERPATH"));
		driver = new InternetExplorerDriver();
	}
	else if(browser.equalsIgnoreCase("Chrome")){
		System.setProperty("webdriver.chrome.driver",Property.getPropertyValue("CHROMEDRIVERPATH"));
		driver = new ChromeDriver();
	}
	
	//db connection
	Helper h=new Helper();
	Connection c=h.controller();
	
	//getting usecase id
	String[] useCaseSplit=ucid.split("=");
	
	if(useCaseSplit[0].equals("usecase_id")){
		String[] stl=useCaseSplit[1].split(",");
		 for(int i=0;i<stl.length;i++)
			 ucidl.add(stl[i]);
	}
	
	if(useCaseSplit[0].equals("job_id")){
		   //get ucid's form job id and assign to ucidl variable
		try {
			stmt2=c.createStatement();
			System.out.println(useCaseSplit[1]);
	        String ucQuery="select usecase_id from qa_app_jobusecases where job_id="+useCaseSplit[1]+";";
	        rs2=stmt2.executeQuery(ucQuery);
	        int count=0;
	        System.out.println(rs2);
	        while(rs2.next()){
	        	
	        	System.out.println("usecase id: "+rs2.getString("usecase_id"));
	        	ucidl.add(Integer.toString(rs2.getInt("usecase_id")));
		    	count++;
		    }
		    } catch (SQLException e) {
			   e.printStackTrace();
		    }
		}
	
	//no of usecaseid : no of times selenium performs actions
	try {
		for(String id:ucidl){
			usecase_id=Integer.parseInt(id);
		stmt1=c.createStatement();
		String query1 = "SELECT use_case_name FROM qa_app_usecase Where id="+usecase_id+"; ";
	    rs1=stmt1.executeQuery(query1);
	   
	    while(rs1.next()){
	    	useCaseName=rs1.getString("use_case_name");
	    	
	    }
	    
		dateVar = new Model().getDateTime();
		
		
		//for each usecase new folder for reports is created and html report will be present in it
		file = new File(Property.getPropertyValue("REPORTFOLDER")+dateVar+usecase_id);
		file.mkdir();
		
		eReport=new ExtentReports(Property.getPropertyValue("REPORTFOLDER")+dateVar+usecase_id+"//"+useCaseName+usecase_id+".html");

		path=Property.getPropertyValue("REPORTFOLDER")+dateVar+usecase_id+"//"+useCaseName+usecase_id+".html";
		testReport=eReport.startTest(useCaseName);
		    
	    	stmt=c.createStatement(); 
	    	String query = "Select description,action,locators,element_identifier,element_value from qa_app_action where use_case_id="+usecase_id+" Order by seq";
	    	
	    	rs = stmt.executeQuery(query);
	    	
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS) ;
			
			
			ActionClass ac=new ActionClass();
			while(rs.next()){
				
				String desc=rs.getString("description");
				String action=rs.getString("action");
				String locators=rs.getString("locators");
				String locatorName=rs.getString("element_identifier");
				String testData=rs.getString("element_value");
				String msg=desc+action+locators+locatorName+testData;
				testReport.log(LogStatus.INFO,msg);
				ac.callActionMethods(driver,action,locators,locatorName,testData,c,testReport);
				
				
			}
			eReport.flush(); 
		}  
	} catch (SQLException e1) {
		e1.printStackTrace();
	}
	 
     finally{
    	 ucidl=null;
    	 try {
     		// inserting file pathe, usecase id and timstamp to db
    		 driver.close();
 			 pStmt=c.prepareStatement("INSERT INTO qa_app_reports (report,use_case_id,time) VALUES (?,?,?)");
 			 pStmt.setString(1,path);
 			 pStmt.setInt(2,usecase_id);
 			 pStmt.setString(3,dateVar); 
 			 pStmt.executeUpdate();
 		     pStmt.close();
 		} catch (SQLException e) {
 			e.printStackTrace();
 		}
    	 
    	 if(c!=null){
    		 try {
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	 }
    	 if(stmt!=null){
    		 try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		 if(stmt1!=null){
        		 try {
    				stmt.close();
    			} catch (SQLException e) {
    				e.printStackTrace();
    			}
    	 }
    	 if(rs!=null){
 				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
 				 if(rs1!=null){
 	 				try {
 						rs.close();
 					} catch (SQLException e) {
 						e.printStackTrace();
 					}		
    	 }
    	 
     }
    	 }
     }
     
   
}

public String getDateTime(){
 	
	 // Create object of SimpleDateFormat class and decide the format
	 DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy HH.mm.ss");
	 Date date = new Date();
	 String currentDate= dateFormat.format(date);
	 return currentDate;
	
}
}
