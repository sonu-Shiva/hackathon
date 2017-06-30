package com.test.automation;

import java.util.Properties;

public class Property {
	static Properties configProperies = new Properties();
	static String filePath = "config.properties";
	static {
		
		try{
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			java.io.InputStream input = classLoader.getResourceAsStream(filePath);
			configProperies.load(input);
			
		}
		catch(Exception e)
		{e.printStackTrace();}
	}
	public static String getPropertyValue(String key)
	{
		return configProperies.getProperty(key);
	}

}
