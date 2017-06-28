package com.test.automation;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


public class ActionMethods {
	public static void clickit(WebDriver driver,String action,String locatorName,String locatorData,ExtentTest testReport){
		try {
			WebDriverWait wait=new WebDriverWait(driver,60);
			wait.until(ExpectedConditions.visibilityOfElementLocated(new LocatorClass().getLocator(locatorName,locatorData)));
			driver.findElement(new LocatorClass().getLocator(locatorName,locatorData)).click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			testReport.log(LogStatus.FAIL,"Click operation failed");
			testReport.log(LogStatus.FAIL,e.getMessage().toString());
			e.printStackTrace();
			
			return;
		}
		testReport.log(LogStatus.PASS,"Clicked successfully");
	}
	public static void sendkeys(WebDriver driver,String action,String locatorName,String locatorData,String testdata,ExtentTest testReport){
		try {
			WebDriverWait wait=new WebDriverWait(driver,60);
			wait.until(ExpectedConditions.visibilityOfElementLocated(new LocatorClass().getLocator(locatorName,locatorData)));
			driver.findElement(new LocatorClass().getLocator(locatorName,locatorData)).sendKeys(testdata);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			testReport.log(LogStatus.FAIL,"Not able to input the text");
			testReport.log(LogStatus.FAIL,e.getMessage().toString());
			e.printStackTrace();
			return;
		}
		testReport.log(LogStatus.PASS,"Able to input the text");
		
	}
	public static void sendDynamickeys(WebDriver driver,String action,String locatorName,String locatorData,String testdata,ExtentTest testReport){
		String tempData = null;
		try {
			WebDriverWait wait=new WebDriverWait(driver,60);
			wait.until(ExpectedConditions.visibilityOfElementLocated(new LocatorClass().getLocator(locatorName,locatorData)));
			tempData=new SimpleDateFormat("yyMMddHHmmss").format(new java.util.Date());
			if(testdata==null){
			driver.findElement(new LocatorClass().getLocator(locatorName,locatorData)).sendKeys(tempData);
			testReport.log(LogStatus.PASS,"Able to input the text: "+tempData);
			}
			else{
			driver.findElement(new LocatorClass().getLocator(locatorName,locatorData)).sendKeys(tempData+testdata);
			testReport.log(LogStatus.PASS,"Able to input the text: "+tempData+testdata);
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if(testdata==null)
			testReport.log(LogStatus.FAIL,"Not able to input the text: "+tempData);
			else
			testReport.log(LogStatus.FAIL,"Not able to input the text: "+tempData+testdata);	
			
			testReport.log(LogStatus.FAIL,e.getMessage().toString());
			e.printStackTrace();
			return;
		}
		
		
	}
	
	public static void dropdown(WebDriver driver,String action,String locatorName,String locatorData,String dropdownvalue,ExtentTest testReport){
		try {
			WebDriverWait wait=new WebDriverWait(driver,60);
			wait.until(ExpectedConditions.visibilityOfElementLocated(new LocatorClass().getLocator(locatorName,locatorData)));	
		    new Select(driver.findElement(new LocatorClass().getLocator(locatorName,locatorData))).selectByValue(dropdownvalue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			testReport.log(LogStatus.FAIL,"Not able to select value from dropdown");
			testReport.log(LogStatus.FAIL,e.getMessage().toString());
			e.printStackTrace();
			return;
		}
		testReport.log(LogStatus.PASS,"Successfully selected ");
	}
	
	//for lookup locator value:  xpath,id,tag
	public static void lookup(WebDriver driver,String action,String locatorName,String locatorData,String lookupValue,ExtentTest testReport){
		try {
			String[] str=locatorData.split(",");
			
			WebDriverWait wait=new WebDriverWait(driver,60);
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(new LocatorClass().getLocator(locatorName,str[0]))));	
			WebElement webElement = driver.findElement(new LocatorClass().getLocator(locatorName,str[0]));
			webElement.sendKeys(Character.toString(lookupValue.charAt(0)));
			
			WebElement autoOptions = driver.findElement(By.id(str[1]));
			wait.until(ExpectedConditions.visibilityOf(autoOptions));
			
			List<WebElement> optionsToSelect = autoOptions.findElements(By.tagName(str[2]));
			for(WebElement option : optionsToSelect){
		        if(option.getText().equals(lookupValue)) {
		        	System.out.println("Trying to select: "+lookupValue);
		            option.click();
		            break;
		        }
		    }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			testReport.log(LogStatus.FAIL,"Not able to select value from lookup");
			testReport.log(LogStatus.FAIL,e.getMessage().toString());
			e.printStackTrace();
			return;
		}
		testReport.log(LogStatus.PASS,"Successfully selected ");
	}
	public static void URL(WebDriver driver,String action,String testdata,ExtentTest testReport){
		
		try {
			driver.get(testdata);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			testReport.log(LogStatus.FAIL,"Not able to enter URL");
			testReport.log(LogStatus.FAIL,e.getMessage().toString());
			e.printStackTrace();
			return;
		}
		testReport.log(LogStatus.PASS,"URL entered successfully");
		System.out.println("In Url method");
	}
	
