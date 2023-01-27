
/**
 * @description This is a program which will embedded the Oracle SQL (or the
 *              JBDC) using Java in order to process and answer some of the
 *              questions about the data of the Florida Department of
 *              Education's Florida Standards Assessments for four years. Some
 *              of the preprocessed data is neccessary such as using Excel to
 *              convert xsl file to csv file. After that, all of the scrubbing
 *              and preprocess of the data is done here according to the spec.
 *              The data will be processed into relations and import into my own
 *              Oracle database. From that, using the JBDC API, I would send SQL
 *              command to answer the question according to the spec. The SQL command 
 *              would be hardcoded in for non-user input. 
 * @author Quan Nguyen
 * @course CSC460
 * @assignment Program #3: JDBC
 * @instructor Lester I. McCann
 * @ta Justin Do
 * @dueDate 3 November 2021
 * @language Java 16
 * 
 */
import java.sql.*;
import java.util.Scanner;

public class Prog3 {
	public static void main(String[] args) {

		Scanner getInput = new Scanner(System.in);
		String input = "";
		String query = "";
		String[] loginInfo = { "kyelse", "a2124" }; // my own login info
		// establish the connection to the SQL server
		Connection dbconn = InsertToSQL.createSQLConnection(loginInfo);
		// the loop of taking input
		while (!input.equals("exit")) {
			printPrompt();
			input = getInput.nextLine();
			switch (input) { // case for the user to type in
			// getting the neccesary query to answer the question. We have the case for a,
			// b, c, d
			case "a":
				query = "SELECT count(*) as answer FROM kyelse.S2017, kyelse.S2021 "
						+ "WHERE kyelse.S2017.dno = kyelse.S2021.dno " + "AND kyelse.S2017.sno = kyelse.S2021.sno "
						+ "AND kyelse.S2017.SNAME<>kyelse.S2021.SNAME " + "AND kyelse.S2017.sno IS NOT NULL "
						+ "AND kyelse.S2021.sno IS NOT NULL";
				break;
			case "b":
				query = "SELECT s1.dname as \"District\", s2.sname as \"School\" "
						+ "FROM kyelse.S2019 s1,  kyelse.S2021 s2 " + "WHERE s1.dno =  s2.dno "
						+ "AND  s1.sno =  s2.sno " + "AND s1.P1 < s2.P1 " + "AND s1.P2 < s2.P2 " + "AND s1.P4 >  s2.P4 "
						+ "AND s1.P5 >  s2.P5";
				break;
			case "c":
				String year = getInputYear(getInput);
				String district;
				System.out.println("Please type in your district name");
				district = getInput.nextLine();
				processQueryC(dbconn, year, district);
				input = "c";
				System.out.println();
				continue;
			case "d":
				boolean isInputTrue = false;
				String firstYear = null;
				String secondYear = null;
				while (!isInputTrue) {
					System.out.println("Type in two different years of 2017, 2018, 2019, 2021");
					System.out.println("Type in your first year please: "); 
					firstYear = getInputYear(getInput);
					System.out.println("Type in your second year please: ");
					secondYear = getInputYear(getInput);
					if (firstYear.equals(secondYear)) {
						System.out.println("Please type in two different years");
					} else {
						isInputTrue = true; 
					}
				}

				query = "SELECT DISTINCT s1.dname as \"District\", s1.sname as \"School\" FROM kyelse.S" + firstYear + " s1, kyelse.S"
						+ secondYear + " s2 WHERE s1.PL3 = 100 AND s2.PL3=100";
				break;
			case "exit":
				break;
			default:
				System.out.println("Please only type 'a', 'b', 'c', 'd' or 'exit' for exit");
				query = "";
				break;
			} // process query if the query is not empty (or valid a, b, c, d typed in)
			if (!query.equals("")) {
				processQuery(input, query, args, dbconn);
				System.out.println();
				query = "";
			}

		}

		// close the connection after everything is done
		try {
			dbconn.close();
		} catch (SQLException e) {

			System.err.println("*** SQLException:  " + "Could not fetch query results.");
			System.err.println("\tMessage:   " + e.getMessage());
			System.err.println("\tSQLState:  " + e.getSQLState());
			System.err.println("\tErrorCode: " + e.getErrorCode());
			System.exit(-1);

		}
	}

	/**
	 * Get an input of the user for the year of 2017, 2018, 2019, or 2021 from the
	 * standard input. This is a wrapper function since many times in query b and d.
	 * 
	 * @param scanner the input io to take the input
	 */
	private static String getInputYear(Scanner scanner) {
		boolean isInputTrue = false;
		String year = "";
		while (!isInputTrue) {
			System.out.println("Please type in your year (2017, 2018, 2019, 2021)");
			year = scanner.nextLine();
			if (year.equals("2017") || year.equals("2018") || year.equals("2019") || year.equals("2021")) {
				isInputTrue = true;
			} else {
				System.out.println("Invalid year");
			}
		}
		return year;
	}

