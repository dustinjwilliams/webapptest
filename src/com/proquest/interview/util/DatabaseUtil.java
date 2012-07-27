package com.proquest.interview.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class is just a utility class, you should not have to change anything here
 * @author rconklin
 */
public class DatabaseUtil {
	
	public static void initDB() {
		try {
			Connection cn = getConnection();
			Statement stmt = cn.createStatement();
			stmt.execute("CREATE TABLE IF NOT EXISTS PHONEBOOK (NAME varchar(255), PHONENUMBER varchar(255), ADDRESS varchar(255))");
			stmt.execute("INSERT INTO PHONEBOOK (NAME, PHONENUMBER, ADDRESS) VALUES('Chris Johnson','(321) 231-7876', '452 Freeman Drive, Algonac, MI')");
			stmt.execute("INSERT INTO PHONEBOOK (NAME, PHONENUMBER, ADDRESS) VALUES('Dave Williams','(231) 502-1236', '285 Huron St, Port Austin, MI')");
			cn.commit();
			cn.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("org.hsqldb.jdbcDriver");
		return DriverManager.getConnection("jdbc:hsqldb:mem", "sa", "");
	}

	/**
	 * Utility method to print the contents of the Phonebook database table.
	 */
	public static void printDatabaseContents() {
		
		System.out.println("Printing contents of Phonebook table from database");
		
		try {
			Connection cn = getConnection();
			Statement stmt = cn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM PHONEBOOK");
			while(rs.next()){
				System.out.println(rs.getString("Name")  + " " + rs.getString("PHONENUMBER") + " " + rs.getString("ADDRESS"));
			}
			
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
	}
	
	/**
	 * Utility function to make sure the phonebook is empty.  Necessary for unit tests.
	 */
	public static void wipeDb() {
		try {
			Connection cn = getConnection();
			Statement stmt = cn.createStatement();
			stmt.execute("TRUNCATE TABLE PHONEBOOK");
			cn.commit();
			cn.close();
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	
	}
}