	public static void loop(WebDriver driver,String action,String locator,String input1,String input2,Connection c,ExtentTest testReport){
		String[] str=input2.split("-");
		
		Statement st=null;
		ResultSet rs=null;
		
	     try {
	    	 System.out.println("after connection");
	    	 for(int i=0;i<Integer.parseInt(input1);i++){
	    	 st=c.createStatement();
	    	 String sqlQuery="SELECT usecase_id, description, action, locators, element_identifier, element_value, seq_id FROM public.\"Actions_Table\" WHERE seq_id BETWEEN '"+str[0]+"' AND '"+str[1]+"' ORDER BY SEQ_ID;";
	    	 rs = st.executeQuery(sqlQuery);
	    	 ActionClass ac=new ActionClass();
	    	 while(rs.next()){
	    	 String desc=rs.getString("description");
				String actionType=rs.getString("action");
				String locators=rs.getString("locators");
				String locatorName=rs.getString("element_identifier");
				String testData=rs.getString("element_value");
				ac.callActionMethods(driver,actionType,locators,locatorName,testData,c,testReport);
	    	 }
	    	 }
	     }
	     catch (SQLException e) {
	 		// TODO Auto-generated catch block
	    	testReport.log(LogStatus.FAIL,"Failed to Loop"+e.getMessage());
	 		e.printStackTrace();
	 		return;
	 	}	
	     testReport.log(LogStatus.PASS,"Looped successfully for "+Integer.parseInt(input1)+" times from "+str[0]+" to "+str[1]+"th step");
	}
	
	public static void presenceOfElement(WebDriver driver,String action,String locatorName,String locatorData,String testdata,ExtentTest testReport){
		try{
		WebDriverWait wait=new WebDriverWait(driver,3);
		wait.until(ExpectedConditions.visibilityOfElementLocated(new LocatorClass().getLocator(locatorName,locatorData)));
		WebElement webElement = driver.findElement(new LocatorClass().getLocator(locatorName,locatorData));
		if(webElement.getText().equals(testdata)){
			System.out.println("Element is present");
			testReport.log(LogStatus.PASS,"Element "+testdata+" is present");	
		}
		else{
			System.out.println("Element is not present");
			testReport.log(LogStatus.FAIL,"Element "+testdata+" is not present");	
		}
		}
		catch(Exception e){
			System.out.println("Invalid LocatorValue");
			testReport.log(LogStatus.FAIL,"Element is not present or Invalid LocatorValue"+e.getMessage());
			e.printStackTrace();
			return;
		}
			
	}
	
	public static void pause(WebDriver driver,String action,String testdata,ExtentTest testReport){
		long number = Long.parseLong(testdata);
		try {
			Thread.sleep(number);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
