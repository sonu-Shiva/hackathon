package com.test.automation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Helper implements Constants {
	Connection c;
	public Connection controller(){
        System.out.println("in helper");
		try {
		Class.forName(Property.getPropertyValue("DBDRIVER"));
		} catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		}
		try {
		c = DriverManager.getConnection(Property.getPropertyValue("DBURL"),Property.getPropertyValue("DBUN"),Property.getPropertyValue("DBPWD"));
		
		System.out.println("connection successfully established "+c);
		}
		catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		return c;
		}
}
