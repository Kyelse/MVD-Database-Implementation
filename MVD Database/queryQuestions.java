import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/*
 * FILE: queryQuestion.java
 * AUTHORS: QUANG VU, QUAN NGUYEN, MINH TRAN
 * CLASS: CSC 460 FALL 2021
 * ASSIGNMENT: Project 4
 * PURPOSE: This file will answer query questions
 */

public class queryQuestions {
	/*
	 * This function will take in the scanner from the main menu that call it
	 * and use the scanner to work with JDBC
	 */
	public static void selection(Scanner input, Connection dbconn) {
		String option = "options";
		while (!option.toLowerCase().equals("back")) {
			if (!isOptionValid(option)) {
				System.out.println("Invalid input! Input can only be back, options, 1, 2, 3, 4"); // add more options here
                option = input.nextLine();
			} else if (option.equalsIgnoreCase("options")) {
				System.out.println("Basic Functionalities");
				System.out.println("Please select one option below");
				System.out.println("1. Display the user details whose Ids will expire given a date in format MM/DD/YYYY");
				System.out.println("2. Count every type of appointment and check how many of them got successful their IDs");
				System.out.println("3. Display the collected fee amount for every department for a given monthin the format MM/YYYY");
				System.out.println("4. Display information about the owner of a car given its registration number");
				System.out.println("back to back to the main menu");
				option = input.nextLine();
			} else if (option.equalsIgnoreCase("1")) {
				 System.out.println("type in Date as  MM/DD/YYYY");
                 String date = input.nextLine();
                 while (!isFormatValid(date, "MM/DD/YYYY")) {
                         System.out.println("Date is not valid, type in Date as MM/DD/YYYY again!");
                         date = input.nextLine();
                 }
                 
                 // add function here
                 
                 option = "options";
                 
			}  else if (option.equalsIgnoreCase("3")) {
				 System.out.println("type in Date as  DD/YYYY");
                 String date = input.nextLine();
                 while (!isFormatValid(date, "DD/YYYY")) {
                         System.out.println("Date is not valid, type in Date as DD/YYYY again!");
                         date = input.nextLine();
                 }
                 
                 // add function here
                 
                 option = "options";
			} else if (option.equalsIgnoreCase("2")) {
				secondQuery(dbconn);
				option = "options";
			} else if (option.equalsIgnoreCase("4")) {
				 System.out.println("type in registration number");
                 String number = input.nextLine();
                 fourthQuery(number, dbconn);
                 option = "options";
			}
		}
	}
	
	/*
	 * Check if useriput is valid
	 */
	private static boolean isOptionValid(String option) {
        String[] valid = new String[] {"back", "options", "1", "2", "3", "4"}; // add more options here
        for (String op: valid) {
                if (option.toLowerCase().equals(op))
                        return true;
        }
        return false;
	}
	
	/*
	 * Check if format is valid
	 */
	private static boolean isFormatValid(String input, String format) {
		if (format.equals("MM/DD/YYYY")) {
			// implement logic here
			return true;
		} else if (format.equals("MM/YYYY")) {
			// implement logic here
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Second Query
	 */
	private static void secondQuery(Connection dbconn) {
		String[] types = new String[] {"Permit", "Licence", "Vehicle Registration", "State ID"};
		for (String type: types) {
			String query = "select count(*) from appointment where time > to_date('10-31-2021','MM-DD-YYYY')"
	                + " AND  time < to_date('12-01-2021','MM-DD-YYYY')"
	                + " AND type='" + type + "'";
	
			Statement stmt = null;
			ResultSet answer = null;
			
			try {
			
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);
			
			if (answer != null) {
			answer.next();
			System.out.println("Number of appointment related to" + type + ": " + answer.getInt("COUNT(*)"));
			}
			System.out.println();
			
			// Shut down the connection to the DBMS.
			
			stmt.close();
			
			} catch (SQLException e) {
			
			System.err.println("*** SQLException:  "
			    + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
			
			}
			
			stmt = null;
			answer = null;
			query += " AND success = 0";
			try {
				
				stmt = dbconn.createStatement();
				answer = stmt.executeQuery(query);
				
				if (answer != null) {
				answer.next();
				System.out.println("Number of success appointment related to" + type + ": " + answer.getInt("COUNT(*)"));
				}
				System.out.println();
				
				// Shut down the connection to the DBMS.
				
				stmt.close();
				
			} catch (SQLException e) {
				
				System.err.println("*** SQLException:  "
				    + "Could not fetch query results.");
				System.err.println("\tMessage:   " + e.getMessage());
				System.err.println("\tSQLState:  " + e.getSQLState());
				System.err.println("\tErrorCode: " + e.getErrorCode());
				System.exit(-1);
				
			}
			
			
		}
	}
	
	/*
	 * query number 4
	 */
	private static void fourthQuery(String number, Connection dbconn) {
		String query = "SELECT client.fname, client.lname, license.LicenseID, vehicle.make, vehicle.color FROM vehicle, client, license"
                + " WHERE client.username = license.username"
                + " AND client.username = vehicle.username"
                + " AND vehicle.registration_number = " + number;

		Statement stmt = null;
		ResultSet answer = null;
		
		try {
		
		stmt = dbconn.createStatement();
		answer = stmt.executeQuery(query);
		
		if (answer != null) {
		answer.next();
		System.out.println("Name: " + answer.getString("fname") + " " +  answer.getString("lname") + ", License ID: " + answer.getInt("license") 
						+ ", car make:" + answer.getString("make") + ", car color:" + answer.getString("color"));
		}
		System.out.println();
		
		// Shut down the connection to the DBMS.
		
		stmt.close();
		
		} catch (SQLException e) {
		
		System.err.println("*** SQLException:  "
		    + "Could not fetch query results.");
		System.err.println("\tMessage:   " + e.getMessage());
		System.err.println("\tSQLState:  " + e.getSQLState());
		System.err.println("\tErrorCode: " + e.getErrorCode());
		System.exit(-1);
		
		}

	}
}