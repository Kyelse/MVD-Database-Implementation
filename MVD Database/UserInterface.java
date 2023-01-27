
/**
 * @description This file basically providing the user a text interface to interact 
 * with this basic DBMS program. The text with display questions and tell the user 
 * to type in 1, 2 ,3... in order to interact with that and inputting the neccessary 
 * basic function or the query itself. 
 * @author Quan Nguyen, Quang Vu, Minh Tran
 * @course CSC460
 * @assignment Program #4: Database Design and Implementation
 * @instructor Lester I. McCann
 * @ta Justin Do
 * @dueDate 6 December 2021
 * @language Java 16
 * 
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class UserInterface {
	public static void main(String args[]) {
		String option = "options";
		String username = "kyelse"; // Oracle DBMS username
		String password = "a2124"; // Oracle DBMS password
		Connection dbconn = createConnection(username, password);
		Scanner input = new Scanner(System.in);
		while (!option.toLowerCase().equals("exit")) {
			if (!isOptionValid(option)) {
				System.out.println("Invalid input! Input can only be 1, 2, exit, options");
				option = input.nextLine();
			} else if (option.equalsIgnoreCase("options")) {
				System.out.println("Welcome to Department of Motor Vehicles (DMV)");
				System.out.println("Please select one option below");
				System.out.println("1. Basic Functionalities");
				System.out.println("2. Answer Queries Questions");
				System.out.println("Exit to exit program");
				option = input.nextLine();
			} else if (option.equalsIgnoreCase("1")) {
				BasicFunction.selection(input, dbconn);
				option = "options";
			} else { // options = '2'
				queryQuestions.selection(input, dbconn);
				option = "options";
			}
		}
		input.close();
		System.out.println("Good Bye!");

	}

	/**
	 * Checking if the option itself is valid or not.
	 * 
	 * @param option
	 * @return
	 */
	private static boolean isOptionValid(String option) {
		String[] valid = new String[] { "1", "2", "exit", "options" };
		for (String op : valid) {
			if (option.toLowerCase().equals(op))
				return true;
		}
		return false;
	}

	/**
	 * createConnection(String userName, String password): This function will use
	 * the given parameter to create connection to the appropriate server and later
	 * use that server to create table
	 */
	private static Connection createConnection(String username, String password) {
		// Access the oracle DB
		final String oracleURL = // Magic lectura -> aloe access spell
				"jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

		// load the (Oracle) JDBC driver by initializing its base
		// class, 'oracle.jdbc.OracleDriver'.

		try {

			Class.forName("oracle.jdbc.OracleDriver");

		} catch (ClassNotFoundException e) {

			System.err.println("*** ClassNotFoundException:  " + "Error loading Oracle JDBC driver.  \n"
					+ "\tPerhaps the driver is not on the Classpath?");
			System.exit(-1);

		}

		// make and return a database connection to the client's
		// Oracle database

		Connection dbconn = null;

		try {
			dbconn = DriverManager.getConnection(oracleURL, username, password);

		} catch (SQLException e) {

			System.err.println("*** SQLException:  " + "Could not open JDBC connection.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}

		return dbconn;
	}
}
