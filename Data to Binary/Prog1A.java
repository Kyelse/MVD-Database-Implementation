
/**
 * @description This is the first part (part A) of Program#1: Creating and
 *              Interpolation-Searching a Binary File of the class CSC460. This
 *              program read in a file named file.csv and output a binary file
 *              named file.bin. The content of the binary file is the content of
 *              file.csv whose record are sorted in ascending order by the
 *              WardCode fields. The csv file will have eight fields of
 *              information, fields type are int, double, and String. For each
 *              collumn, all values would take an equal amount of memory (ex: 8
 *              bytes for double), but for alphanumeric collumn, we would store
 *              as the longest strings of that collumn.
 * @author Quan Nguyen
 * @course CSC460
 * @assignment Program#1: Creating and Interpolation-Searching a Binary File
 * @instructor Lester I. McCann
 * @ta Justin Do
 * @dueDate 8 September 2021
 * @language Java 16
 * 
 */
import java.util.*;
import java.io.*;

public class Prog1A {
	public static void main(String[] args) {
		String[] getFileName = args[0].split("/");
		String fileName = getFileName[getFileName.length - 1];
		String fileToCreate = fileName.split("\\.")[0] + ".bin";
		Scanner csvFile = null;
		try {
			csvFile = new Scanner(new File(args[0]));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<DataRecord> data = processFile(csvFile);
		csvFile.close();
		writeToBin(data, fileToCreate);
	}



	/**
	 * writeToBin(ArrayList<DataRecord> data)-- Write the content of each DataRecord
	 * in the Array List to the binary file named in fileToCreate. The data will be
	 * sorted based on the WardCode.
	 * 
	 * @param data         the ArrayList which contains every DataRecord in the csv
	 *                     file.
	 * @param fileToCreate the name of the binary file of which the data will be
	 *                     written to.
	 */
	private static void writeToBin(ArrayList<DataRecord> data, String fileToCreate) {
		Collections.sort(data, new Comparator<DataRecord>() {
			@Override
			public int compare(DataRecord z1, DataRecord z2) {
				if (z1.getWardCode() > z2.getWardCode())
					return 1;
				if (z1.getWardCode() < z2.getWardCode())
					return -1;
				return 0;
			}
		});
		File fileRef = new File(fileToCreate); // used to create the file
		if (fileRef.exists()) {
			fileRef.delete();
		}
		try {
			fileRef.createNewFile();
		} catch (IOException e1) {
			System.out.println("Somehow I cannot create new file");
			e1.printStackTrace();
		}
		RandomAccessFile dataStream = null; // specializes the file I/O
		try {
			dataStream = new RandomAccessFile(fileRef, "rw");
		} catch (IOException e) {
			System.out
					.println("I/O ERROR: Something went wrong with the " + "creation of the RandomAccessFile object.");
			System.exit(-1);
		}
		// writing the data in
		for (DataRecord row : data) {
			row.dumpObject(dataStream);
		}
		// write the three maximum length of the three string fields
		try {
			dataStream.writeInt(DataRecord.getDateLength());
			dataStream.writeInt(DataRecord.getGroupLength());
			dataStream.writeInt(DataRecord.getWardLength());
		} catch (IOException e) {
			System.out.println("I/O ERROR: Couldn't write to the file;\n\t" + "perhaps the file system is full?");
			System.exit(-1);
		}
		// Clean-up by closing the file
		try {
			dataStream.close();
		} catch (IOException e) {
			System.out.println("VERY STRANGE I/O ERROR: Couldn't close " + "the file!");
		}
	}

	/**
	 * processFile(Scanner csvFile) -- Processing the whole file. It would split the
	 * line into an array with 8 elements based on comma (but ignore comma in
	 * quotes). Then, for each line, the data in the correct format (correct fields
	 * with type) will be written in a DataRecord file which is added into an
	 * ArrayList. Return the ArrayList which contains DataRecord which contains
	 * content of the file
	 * 
	 * @param csvFile a Scanner object which contains the content of the csvFile.
	 * @return the ArrayList which contains DataRecord which contains content of the
	 *         file
	 */
	private static ArrayList<DataRecord> processFile(Scanner csvFile) {
		while (csvFile.nextLine().trim().equals("")) {
			continue;
		}
		ArrayList<DataRecord> record = new ArrayList<DataRecord>();
		while (csvFile.hasNextLine()) {
			String line = csvFile.nextLine().trim();
			if (line.equals("")) {
				continue;
			}
			String[] afterSplit = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			DataRecord rowToAdd = new DataRecord();
			rowToAdd.setDateTimeOfCall(afterSplit[0].trim());
			// HourlyNotionalCost,IncidentNotionalCost, and WardCode are integer
			rowToAdd.setHourlyNotionalCost(formatIntNumber(afterSplit, 1));
			rowToAdd.setIncidentNotionalCost(formatIntNumber(afterSplit, 2));
			rowToAdd.setAnimalGroupParent(afterSplit[3].trim());
			rowToAdd.setWardCode(formatIntNumber(afterSplit, 4));
			rowToAdd.setWard(afterSplit[5].trim());
			// Latitude and longitude are double
			rowToAdd.setLatitude(formatDoubleNumber(afterSplit, 6));
			rowToAdd.setLongitude(formatDoubleNumber(afterSplit, 7));
			DataRecord.setAllLength(afterSplit[0].length(), afterSplit[3].length(), afterSplit[5].length());
			record.add(rowToAdd);
		}
		return record;
	}

	/**
	 * formatIntNumber(String[] afterSplit, int index, boolean isInt): change an
	 * integer string representation into an integer in certain index and return it
	 * 
	 * @param afterSplit the array of string
	 * @param index      index of the numeric string
	 * @return the value of the string representation
	 */
	private static int formatIntNumber(String[] afterSplit, int index) {
		if (afterSplit[index].isEmpty() || afterSplit[index].toLowerCase().equals("null")) {
			return 0;
		}
		if (index == 4) { // return the integer but not the "E"
			return Integer.parseInt(afterSplit[index].substring(1));
		} else {
			return Integer.parseInt(afterSplit[index]);
		}

	}

	/**
	 * formatDoubleNumber(String[] afterSplit, int index, boolean isInt): change a
	 * double string representation into a double in certain index and return it.
	 * 
	 * @param afterSplit the array of string
	 * @param index      index of the numeric string
	 * @return the value of the string representation
	 */
	private static double formatDoubleNumber(String[] afterSplit, int index) {
		if (afterSplit[index].isEmpty() || afterSplit[index].toLowerCase().equals("null")) {

			return 0;
		}
		return Double.parseDouble(afterSplit[index]);
	}

}
