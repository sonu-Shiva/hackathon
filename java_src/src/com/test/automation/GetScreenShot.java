package com.test.automation;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.apache.poi.util.SystemOutLogger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class GetScreenShot {
	public static String capture(WebDriver driver,String screenShotName) 
    {
        TakesScreenshot ts = (TakesScreenshot)driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        System.out.println("ab path"+Model.file.getAbsolutePath());
        String dest = Model.file.getAbsolutePath()+"/"+screenShotName+".png";
        
        File destination = new File(dest);
        
        try {
			FileUtils.copyFile(source, destination);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}                             
        return dest;
    }

}
