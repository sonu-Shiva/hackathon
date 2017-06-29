package com.test.automation;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class Model {
public static File file;	
public static void triggerSelenium(int usecase_id,String browser){
	System.out.println("in model");
        Statement st=null;
        Statement st1=null;
        Statement st2=null;
	ResultSet rs=null;
        ResultSet rs1=null;
        String usecaseName=null;
	ExtentReports eReport;
	ExtentTest testReport;
	
//db connection
	Helper h=new Helper();
	Connection c=h.controller();
	try {
	    st1=c.createStatement();
	    String query1 = "SELECT uc_name FROM public.\"Usecase\" Where prod_id="+1+" and uc_id="+usecase_id+"; ";
	    System.out.println("after connection1");
	    rs1=st1.executeQuery(query1);
	    while(rs1.next()){
	    	usecaseName=rs1.getString("uc_name");
	    	System.out.println(usecaseName);
	    }
	    
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

String dateVar = new Model().getDateTime();
	System.out.println(dateVar);
	file = new File(Property.getPropertyValue(configPptPath,"REPORTFOLDER")+dateVar+usecase_id);
	file.mkdir();
	eReport=new ExtentReports(Property.getPropertyValue(configPptPath,"REPORTFOLDER")+dateVar+usecase_id+"//"+usecaseName+usecase_id+".html");

		
	testReport=eReport.startTest(usecaseName);
	
     try {
    	 System.out.println("after connection");
    	st=c.createStatement(); 
    	String query = "SELECT usecase_id, description, action, locators, element_identifier, element_value, seq_id FROM public.\"Actions_Table\"  ORDER BY SEQ_ID;";
    	System.out.println("after connection1");
    	rs = st.executeQuery(query);
    	
    	if(browser.equalsIgnoreCase("firefox")){
			driver = new FirefoxDriver();
    	}
		else if(browser.equalsIgnoreCase("ie")){
			System.setProperty("webdriver.ie.driver", Property.getPropertyValue(configPptPath,"IEDRIVERPATH"));
			driver = new InternetExplorerDriver();
		}
		else if(browser.equalsIgnoreCase("Chrome")){
			System.setProperty("webdriver.chrome.driver",Property.getPropertyValue(configPptPath,"CHROMEDRIVERPATH"));
			driver = new ChromeDriver();
		}
    	
    	
//     	System.setProperty("webdriver.chrome.driver","C://Users//sandeepraju//Desktop//Workplace//CodeLessAutomation//drivers//chromedriver.exe");
//		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS) ;
		
		
		ActionClass ac=new ActionClass();
		while(rs.next()){
			System.out.println("after connection1");
			String desc=rs.getString("description");
			String action=rs.getString("action");
			String locators=rs.getString("locators");
			String locatorName=rs.getString("element_identifier");
			String testData=rs.getString("element_value");
			System.out.println(rs.getString("action"));
			String msg=desc+action+locators+locatorName+testData;
			testReport.log(LogStatus.INFO,msg);
			ac.callActionMethods(driver,action,locators,locatorName,testData,c,testReport);
			System.out.println("going out of while loop");
			
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
     finally{
    	 if(c!=null){
    		 try {
				c.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	 }
    	 if(st!=null){
    		 try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	 }
    	 if(rs!=null){
 				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	 }
    	 System.out.println("in finally");
    	 eReport.flush(); 
    	 System.out.println("out finally");
     }
     
   
}

public String getDateTime(){
 	
	 // Create object of SimpleDateFormat class and decide the format
	 DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy HH.mm.ss");
	 //get current date time with Date()
	 Date date = new Date();
	 
	 // Now format the date
	 String currentDate= dateFormat.format(date);
	 
//
	 System.out.println("Current date and time is " + currentDate );
	return currentDate;
	
}
}
