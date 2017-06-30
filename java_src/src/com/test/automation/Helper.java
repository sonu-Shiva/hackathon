package com.test.automation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Helper implements Constants {
	Connection c;
	public Connection controller(){
        System.out.println("in helper");
		try {
		Class.forName(Property.getPropertyValue(configPptPath,"DBDRIVER"));
		} catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		}
		try {
		c = DriverManager.getConnection(Property.getPropertyValue(configPptPath,"DBURL"),Property.getPropertyValue(configPptPath,"DBUN"),Property.getPropertyValue(configPptPath,"DBPWD"));
		System.out.println("connection successfully established "+c);
		}
		catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		return c;
		}
}
