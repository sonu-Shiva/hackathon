package com.test.automation;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class Model implements Constants{
public static File file;
public synchronized static void triggerSelenium(String ucid,String browser){
	System.out.println("in model");
    Statement st=null;
    Statement st1=null;
    Statement st2=null;
	ResultSet rs=null;
	ResultSet rs1=null;
	String usecaseName=null;
	WebDriver driver=null;
	
	ExtentReports eReport = null;
	ExtentTest testReport;
	
	if(browser.equalsIgnoreCase("firefox")){
		driver = new FirefoxDriver();
	}
	else if(browser.equalsIgnoreCase("ie")){
		System.setProperty("webdriver.ie.driver",Property.getPropertyValue(configPptPath,"IEDRIVERPATH") );
		driver = new InternetExplorerDriver();
	}
	else if(browser.equalsIgnoreCase("Chrome")){
		System.setProperty("webdriver.chrome.driver",Property.getPropertyValue(configPptPath,"CHROMEDRIVERPATH") );
		driver = new ChromeDriver();
	}
	
	//db connection
	Helper h=new Helper();
	Connection c=h.controller();
	String[] useCaseSplit=ucid.split("=");
	String[] ucidl=useCaseSplit[1].split(",");
	String path = null;
	
	try {
		for(String id:ucidl){
		int usecase_id=Integer.parseInt(id);
		st1=c.createStatement();
		String query1 = "SELECT use_case_name FROM public.\"UseCase\" Where id="+usecase_id+"; ";//rmove prod id bcs usecase id is enough
		System.out.println("after connection1");
	    rs1=st1.executeQuery(query1);
	    while(rs1.next()){
	    	usecaseName=rs1.getString("use_case_name");
	    	System.out.println(usecaseName);
	    }
	 // eReport=new ExtentReports(System.getProperty("user.dir")+"/Reports/ESFREPORT.html");
		String dateVar = new Model().getDateTime();
		System.out.println(dateVar);
		
		file = new File(Property.getPropertyValue(configPptPath,"REPORTFOLDER")+dateVar+usecase_id);
		file.mkdir();
		
		eReport=new ExtentReports(Property.getPropertyValue(configPptPath,"REPORTFOLDER")+dateVar+usecase_id+"//"+usecaseName+usecase_id+".html");

		path=Property.getPropertyValue(configPptPath,"REPORTFOLDER")+dateVar+usecase_id+"//"+usecaseName+usecase_id+".html";
		testReport=eReport.startTest(usecaseName);
		
		 System.out.println("after connection");//SELECT usecase_id, description, action, locators, element_identifier, element_value, seq_id FROM public.\"Actions_Table\"  ORDER BY SEQ_ID;"
	    	st=c.createStatement(); //
	    	String query = "Select description,action,locators,element_identifier,element_value from action where id="+usecase_id+" Order by seq";
	    	System.out.println("after connection1");
	    	rs = st.executeQuery(query);
	    	
//	     	System.setProperty("webdriver.chrome.driver","C://Users//sandeepraju//Desktop//Workplace//CodeLessAutomation//Drivers//chromedriver.exe");
//			driver = new ChromeDriver();
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
			eReport.flush(); 
		}  
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
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
    	 
    	 try {
    		// putting file pathe to db
    		
			st2=c.createStatement();
			 String query2="INSERT INTO public.\"reports\"(report,usecase) VALUES ("+path+","+usecaseName+");";                     
	  		 st2.executeQuery(query2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		
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
