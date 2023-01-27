

/**
 * @description This file create all of the basic necessary table with the
 *              relation described in the docs. The table will be created in the
 *              Oracle PL SQL DMBS.
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
import java.sql.Statement;
public class CreateTables {
	public static void main(String[] args) {
		String username = "kyelse", // Oracle DBMS username
				password = "a2124"; // Oracle DBMS password

		// create connection
		Connection dbconn = createConnection(username, password);

		// create tables
		userTable(dbconn);
		licenseTable(dbconn);
		vehicleTable(dbconn);
		jobTable(dbconn);
		departmentTable(dbconn);
		employeeTable(dbconn);
		serviceTable(dbconn);
		stateIDTable(dbconn);
		permitTable(dbconn);
		appointmentTable(dbconn);
		logTable(dbconn);

		System.out.println("all tables were created");

	}

	/**
	 * CreateConnection(String userName, String password): This function will use
	 * the given parameter to create connection to the appropriate server and later
	 * use that server to create table.
	 * 
	 * @param username
	 * @param password
	 * @return the connection to the SQL server
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

	/**
	 * This function will create client table in the Oracle database
	 * 
	 */
	private static void userTable(Connection dbconn) {
		Statement stmt = null;

		StringBuilder query = new StringBuilder();
		query.append("CREATE table client ( ");
		query.append("username varchar2(64), ");
		query.append("DOB DATE, ");
		query.append("fname varchar2(64), ");
		query.append("lname varchar2(64), ");
		query.append("primary key (username))");
		System.out.println(query.toString());
		// Grant access to outside
		String special = "GRANT SELECT ON client TO PUBLIC";
		try {
			stmt = dbconn.createStatement();
			stmt.executeUpdate(query.toString());
			System.out.println("create client table successfully");
			stmt.executeUpdate(special);
			stmt.close();
		} catch (SQLException e) {
			System.err.println(
					"*** SQLException:  " + "Could not fetch query results. (Error happen when create client table)");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/*
	 * This function will create license table
	 *
	 */
	private static void licenseTable(Connection dbconn) {
		Statement stmt = null;

		StringBuilder query = new StringBuilder();
		query.append("CREATE table license ( ");
		query.append("username varchar2(64), ");
		query.append("issuedDate DATE, ");
		query.append("LicenseID INTEGER, ");
		query.append("primary key (LicenseID), ");
		query.append("FOREIGN KEY (username) REFERENCES client(username) " + "ON DELETE CASCADE ,  "
				+ "UNIQUE (username)) ");

		System.out.println(query.toString());
		// Grant access to outside
		String special = "GRANT SELECT ON license TO PUBLIC";
		try {
			stmt = dbconn.createStatement();
			stmt.executeUpdate(query.toString());
			System.out.println("create LICENSE table successfully");
			stmt.executeUpdate(special);
			stmt.close();
		} catch (SQLException e) {
			System.err.println(
					"*** SQLException:  " + "Could not fetch query results. (Error happen when create LICENSE table)");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/*
	 * This function will create vehicle table
	 */
	private static void vehicleTable(Connection dbconn) {
		Statement stmt = null;

		StringBuilder query = new StringBuilder();
		query.append("CREATE table vehicle ( ");
		query.append("username varchar2(64), ");
		query.append("registration_number varchar2(64), ");
		query.append("issuedDate DATE, ");
		query.append("make varchar2(64), ");
		query.append("color varchar2(64), ");
		query.append("primary key (registration_number), ");
		query.append("FOREIGN KEY (username) REFERENCES client(username) " + "ON DELETE CASCADE ) ");
		System.out.println(query.toString());
		// Grant access to outside
		String special = "GRANT SELECT ON vehicle TO PUBLIC";
		try {
			stmt = dbconn.createStatement();
			stmt.executeUpdate(query.toString());
			System.out.println("create vehicle table successfully");
			stmt.executeUpdate(special);
			stmt.close();
		} catch (SQLException e) {
			System.err.println(
					"*** SQLException:  " + "Could not fetch query results. (Error happen when create VEHICLE table)");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/*
	 * This function will create employee table
	 */
	private static void employeeTable(Connection dbconn) {
		Statement stmt = null;

		StringBuilder query = new StringBuilder();
		query.append("CREATE table employee ( ");
		query.append("job_id integer, ");
		query.append("departmentNumber integer, ");
		query.append("fname varchar2(64), ");
		query.append("lname varchar2(64), ");
		query.append("EmployeeID Integer, ");
		query.append("primary key (EmployeeID), ");
		query.append("FOREIGN KEY (job_id) REFERENCES job(job_id) , "
				+ "FOREIGN KEY (departmentNumber) REFERENCES department(departmentNumber)  )");

		System.out.println(query.toString());
		// Grant access to outside
		String special = "GRANT SELECT ON employee TO PUBLIC";
		try {
			stmt = dbconn.createStatement();
			stmt.executeUpdate(query.toString());
			System.out.println("create employee table successfully");
			stmt.executeUpdate(special);
			stmt.close();
		} catch (SQLException e) {
			System.err.println(
					"*** SQLException:  " + "Could not fetch query results. (Error happen when create employee table)");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/*
	 * This method will create appointment table
	 */
	private static void appointmentTable(Connection dbconn) {
		Statement stmt = null;

		StringBuilder query = new StringBuilder();
		query.append("CREATE table appointment ( ");
		query.append("username varchar2(64), ");
		query.append("time DATE NOT NULL, ");
		query.append("EmployeeID Integer, ");
		query.append("service_type varchar2(64), ");
		query.append("success Integer, "); // 0 true, 1 false
		query.append("process Integer, "); // 0 true, 1 false
		query.append("primary key (username, time), ");
		query.append("FOREIGN KEY (username) REFERENCES client(username) " + "ON DELETE CASCADE , ");
		query.append("FOREIGN KEY (EmployeeID) REFERENCES employee(employeeID) " + " ON DELETE CASCADE, ");
		query.append("FOREIGN KEY (service_type) REFERENCES service(service_type))");

		System.out.println(query.toString());
		// Grant access to outside
		String special = "GRANT SELECT ON appointment TO PUBLIC";
		try {
			stmt = dbconn.createStatement();
			stmt.executeUpdate(query.toString());
			System.out.println("create appointment table successfully");
			stmt.executeUpdate(special);
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  "
					+ "Could not fetch query results. (Error happen when create APPOINTMENT table)");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/*
	 * This method will create service table
	 */
	private static void serviceTable(Connection dbconn) {
		Statement stmt = null;

		StringBuilder query = new StringBuilder();
		query.append("CREATE table service ( ");
		query.append("service_type varchar2(64), ");
		query.append("price integer, ");
		query.append("expiry_length integer, ");
		query.append("primary key (service_type))");
		System.out.println(query.toString());
		// Grant access to outside
		String special = "GRANT SELECT ON service TO PUBLIC";
		try {
			stmt = dbconn.createStatement();
			stmt.executeUpdate(query.toString());
			System.out.println("create service table successfully");
			stmt.executeUpdate(special);
			stmt.close();
		} catch (SQLException e) {
			System.err.println(
					"*** SQLException:  " + "Could not fetch query results. (Error happen when create SERVICE table)");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/*
	 * This function will create stateID table
	 */
	private static void stateIDTable(Connection dbconn) {
		Statement stmt = null;

		StringBuilder query = new StringBuilder();
		query.append("CREATE table stateID ( ");
		query.append("username varchar2(64), ");
		query.append("issuedDate DATE, ");
		query.append("id INTEGER, ");
		query.append("primary key (id),");
		query.append("FOREIGN KEY (username) REFERENCES client(username)  " + "ON DELETE CASCADE , "
				+ "UNIQUE (username) ) ");
		System.out.println(query.toString());
		// Grant access to outside
		String special = "GRANT SELECT ON stateID TO PUBLIC";
		try {
			stmt = dbconn.createStatement();
			stmt.executeUpdate(query.toString());
			System.out.println("create stateID table successfully");
			stmt.executeUpdate(special);
			stmt.close();
		} catch (SQLException e) {
			System.err.println(
					"*** SQLException:  " + "Could not fetch query results. (Error happen when create STATEID table)");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/*
	 * This function will create permit table
	 */
	private static void permitTable(Connection dbconn) {
		Statement stmt = null;

		StringBuilder query = new StringBuilder();
		query.append("CREATE table permit ( ");
		query.append("username varchar2(64), ");
		query.append("issuedDate DATE, ");
		query.append("id INTEGER, ");
		query.append("primary key (id), ");
		query.append("FOREIGN KEY (username) REFERENCES client(username) " + "ON DELETE CASCADE ) ");
		System.out.println(query.toString());
		// Grant access to outside
		String special = "GRANT SELECT ON permit TO PUBLIC";
		try {
			stmt = dbconn.createStatement();
			stmt.executeUpdate(query.toString());
			System.out.println("create permit table successfully");
			stmt.executeUpdate(special);
			stmt.close();
		} catch (SQLException e) {
			System.err.println(
					"*** SQLException:  " + "Could not fetch query results. (Error happen when create PERMIT table)");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/*
	 * This function will create job table
	 */
	private static void jobTable(Connection dbconn) {
		Statement stmt = null;

		StringBuilder query = new StringBuilder();
		query.append("CREATE table job ( ");
		query.append("job_id integer, ");
		query.append("job_type VARCHAR2(64), ");
		query.append("job_title VARCHAR2(64), ");
		query.append("salary NUMBER(8, 2), ");
		query.append("primary key (job_id)) ");
		System.out.println(query.toString());
		// Grant access to outside
		String special = "GRANT SELECT ON job TO PUBLIC";
		try {
			stmt = dbconn.createStatement();
			stmt.executeUpdate(query.toString());
			System.out.println("create job table successfully");
			stmt.executeUpdate(special);
			stmt.close();
		} catch (SQLException e) {
			System.err.println(
					"*** SQLException:  " + "Could not fetch query results. (Error happen when create job table)");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}
	}

	/*
	 * Create department table
	 */
	private static void departmentTable(Connection dbconn) {
		Statement stmt = null;

		StringBuilder query = new StringBuilder();
		query.append("CREATE table department ( ");
		query.append("departmentNumber integer, ");
		query.append("name VARCHAR2(64), ");
		query.append("primary key (departmentNumber))");
		System.out.println(query.toString());
		// Grant access to outside
		String special = "GRANT SELECT ON department TO PUBLIC";
		try {
			stmt = dbconn.createStatement();
			stmt.executeUpdate(query.toString());
			System.out.println("create department table successfully");
			stmt.executeUpdate(special);
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  "
					+ "Could not fetch query results. (Error happen when create department table)");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}

	}

	/*
	 * Create the log transaction table. The primary key are the foreign key to the
	 * appointment table since we have to keep 1:1 relationship to each appointment.
	 * This is just in order to match from what appointment has.
	 */
	private static void logTable(Connection dbconn) {
		Statement stmt = null;

		StringBuilder query = new StringBuilder();
		query.append("CREATE table transLog ( ");
		query.append("username varchar2(64), ");
		query.append("time DATE NOT NULL, ");
		query.append("EmployeeID Integer, ");
		query.append("charge Integer, ");
		query.append("primary key (username, time, employeeID), ");
		query.append("FOREIGN KEY (username) REFERENCES client(username) " + "ON DELETE CASCADE , ");
		query.append("FOREIGN KEY (EmployeeID) REFERENCES employee(EmployeeID) " + " ON DELETE CASCADE, ");
		query.append("FOREIGN KEY (username, time) REFERENCES appointment(username, time))");
		System.out.println(query.toString());
		// Grant access to outside
		String special = "GRANT SELECT ON transLog TO PUBLIC";
		try {
			stmt = dbconn.createStatement();
			stmt.executeUpdate(query.toString());
			System.out.println("create transLog table successfully");
			stmt.executeUpdate(special);
			stmt.close();
		} catch (SQLException e) {
			System.err.println("*** SQLException:  "
					+ "Could not fetch query results. (Error happen when create department table)");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);
		}

	}

}
