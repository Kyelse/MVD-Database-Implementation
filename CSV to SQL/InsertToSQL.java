
/**
 * @description This is a helper class for Prog3.java. This would process the data in the CSV file and insert into 
 * the relation of my own Oracle database. This would apply for all four file. I named the file 2017_Geo, 2018_Geo, etc. 
 * This would use the reference of the JDBC.java in the class website of http://u.arizona.edu/~mccann/classes/460/ 
 * for reference of insert into relation of project. 
 * Four table will be just S2017, S2018, S2019, S2021 for each year 
 * [YEAR](DNO, DNAME, SNO, SNAME, STUDENT_COUNT, MEAN_SCORE, PL3, P1, P2, P3, P4, P5) 
 * will be the relation of that. (DNO, SNO) should be the primary key. 
 * @author Quan Nguyen
 * @course CSC460
 * @assignment Program #3: JDBC
 * @instructor Lester I. McCann
 * @ta Justin Do
 * @dueDate 3 November 2021
 * @language Java 16 
 * 
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

public class InsertToSQL {
	public static void main(String[] args) {
		Connection dbconn = createSQLConnection(args);
		initMySQL(dbconn);
		String fileName = null;
		Scanner csvFile = null;
		String[] yearList = { "2017", "2018", "2019", "2021" };
		for (String year : yearList) {
			fileName = year + "_Geo.csv";
			try {
				csvFile = new Scanner(new File(fileName));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// Skip the first 8 line since they only contains metadata
			for (int i = 0; i < 8; i++) {
				csvFile.nextLine();
			}
			while (csvFile.hasNextLine()) {
				// split but keep the comma inside double quote
				String[] data = csvFile.nextLine().trim().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
				insertSQL(data, dbconn, year);
			}
			csvFile.close();
		}
		try {
			dbconn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Insert the data into the approriate table according to the year. String[] has
	 * the format of [DNO, DNAME, SNO, SNAME, STUDENT_COUNT, MEAN_SCORE, PL3, P1,
	 * P2, P3, P4, P5]. This would be all created into a string tuple and send directly into 
	 * the Oracle SQL table with the name of S[year]
	 * @param data the splited array of data
	 */
	private static void insertSQL(String[] data, Connection dbconn, String year) {
		String query = "insert into S" + year + " values (";
		for (int i = 0; i < data.length; i++) {
			String element = data[i];
			if (element.equals("*")) { // make * to NULL
				element = "NULL";
			}
			if (i == 1 || i == 3) { // get the single quote to be single quote in Oracle ''
				element = element.replaceAll("'", "''");
				element = "'" + element + "'";
			} else { //Remove comma in number and the quote around number
				if (!element.equals("NULL")) {
					element = element.replaceAll(",|\"", "");
				}
			}
			// make sure to let all of this to be upper case, make sure the user to be case insensitive
			query += element.toUpperCase() + ",";
		}
		query = query.substring(0, query.length() - 1) + ")";
		// Send the query to the DBMS, and get and display the results

		Statement stmt = null;

		try {
			stmt = dbconn.createStatement();
			stmt.executeUpdate(query);

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
	 * Create a SQL connection using the first first two argument as the username
	 * and the password. Return the object which represent the connection to the
	 * Orcale SQL data server. This piece of code is copy pasted directly from the
	 * class website.
	 * 
	 * @param args the String list which contains the username and the password
	 * @return the connection to the Oracle SQL database.
	 */
	public static Connection createSQLConnection(String[] args) {
		final String oracleURL = // Magic lectura -> aloe access spell
				"jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

		String username = null, // Oracle DBMS username
				password = null; // Oracle DBMS password

		if (args.length == 2) { // get username/password from cmd line args
			username = args[0];
			password = args[1];
		} else {
			System.out.println("\nUsage:  java JDBC <username> <password>\n"
					+ "    where <username> is your Oracle DBMS" + " username,\n    and <password> is your Oracle"
					+ " password (not your system password).\n");
			System.exit(-1);
		}

		// load the (Oracle) JDBC driver by initializing its base
		// class, 'oracle.jdbc.OracleDriver'.

		try {

			Class.forName("oracle.jdbc.OracleDriver");

		} catch (ClassNotFoundException e) {

			System.err.println("*** ClassNotFoundException:  " + "Error loading Oracle JDBC driver.  \n"
					+ "\tPerhaps the driver is not on the Classpath?");
			System.exit(-1);

		}

		// make and return a database connection to the user's
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
	 * Setup the database and the table if it is not exist yet. Four table will be
	 * just S2017, S2018, S2019, S2021 for each year [YEAR](DNO, DNAME, SNO, SNAME,
	 * STUDENT_COUNT, MEAN_SCORE, PL3, P1, P2, P3, P4, P5) will be the relation of
	 * that. (DNO, SNO) should be the primary key.
	 * 
	 * @param dbconn the connection to the SQL server
	 */
	private static void initMySQL(Connection dbconn) {
		// Send the query to the DBMS, and get and display the results

		Statement stmt = null;
		ResultSet answer = null;
		String[] query = new String[4];
		for (int i = 0; i < query.length; i++) {
			int year = i + 2017;
			if (year == 2020) {
				year = 2021;
			}
			query[i] = "CREATE table S" + year + " ( " + " dno integer, " + " dname varchar2(64)," + " sno integer, "
					+ " sname varchar2(64), " + "STU_COUNT integer, " + "SCR_MEAN integer, " + "PL3 integer, "
					+ "P1 integer, " + "P2 integer, " + "P3 integer, " + "P4 integer, " + "P5 integer, "
					+ "primary key (dno, sno)" + ")";
			System.out.println(query[i]);
		}

		for (String command : query) {
			try {

				stmt = dbconn.createStatement();
				stmt.executeUpdate(command);

				stmt.close();

			} catch (SQLException e) {
				System.err.println("*** SQLException:  " + "Could not fetch query results.");
				System.err.println("\tMessage:   " + e.getMessage());
				System.err.println("\tSQLState:  " + e.getSQLState());
				System.err.println("\tErrorCode: " + e.getErrorCode());
				System.exit(-1);

			}
		}

	}

}
