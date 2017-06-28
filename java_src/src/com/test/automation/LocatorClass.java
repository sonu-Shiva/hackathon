package com.test.automation;

import org.openqa.selenium.By;

public class LocatorClass {
	
	public By getLocator(String locatorName,String locatorData){
		By b= null;
		
		switch(locatorName.toUpperCase()){
		  case "ID":
		  b=By.id(locatorData);
		  break;
		  
		  case "NAME":
		  b=By.name(locatorData);
		  break;
			  
		  case "XPATH":
		  b=By.xpath(locatorData);
		  break;
		  
		  case "CSSSELECTOR":
		  b=By.cssSelector(locatorData);
		  break;	
		  
		  case "LINKTEXT":
		  b=By.linkText(locatorData);
		  break;	
		  
		  case "CLASSNAME":
	      b=By.className(locatorData);
	      break;
				  
		  case "TAGNAME":
		  b=By.tagName(locatorData);
		  break;
			  
		  case "PARTIALLINKTEXT":
		  b=By.partialLinkText(locatorData);
		  break;
		  
		  default:
		}
		return b;
		
	}
}
