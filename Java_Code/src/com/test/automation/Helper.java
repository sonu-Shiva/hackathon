package com.test.automation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Helper {
	Connection c;
	public Connection controller(){
        System.out.println("in helper");
		try {
		Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
		}
		try {
		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/","postgres","Tiger");
		System.out.println("connection successfully established "+c);
		}
		catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		return c;
		}
}