	/**
	 * Print out the prompt for the four questions.
	 */
	private static void printPrompt() {
		System.out.println("Please choose question to answer. Type exit for exit the program.");
		System.out.println(
				"(a) How many schools are listed with different school names in 2021 than they were listed in 2017? To\r\n"
						+ "be counted, a school must appear in both years.\r\n"
						+ "\n(b) Considering only the years 2019 and 2021 (pre- and mid- pandemic), and only the schools for which\r\n"
						+ "level percentage data is available for both years, what are the district names and school names\r\n"
						+ "of the schools that reported both decreases in percentages in both Levels 4 & 5 and increases in\r\n"
						+ "percentages in both Levels 1 & 2 in 2021 as compared to 2019? "
						+ "\n\n(c) For a year and a case-insensitive district name given by the user, if the district has at least 10\r\n"
						+ "schools in the report, list in descending order the five schools with the highest combined level 4\r\n"
						+ "and 5 percentages. Display those schools' numbers, names, the two individual percentages, and\r\n"
						+ "their sum.\r\n"
						+ "\n(d) Parents and students would like to know the most prestige schools either for fun, or for \r\n"
						+ "the goal to their child. Display all of the schools with the district which keep their Percentage in\r\n"
						+ "Level 3 or Above at 100% in the two years which the user want to see. \r\n");
	}

	/**
	 * Processing the query of the SQL using embedded JDBC. This would call the
	 * function createSQLConnection from the InsertToSQL file in order to properly
	 * 
	 * @param input  The input of the user, must be either 'a', 'b', or 'd'
	 * @param query  The predetermined SQL query to answer the question from the
	 *               user
	 * @param args   Login informations of my own SQL in order to make sure that the
	 *               program works with my own database no problem
	 * @param dbconn the connection to the SQL server
	 */
	private static void processQuery(String input, String query, String[] args, Connection dbconn) {
		Statement stmt = null;
		ResultSet answer = null;
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);
			if (answer != null) {
				System.out.print("\nThe answer for your question (" + input + ") are: \n");
				switch (input) {
				case "a":
					answer.next();
					System.out.println(answer.getInt("answer") + "\n");
					break;
				case "b":
				case "d":
					ResultSetMetaData answermetadata = answer.getMetaData();

					for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
						System.out.printf("%-64s", answermetadata.getColumnName(i));
					}
					System.out.println();

					// Use next() to advance cursor through the result
					// tuples and print their attribute values

					while (answer.next()) {
						System.out.printf("%-64s %-64s\n", answer.getString("District"), answer.getString("School"));

					}
					break;
				}
			}
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
	 * A simple helper for the query C since there need to be a check before execute
	 * the actual answer for the query. The queryCheck is the query which check if 
	 * the district has more than 10 school
	 * 
	 * @param dbconn   the connection to the SQL server
	 * @param year     the year that the user input for the query
	 * @param district the district that the user input for the query
	 */
	private static void processQueryC(Connection dbconn, String year, String district) {
		Statement stmt = null;
		ResultSet answer = null;
		String query = "SELECT dname as \"District\", sname as \"School\", p4 as \"P4 Score\", p5 as \"P5 Score\",  \"Combined Score\" "
				+ "FROM (SELECT dno, sno, dname, sname, p4, p5, sum(p4+p5) as \"Combined Score\" " + "FROM S" + year
				+ " WHERE dname = '" + district.toUpperCase() + "' AND p5 IS NOT NULL "
				+ "GROUP BY dno, sno, dname, sname, p4, p5" + " ORDER BY sum(p4+p5) desc) " + "WHERE rownum < 6"; 
		// check if district has more than 10 schools or not
		String queryCheck = "SELECT count(*) as answer FROM kyelse.S" + year + " WHERE dname = '"
				+ district.toUpperCase() + "'";
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(queryCheck);
			if (answer != null) {
				answer.next();
				int count = answer.getInt("answer");
				if (count < 10) {
					System.out.println("I'm sorry, but district " + district.toUpperCase() + " has just "
							+ answer.getInt("answer") + " schools.");
				} else {
					answer = stmt.executeQuery(query);
					if (answer != null) {
						ResultSetMetaData answermetadata = answer.getMetaData();
						// Using printf to format the answer
						for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
							if (i != 2) {
								System.out.printf("%-15s", answermetadata.getColumnName(i));
							} else {
								System.out.printf("%-64s", answermetadata.getColumnName(i));
							}

						}

						System.out.println();

						// Use next() to advance cursor through the result
						// tuples and print their attribute values

						while (answer.next()) {
							System.out.printf("%-15s %-64s %-15s %-15s %-15s\n", answer.getString("District"),
									answer.getString("School"), answer.getInt("P4 Score"), answer.getInt("P5 Score"),
									answer.getInt("Combined Score"));

						}
					}
				}
			}
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

}
