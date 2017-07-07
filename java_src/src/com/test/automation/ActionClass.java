package com.test.automation;

import java.sql.Connection;

import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentTest;

public class ActionClass {
	
	public void callActionMethods(WebDriver driver,String actionName,String locatorName, String locatorData, String testData,Connection c,ExtentTest testReport){
		
		
		switch(actionName.toUpperCase()){
		
		  case "CLICK":
		  ActionMethods.clickit(driver, actionName, locatorName, locatorData,testReport);
		  break;
		  
		  case "ENTERTEXT":
		     System.out.println("in sendkeys");
		     ActionMethods.sendkeys(driver, actionName, locatorName, locatorData, testData,testReport);
		     System.out.println("outside send keys");
		     break;
		     
		  case "ENTERDYNAMICTEXT":
		     System.out.println("in sendDynamickeys");
		     ActionMethods.sendDynamickeys(driver, actionName, locatorName, locatorData, testData,testReport);
		     System.out.println("outside sendDynamickeys");
		     break;
			  
		  case "SELECTFROMDROPDOWN":
			  ActionMethods.dropdown(driver, actionName, locatorName, locatorData, testData,testReport);
			  break;
			  
		  case "SELECTFROMLOOKUP":
			  ActionMethods.lookup(driver, actionName, locatorName, locatorData, testData,testReport);
			  break;
			  
		  case "URL":
			  ActionMethods.URL(driver, actionName, testData,testReport);
			  break;
		
		  case "LOOP":
			  ActionMethods.loop(driver, actionName,locatorName,locatorData, testData,c,testReport);
			  break;
		  
		  case "PRESENCEOFELEMENT":
			  ActionMethods.presenceOfElement(driver, actionName, locatorName, locatorData, testData, testReport);
			  break;
		    
		  case "PAUSE":
			  ActionMethods.pause(driver, actionName, testData,testReport);
			  break;
		 
		}
		
	}
	

}
