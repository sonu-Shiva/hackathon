package com.test.automation;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
public static void triggerSelenium(int usecase_id,String browser){
	System.out.println("in model");
    Statement st=null;
	ResultSet rs=null;
	ExtentReports eReport;
	ExtentTest testReport;
	Date date = new Date();
	String dateVar=date.toString();
	//File f=new File("Reports");
	 eReport=new ExtentReports("C://Users//sandeepraju//Desktop//Workplace//QA-BOT//reports//ESFREPORT.html");
	 testReport=eReport.startTest("UseCase");
	WebDriver driver=null;
	Helper h=new Helper();
	
	Connection c=h.controller();
     try {
    	 System.out.println("after connection");
    	st=c.createStatement(); //Select description,action,locators,element_identifier,element_value from test_cases_action where use_case_id="+usecase_id+" Order by id
    	String query = "SELECT usecase_id, description, action, locators, element_identifier, element_value, seq_id FROM public.\"Actions_Table\"  ORDER BY SEQ_ID;";
    	System.out.println("after connection1");
    	rs = st.executeQuery(query);
    	
    	if(browser.equalsIgnoreCase("firefox")){
			driver = new FirefoxDriver();
    	}
		else if(browser.equalsIgnoreCase("ie")){
			System.setProperty("webdriver.ie.driver", "C://Users//user//Documents//IEDriverServer.exe");
			driver = new InternetExplorerDriver();
		}
		else if(browser.equalsIgnoreCase("Chrome")){
			System.setProperty("webdriver.chrome.driver","C://Users//sandeepraju//Desktop//Workplace//CodeLessAutomation//drivers//chromedriver.exe");
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
}
