
/**
 * @description This file will implement all the basic functionalities of the program which is 
 * including adding, updating, and deleting some records in the databases (might be client 
 * information, employee, appointment, or service). 
 * @author Quan Nguyen
 * @course CSC460
 * @assignment Program #4: Database Design and Implementation
 * @instructor Lester I. McCann
 * @ta Justin Do
 * @dueDate 6 December 2021
 * @language Java 16
 * 
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class BasicFunction {
	/*
	 * This function will take in the scanner from the main menu that call it and
	 * use the scanner to work with JDBC
	 */
	public static void selection(Scanner input, Connection dbconn) {
		String option = "options";
		while (!option.toLowerCase().equals("back")) {
			if (!isOptionValid(option)) {
				System.out.println("Invalid input! Input can only be back, options"); // add more options here
				option = input.nextLine();
			} else if (option.equalsIgnoreCase("options")) {
				System.out.println("Basic Functionalities");
				System.out.println("Please select one option below");
				System.out.println("1.  Add				Client");
				System.out.println("2.  Update			Client");
				System.out.println("3.  Delete			Client");
				System.out.println("4.  Add				Employee");
				System.out.println("5.  Update			Employee");
				System.out.println("6.  Delete			Employee");
				System.out.println("7.  Add				Appointment");
				System.out.println("8.  Update			Appointment");
				System.out.println("9.  Delete			Appointment");
				System.out.println("10. Add				Service");
				System.out.println("11. Update			Service");
				System.out.println("12. Delete			Service");
				System.out.println("13. Add, Update			License");
				System.out.println("14. Add, Update			Permit");
				System.out.println("15. Add, Update			Registration");
				System.out.println("16. Add, Update			StateID");
				System.out.println("back to back to the main menu");
				option = input.nextLine();
				switch (option) {
				case "1":
					addClient(input, dbconn);
					break;
				case "2":
					updateClient(input, dbconn);
					break;
				case "3":
					deleteClient(input, dbconn);
					break;
				case "4":
					addEmployee(input, dbconn);
					break;
				case "5":
					updateEmployee(input, dbconn);
					break;
				case "6":
					deleteEmployee(input, dbconn);
					break;
				case "7":
					addAppointment(input, dbconn);
					break;
				case "8":
					updateAppointment(input, dbconn);
					break;
				case "9":
					deleteAppointment(input, dbconn);
					break;
				case "10":
					addService(input, dbconn);
					break;
				case "11":
					updateService(input, dbconn);
					break;
				case "12":
					deleteService(input, dbconn);
					break;
				case "13":
					changeLicense(input, dbconn);
					break;
				case "14":
					changePermit(input, dbconn);
					break;
				case "15":
					changeRegistration(input, dbconn);
					break;
				case "16":
					changeStateID(input, dbconn);
					break;
				}
				option = input.nextLine();
			}
		}
	}

	/**
	 * Change a license number information based on user input. This will ask all of
	 * the neccesary information from the user in order to put in.
	 * 
	 * @param input  the Scanner object to take user input
	 * @param dbconn the connection to the SQL server
	 */
	private static void changeLicense(Scanner input, Connection dbconn) {
		// get the user name and check
		System.out.println("Please type in the username: ");
		String username = input.nextLine();
		if (!checkUserName(dbconn, username)) {
			System.out.println("The user does not exist in the database");
			return;
		}
		// check the time
		System.out.println("Please type in issued date: ");
		String time = input.nextLine();
		if (!isValidDate("dd/MM/yyyy", time)) {
			System.out.println("Wrong form.");
			return;
		}
		// get the license number
		System.out.println("Please type in current/new license number");
		String license = input.nextLine();

		Statement stmt = null;
		try {
			stmt = dbconn.createStatement();
			stmt.executeUpdate("DELETE from license WHERE username = '" + username + "'");
			stmt.executeUpdate("INSERT INTO license(username, time, license) values('" + username + "', '" + time + "', " + license + "'");
			System.out.println("Add successfully");
			stmt.close();

			// Shut down the connection to the DBMS.

		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/**
	 * Change a registration infomation by asking all of the neccesary information
	 * from the user in order to put in.
	 * 
	 * @param input  the Scanner object to take user input
	 * @param dbconn the connection to the SQL server
	 */
	private static void changeRegistration(Scanner input, Connection dbconn) {
		// getting user name
		System.out.println("Please type in the username: ");
		String username = input.nextLine();
		if (!checkUserName(dbconn, username)) {
			System.out.println("The user does not exist in the database");
			return;
		}
		// getting date
		System.out.println("Please type in issued date: ");
		String time = input.nextLine();
		if (!isValidDate("dd/MM/yyyy", time)) {
			System.out.println("Wrong form.");
			return;
		}
		// get all of the necessary information
		System.out.println("Please type in current new registration number");
		String rnum = input.nextLine();
		System.out.println("Please type in current new make");
		String make = input.nextLine();
		System.out.println("Please type in current new color");
		String color = input.nextLine();
		Statement stmt = null;
		try {
			stmt = dbconn.createStatement();
			stmt.executeUpdate("DELETE from vehicle WHERE registration number = '" + rnum + "'");
			stmt.executeUpdate("INSERT INTO vehicle(username, rnum, time, make, color) values('" + username + "', '" + rnum + "', '" + time + "', '" + make
					+ "', '" + color + "'");
			System.out.println("Add successfully");
			// Shut down the connection to the DBMS.
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/**
	 * Change a permit infomation by asking all of the neccesary information from
	 * the user in order to put in.
	 * 
	 * @param input  the Scanner object to take user input
	 * @param dbconn the connection to the SQL server
	 */
	private static void changePermit(Scanner input, Connection dbconn) {
		System.out.println("Please type in the username: ");
		String username = input.nextLine();
		if (!checkUserName(dbconn, username)) {
			System.out.println("The user does not exist in the database");
			return;
		}

		System.out.println("Please type in issued date: ");
		String time = input.nextLine();
		if (!isValidDate("dd/MM/yyyy", time)) {
			System.out.println("Wrong form.");
			return;
		}

		System.out.println("Please type in current/new permit number");
		String permit = input.nextLine();

		Statement stmt = null;
		try {
			stmt = dbconn.createStatement();
			stmt.executeUpdate("DELETE from permit WHERE username = '" + username + "'");
			stmt.executeUpdate("INSERT INTO permit(username, time, permit) values('" + username + "', '" + time + "', " + permit + "'");
			System.out.println("Add successfully");
			stmt.close();

			// Shut down the connection to the DBMS.

		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/**
	 * Change a stateID infomation by asking all of the neccesary information from
	 * the user in order to put in.
	 * 
	 * @param input  the Scanner object to take user input
	 * @param dbconn the connection to the SQL server
	 */
	private static void changeStateID(Scanner input, Connection dbconn) {
		System.out.println("Please type in the username: ");
		String username = input.nextLine();
		if (!checkUserName(dbconn, username)) {
			System.out.println("The user does not exist in the database");
			return;
		}

		System.out.println("Please type in issued date: ");
		String time = input.nextLine();
		if (!isValidDate("dd/MM/yyyy", time)) {
			System.out.println("Wrong form.");
			return;
		}

		System.out.println("Please type in current/new stateID number");
		String stateID = input.nextLine();

		Statement stmt = null;
		try {
			stmt = dbconn.createStatement();
			stmt.executeUpdate("DELETE from permit WHERE username = '" + username + "'");
			stmt.executeUpdate("INSERT INTO permit(username, time, stateID) values('" + username + "', '" + time + "', " + stateID + "'");
			System.out.println("Add successfully");
			stmt.close();

			// Shut down the connection to the DBMS.

		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/**
	 * Delete a client information based on user input. This will ask all of the
	 * neccesary information from the user in order to put in. which is only the
	 * primary key for these
	 * 
	 * @param input
	 * @param dbconn the connection to the SQL server
	 */
	private static void deleteClient(Scanner input, Connection dbconn) {

		System.out.println("Please type in the username: ");
		String username = input.nextLine();
		if (!checkUserName(dbconn, username)) {
			System.out.println("The user is does exist in the database");
			return;
		}

		Statement stmt = null;
		try {
			stmt = dbconn.createStatement();
			String query = "DELETE from client WHERE username = '" + username + "'";
			stmt.executeUpdate(query);
			System.out.println("Add successfully");
			stmt.close();

			// Shut down the connection to the DBMS.

		} catch (SQLException e) {

			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}

	}

	/**
	 * Delete a employee information based on user input. This will ask all of the
	 * neccesary information from the user in order to put in. which is only the
	 * primary key for these
	 * 
	 * @param input
	 * @param dbconn the connection to the SQL server
	 */
	private static void deleteEmployee(Scanner input, Connection dbconn) {
		System.out.print("Please type in EmployeeID: ");
		String eid = input.nextLine();
		if (!checkEID(dbconn, eid)) {
			System.out.println("I am sorry, this employee does not exists");
			return;
		}
		Statement stmt = null;
		try {
			stmt = dbconn.createStatement();
			String query = "DELETE from employee WHERE employeeID = '" + eid + "'";
			stmt.executeUpdate(query);
			System.out.println("Add successfully");

			// Shut down the connection to the DBMS.

			stmt.close();

		} catch (SQLException e) {

			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}

	}

	/**
	 * Delete an appointment information based on user input. This will ask all of
	 * the neccesary information from the user in order to put in. which is only the
	 * primary key for these
	 * 
	 * @param input
	 * @param dbconn the connection to the SQL server
	 */
	private static void deleteAppointment(Scanner input, Connection dbconn) {
		System.out.println("Please type in the username: ");
		String username = input.nextLine();
		if (!checkUserName(dbconn, username)) {
			System.out.println("There is no user with that username exist");
			return;
		}
		System.out.println("Please type in the time(DD/MM/YYYY): ");
		String time = input.nextLine();
		if (!isValidDate("dd/MM/yyyy", time)) {
			System.out.println("Wrong form.");
			return;
		}
		if (!checkAppointment(dbconn, username, time)) {
			System.out.println(
					"I'm sorry, an appointment of '" + username + "' at " + time + " does not exist in the database");
			return;
		}
		Statement stmt = null;
		try {
			stmt = dbconn.createStatement();
			String query = "DELETE from appointment WHERE username = '" + username + "'";
			stmt.executeUpdate(query);
			System.out.println("Add successfully");

			// Shut down the connection to the DBMS.

			stmt.close();

		} catch (SQLException e) {

			System.err.println("***Something wrong here***");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}
	}

	/**
	 * Delete a service information based on user input. This will ask all of the
	 * neccesary information from the user in order to put in. which is only the
	 * primary key for these
	 * 
	 * @param input
	 * @param dbconn the connection to the SQL server
	 */
	private static void deleteService(Scanner input, Connection dbconn) {
		System.out.println("Please type in the service type: ");
		String service = input.nextLine();
		if (checkService(dbconn, service)) {
			System.out.println("Service already exist");
			return;
		}
		Statement stmt = null;
		try {
			stmt = dbconn.createStatement();
			String query = "DELETE FROM service where service_type = '" + service + "'";
			stmt.executeUpdate(query);
			System.out.println("Add service successfully");
			// Shut down the connection to the DBMS.
			stmt.close();

		} catch (SQLException e) {

			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/**
	 * Adding a client information based on user input. This will ask all of the
	 * neccesary information from the user in order to put in. which are all of the
	 * fields
	 * 
	 * @param input
	 * @param dbconn the connection to the SQL server
	 */
	private static void addClient(Scanner input, Connection dbconn) {
		System.out.println("Please type in the username: ");
		String username = input.nextLine();
		if (checkUserName(dbconn, username)) {
			System.out.println("The user is already exist in the database");
			return;
		}
		Statement stmt = null;
		try {
			stmt = dbconn.createStatement();
			System.out.print("Please type in the DOB(DD/MM/YYYY): ");
			String dob = input.nextLine();
			if (!isValidDate("dd/MM/yyyy", dob)) {
				System.out.println("Wrong form.");
				return;
			}
			System.out.print("Please type in the First Name: ");
			String fname = input.nextLine();
			System.out.print("Please type in the Last Name: ");
			String lname = input.nextLine();
			String query = "INSERT INTO client(username, dob, fname, lname) values ('" + username + "'," + "'" + dob + "',"
					+ "'" + fname + "'," + "'" + lname + "')";
			System.out.println(query);
			stmt.executeUpdate(query);
			System.out.println("Add successfully");
			stmt.close();

			// Shut down the connection to the DBMS.

		} catch (SQLException e) {

			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}

	}

	/**
	 * Adding an employee information based on user input. This will ask all of the
	 * necessary information from the user in order to put in. which are all of the
	 * fields
	 * 
	 * @param input
	 * @param dbconn the connection to the SQL server
	 */
	private static void addEmployee(Scanner input, Connection dbconn) {
		System.out.print("Please type in EmployeeID: ");
		String eid = input.nextLine();
		if (checkEID(dbconn, eid)) {
			System.out.println("I am sorry, this employee already exists");
			return;
		}

		System.out.print("Please type in jobID: ");
		String jid = input.nextLine();
		if (!checkJob(dbconn, jid)) {
			System.out.println("I am sorry, this job does not exists");
			return;
		}

		System.out.print("Please type in Department Number: ");
		String dno = input.nextLine();
		if (!checkDepartment(dbconn, dno)) {
			System.out.println("I am sorry, this department does not exists");
			return;
		}
		Statement stmt = null;
		try {
			stmt = dbconn.createStatement();
			System.out.print("Please type in the First Name: ");
			String fname = input.nextLine();
			System.out.print("Please type in the Last Name: ");
			String lname = input.nextLine();
			String query = "INSERT INTO employee values ('" + eid + "'," + "'" + jid + "'," + "'" + dno + "'," + "'"
					+ fname + "'," + "'" + lname + "')";
			stmt.executeUpdate(query);
			System.out.println("Add successfully");

			// Shut down the connection to the DBMS.

			stmt.close();

		} catch (SQLException e) {

			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}

	}

	/**
	 * Adding an appointment information based on user input. This will ask all of
	 * the necessary information from the user in order to put in. which are all of
	 * the fields
	 * 
	 * @param input
	 * @param dbconn the connection to the SQL server
	 */
	private static void addAppointment(Scanner input, Connection dbconn) {
		System.out.println("Please type in the username: ");
		String username = input.nextLine();
		if (!checkUserName(dbconn, username)) {
			System.out.println("There is no user with that username exist");
			return;
		}
		System.out.println("Please type in the time(DD/MM/YYYY): ");
		String time = input.nextLine();
		if (!isValidDate("dd/MM/yyyy", time)) {
			System.out.println("Wrong form.");
			return;
		}
		if (checkAppointment(dbconn, username, time)) {
			System.out.println(
					"I'm sorry, an appointment of '" + username + "' at " + time + "is already exist in the database");
			return;
		}
		Statement stmt = null;
		try {
			stmt = dbconn.createStatement();
			System.out.print("Please type in EmployeeID: ");
			String eid = input.nextLine();
			if (!checkEID(dbconn, eid)) {
				System.out.println("I am sorry, this employee does not exist");
				return;
			}
			System.out.println("Please type in the service needed: ");
			String service = input.nextLine();
			if (!checkService(dbconn, service)) {
				System.out.println("The service does not exist");
				return;
			}
			String query = "INSERT INTO appointment(username, time, eid, service, success, process) values ('" + username + "'," + "'" + time + "'," + "'" + eid + "',"
					+ "'" + service + "'," + "'" + "0" + "'," + "0" + "')";
			stmt.executeUpdate(query);
			System.out.println("Add successfully");

			// Shut down the connection to the DBMS.

			stmt.close();

		} catch (SQLException e) {

			System.err.println("***Something wrong here***");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}
	}

	/**
	 * Adding an Service information based on user input. This will ask all of the
	 * necessary information from the user in order to put in. which are all of the
	 * fields
	 * 
	 * @param input
	 * @param dbconn the connection to the SQL server
	 */
	private static void addService(Scanner input, Connection dbconn) {
		System.out.println("Please type in the service type: ");
		String service = input.nextLine();
		if (checkService(dbconn, service)) {
			System.out.println("Service already exist");
			return;
		}
		Statement stmt = null;
		try {
			stmt = dbconn.createStatement();
			System.out.print("Please type in the price: ");
			String price = input.nextLine();
			System.out.print("Please type in the expiry length in year: ");
			String expiry = input.nextLine();
			if (isInt(price) == false || isInt(expiry) == false) {
				System.out.println("Please type only whole number for the price and the expiry length");
			}
			String query = "INSERT INTO service(service, price, expiry) values ('" + service + "'," + "'" + price + "'," + "'" + expiry + "')";
			stmt.executeUpdate(query);
			System.out.println("Add service successfully");
			// Shut down the connection to the DBMS.
			stmt.close();

		} catch (SQLException e) {

			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/**
	 * Updating a client information based on user input. This will ask all of the
	 * necessary information from the user in order to put in. which are all of the
	 * fields
	 * 
	 * @param input
	 * @param dbconn the connection to the SQL server
	 */
	private static void updateClient(Scanner input, Connection dbconn) {
		System.out.println("Please type in the username: ");
		String username = input.nextLine();
		if (!checkUserName(dbconn, username)) {
			System.out.println("There are no user like that");
			return;
		}
		Statement stmt = null;
		try {
			stmt = dbconn.createStatement();
			System.out.print("Please type in the DOB(DD/MM/YYYY): ");
			String dob = input.nextLine();
			if (!isValidDate("dd/MM/yyyy", dob)) {
				System.out.println("Wrong form.");
				return;
			}
			System.out.print("Please type in the First Name: ");
			String fname = input.nextLine();
			System.out.print("Please type in the Last Name: ");
			String lname = input.nextLine();
			String query = "UPDATE client set fname = '" + fname + "', lname = '" + lname + "' WHERE username = '"
					+ username + "'";
			stmt.executeUpdate(query);
			System.out.println("Update successfully");
			stmt.close();

			// Shut down the connection to the DBMS.

		} catch (SQLException e) {

			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}

	}

	/**
	 * Adding an employee information based on user input. This will ask all of the
	 * necessary information from the user in order to put in. which are all of the
	 * fields
	 * 
	 * @param input
	 * @param dbconn the connection to the SQL server
	 */
	private static void updateEmployee(Scanner input, Connection dbconn) {
		System.out.print("Please type in EmployeeID: ");
		String eid = input.nextLine();
		if (!checkEID(dbconn, eid)) {
			System.out.println("I am sorry, this employee does not exist");
			return;
		}
		System.out.print("Please type in jobID: ");
		String jid = input.nextLine();
		if (!checkJob(dbconn, jid)) {
			System.out.println("I am sorry, this job does not exist");
			return;
		}
		System.out.print("Please type in Department Number: ");
		String dno = input.nextLine();
		if (!checkDepartment(dbconn, dno)) {
			System.out.println("I am sorry, this job does not exist");
			return;
		}
		Statement stmt = null;
		try {
			stmt = dbconn.createStatement();
			System.out.print("Please type in the First Name: ");
			String fname = input.nextLine();
			System.out.print("Please type in the Last Name: ");
			String lname = input.nextLine();
			String query = "UPDATE employee set job_id = '" + jid + "', departmentNumber = " + "'" + dno
					+ "', fname =  " + "'" + fname + "', lname = " + "'" + lname + "' WHERE employee.employeeID = '"
					+ eid + "'";
			System.out.println(query);
			stmt.executeUpdate(query);
			System.out.println("Add successfully");

			// Shut down the connection to the DBMS.

			stmt.close();

		} catch (SQLException e) {

			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}

	}

	/**
	 * Adding an appoinemnt information based on user input. This will ask all of
	 * the necessary information from the user in order to put in. which are all of
	 * the fields. This will be the only change when log is updated also.
	 * 
	 * @param input
	 * @param dbconn the connection to the SQL server
	 */
	private static void updateAppointment(Scanner input, Connection dbconn) {
		System.out.println("Please type in the username: ");
		String username = input.nextLine();
		if (!checkUserName(dbconn, username)) {
			System.out.println("There is no user with that username exist");
			return;
		}
		System.out.println("Please type in the time(DD/MM/YYYY): ");
		String time = input.nextLine();
		if (!isValidDate("dd/MM/yyyy", time)) {
			System.out.println("Wrong form.");
			return;
		}

		// check if the appointment does exist or not
		if (!checkAppointment(dbconn, username, time)) {
			System.out.println("I'm sorry, an appointment of '" + username + "' at " + time + "does not exist.");
			return;
		}
		Statement stmt = null;
		ResultSet answer = null;
		try {
			stmt = dbconn.createStatement();
			// we get all of the neccessary remain information to update here
			System.out.println("Please type in the new time(DD/MM/YYYY): ");
			String newTime = input.nextLine();
			if (!isValidDate("dd/MM/yyyy", newTime)) {
				System.out.println("Wrong form.");
				return;
			}
			System.out.print("Please type in new EmployeeID: ");
			String eid = input.nextLine();
			if (!checkEID(dbconn, eid)) {
				System.out.println("I am sorry, this employee does not exist");
				return;
			}
			if (!checkAppointment(dbconn, username, newTime)) {
				System.out.println("I'm sorry, an appointment of '" + username + "' at " + newTime + "does not exist.");
				return;
			}
			System.out.println("Please type in the new service needed: ");
			String service = input.nextLine();
			if (!checkService(dbconn, service)) {
				System.out.println("The service does not exist");
				return;
			}
			// We need to get the price of the service
			answer = stmt.executeQuery("SELECT price FROM service WHERE service_type = '" + service + "'");
			answer.next();
			String price = String.valueOf(answer.getInt("price"));
			System.out.println("Please indicate if the appointment is success or not (1 or 0) ");
			String success = input.nextLine();
			if (!success.equals("0") || !success.equals("1")) {
				System.out.println("Only 1 for true and 0 for false");
				return;
			}
			System.out.println("Please indicate if the apppointment is processed or not (1 or 0)");
			String process = input.nextLine();
			// Whenever the appointment is processes, a non refundable fee is charged.
			if (!process.equals("0") || !process.equals("1")) {
				System.out.println("Only 1 for true and 0 for false");
				return;
			}
			if (success.equals("1") && process.equals("0")) {
				System.out.println("The appointment should be processed first");
				return;
			}
			if (process.equals("1")) {
				String query = "INSERT INTO logs(username, newTime, eid, price) values ('" + username + "', '" + newTime + "', '" + eid + "', '"
						+ price + "')";
				stmt.executeUpdate(query);
			}
			// Deleting the old one and input the new with the new time available
			String query = "DELETE from appointment WHERE username = '" + username + "' AND '" + "time = '" + time
					+ "'";
			System.out.println(query);
			stmt.executeUpdate(query);
			query = "INSERT INTO appointment values ('" + username + "'," + "'" + time + "'," + "'" + eid + "'," + "'"
					+ service + "'," + "'" + success + "'," + process + "')";
			System.out.println(query);
			stmt.executeUpdate(query);
			System.out.println("Add successfully");

			// Shut down the connection to the DBMS.

			stmt.close();

		} catch (SQLException e) {

			System.err.println("***Something wrong here***");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}
	}

	/**
	 * Adding an service information based on user input. This will ask all of the
	 * necessary information from the user in order to put in. which are all of the
	 * fields.
	 * 
	 * @param input
	 * @param dbconn the connection to the SQL server
	 */
	private static void updateService(Scanner input, Connection dbconn) {
		System.out.println("Please type in the service type: ");
		String service = input.nextLine();
		if (checkService(dbconn, service)) {
			System.out.println("Service already exist");
			return;
		}
		Statement stmt = null;
		try {
			stmt = dbconn.createStatement();
			System.out.print("Please type in the price: ");
			String price = input.nextLine();
			System.out.print("Please type in the expiry length in year: ");
			String expiry = input.nextLine();
			if (isInt(price) == false || isInt(expiry) == false) {
				System.out.println("Please type only whole number for the price and the expiry length");
			}
			String query = "UPDATE service set price = '" + price + "', " + "expiry_length = '" + expiry
					+ "' WHERE service_type = '" + service + "'";
			System.out.println(query);
			stmt.executeUpdate(query);
			System.out.println("Add service successfully");
			// Shut down the connection to the DBMS.
			stmt.close();

		} catch (SQLException e) {

			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/**
	 * Check in the database if an appointment with for a certain username of a
	 * certain time exist or not.
	 * 
	 * @param dbconn the connection to the SQL server
	 * @param eid    the id number of the employee
	 * @return indiciate if the employee exist
	 */
	private static boolean checkAppointment(Connection dbconn, String username, String time) {
		// Send the query to the DBMS, and get and display the results

		Statement stmt = null;
		ResultSet answer = null;
		String queryCheck = "SELECT count(*) as checking, username as 'UserName', time as 'Appointment Tame', EmployeeID, service_type "
				+ "as 'Service', Success, Process FROM appointment WHERE appointment.username = '" + username
				+ "' AND appointment.time = '" + time + "'";
		System.out.println(queryCheck);
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(queryCheck);
			if (answer != null) {
				answer.next();
				int count = answer.getInt("checking");
				if (count == 0) {
					return false;
				}
			}
			printQueryMetaData(answer);
			while (answer.next()) {
				System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s\n", answer.getInt("UserName"),
						answer.getDate("Appointment Time"), answer.getInt("EmployeeID"), answer.getString("Service"),
						answer.getInt("Success"), answer.getInt("Process"));
			}
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}
		return true;
	}

	/**
	 * Check in the database if an employee with a certain ID number exist or not.
	 * 
	 * @param dbconn the connection to the SQL server
	 * @param eid    the id number of the employee
	 * @return indiciate if the employee exist
	 */
	private static boolean checkEID(Connection dbconn, String eid) {
		// Send the query to the DBMS, and get and display the results

		Statement stmt = null;
		ResultSet answer = null;
		String queryCheck = "SELECT count(*) as checking FROM employee WHERE employee.EmployeeId = '" + eid + "'";
		System.out.println(queryCheck);
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(queryCheck);
			if (answer != null) {
				answer.next();
				int count = answer.getInt("checking");
				if (count == 0) {
					return false;
				}
			}
			queryCheck = "SELECT employeeID as 'EmployeeID', fname as 'First Name', "
					+ "lname as 'Last Name' FROM employee WHERE employee.EmployeeId = '" + eid + "'";
			printQueryMetaData(answer);
			while (answer.next()) {
				System.out.printf("%-15s %-15s %-15s\n", answer.getInt("EmployeeID"), answer.getString("First Name"),
						answer.getString("Last Name"));
			}
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}
		return true;
	}

	/**
	 * Check in the database if a user with a certain username exist or not.
	 * 
	 * @param dbconn the connection to the SQL server
	 * @param eid    the id number of the employee
	 * @return indiciate if the employee exist
	 */
	private static boolean checkUserName(Connection dbconn, String username) {
		// Send the query to the DBMS, and get and display the results

		Statement stmt = null;
		ResultSet answer = null;
		String queryCheck = "SELECT count(*) as checking FROM client WHERE client.username = '" + username + "'";
		System.out.println(queryCheck);
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(queryCheck);
			if (answer != null) {
				answer.next();
				int count = answer.getInt("checking");
				if (count == 0) {
					return false;
				}

			}
			queryCheck = "SELECT username as 'UserName', fname as 'First Name', lname "
					+ "as 'Last Name' FROM client WHERE client.username = '" + username + "'";
			printQueryMetaData(answer);
			while (answer.next()) {
				System.out.printf("%-15s %-15s %-15s\n", answer.getString("UserName"), answer.getString("First Name"),
						answer.getString("Last Name"));
			}
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}
		return true;
	}

	/**
	 * Check in the database if a job with a job number or not. If exist, print out
	 * an entry to let the user knows about the information of that.
	 * 
	 * @param dbconn the connection to the SQL server
	 * @param job_ID the department number of the employee
	 * @return indiciate if the department exist
	 */
	private static boolean checkJob(Connection dbconn, String job_ID) {
		// Send the query to the DBMS, and get and display the results

		Statement stmt = null;
		ResultSet answer = null;
		String queryCheck = "SELECT count(*) as checking FROM job WHERE job.job_id = '" + job_ID + "'";
		System.out.println(queryCheck);
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(queryCheck);
			if (answer != null) {
				answer.next();
				int count = answer.getInt("checking");
				if (count == 0) {
					return false;
				}
			}
			queryCheck = "SELECT job_id as 'Job ID', job_type as 'Job Type', 'job_title'"
					+ " as 'Title', salary as 'Salary' FROM job WHERE job.job_id = '" + job_ID + "'";
			printQueryMetaData(answer);
			while (answer.next()) {
				System.out.printf("%-15s %-15s %-15s %-15s\n", answer.getInt("Job ID"), answer.getString("Job Type"),
						answer.getString("Job Title"), answer.getInt("Salary"));

			}
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}
		return true;
	}

	/**
	 * Check in the database if a department with a department number or not. If
	 * exist, print out an entry to let the user knows about the information of
	 * that.
	 * 
	 * @param dbconn           the connection to the SQL server
	 * @param departmentNumber the department number of the employee
	 * @return indiciate if the department exist
	 */
	private static boolean checkDepartment(Connection dbconn, String departmentNumber) {
		// Send the query to the DBMS, and get and display the results

		Statement stmt = null;
		ResultSet answer = null;
		String queryCheck = "SELECT count(*) as checking FROM department WHERE department.departmentNumber = '"
				+ departmentNumber + "'";
		System.out.println(queryCheck);
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(queryCheck);
			if (answer != null) {
				answer.next();
				int count = answer.getInt("checking");
				if (count == 0) {
					return false;
				}
			}
			queryCheck = "SELECT departmentNumber as 'Department Number', name as "
					+ "'Department Name' FROM department WHERE department.departmentNumber = '" + departmentNumber
					+ "'";
			printQueryMetaData(answer);
			while (answer.next()) {
				System.out.printf("%-15s %-15s\n", answer.getInt("Department Number"),
						answer.getString("Department Name"));
			}
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}
		return true;
	}

	/**
	 * Check in the database if service with a certain service name exist or not. If
	 * exist, print out an entry to let the user knows about the information of
	 * that.
	 * 
	 * @param dbconn  the connection to the SQL server
	 * @param service the service name
	 * @return indiciate if the service exist
	 */
	private static boolean checkService(Connection dbconn, String service) {
		// Send the query to the DBMS, and get and display the results

		Statement stmt = null;
		ResultSet answer = null;
		String queryCheck = "SELECT count(*) as checking FROM service WHERE service.service_type = '" + service
				+ "'";
		System.out.println(queryCheck);
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(queryCheck);
			if (answer != null) {
				answer.next();
				int count = answer.getInt("checking");
				if (count == 0) {
					return false;
				}
			}
			queryCheck = "SELECT service_type as 'Service', price as 'Price', "
					+ "expiry_length as 'Expiry Duration (year)' FROM service WHERE service.service_type = '" + service
					+ "'";
			while (answer.next()) {
				System.out.printf("%-15s %-15s %-15s\n", answer.getString("Service"), answer.getInt("Price"),
						answer.getInt("Expiry Duration (year)"));
			}
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}
		return true;
	}

	/**
	 * A wrapper to print out metadata of an answer. This piece of code is being
	 * used many times so it is good to put it here.
	 * 
	 * @param answer
	 */
	private static void printQueryMetaData(ResultSet answer) {
		// Using printf to format the answer
		try {
			ResultSetMetaData answermetadata = answer.getMetaData();
			for (int i = 2; i <= answermetadata.getColumnCount(); i++) {
				System.out.printf("%-15s", answermetadata.getColumnName(i));
			}
		} catch (SQLException e) {

			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}
	}

	/**
	 * A helper function for validating the date time format. Format helped the
	 * program to identify which format to use.
	 * 
	 * @param format
	 * @param values
	 * @return
	 */
	public static boolean isValidDate(String format, String values) {
		SimpleDateFormat parsing = new SimpleDateFormat("dd/MM/yyyy");
		parsing.setLenient(false);
		try {
			parsing.parse(values);
		} catch (Exception e) {
			return false;
		}
		return true;

	}

	/**
	 * A little wrapper function to check if the string is an integer
	 * representation.
	 * 
	 * @param number
	 * @return true or false depends on the string representation
	 */
	public static boolean isInt(String number) {
		try {
			Integer.parseInt(number);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/*
	 * Check if useriput is valid
	 */
	private static boolean isOptionValid(String option) {
		String[] valid = new String[18]; // add more options here
		valid[0] = "back";
		valid[1] = "options";
		for (int i = 2; i < 18; i++) {
			valid[i] = String.valueOf(i - 1);
		}
		for (String op : valid) {
			if (option.toLowerCase().equals(op))
				return true;
		}
		return false;
	}
}
