
/**
 * @description This is the second part (part A) of Program#1: Creating and
 *              Interpolation-Searching a Binary File of the class CSC460. This
 *              program read in a file named file.bin. The content of the binary
 *              file is the content of file.csv whose record are sorted in
 *              ascending order by the WardCode fields. The csv file will have
 *              eight fields of information, fields type are int, double, and
 *              String. For each collumn, all values would take an equal amount
 *              of memory (ex: 8 bytes for double), but for alphanumeric
 *              collumn, we would store as the longest strings of that collumn.
 *              This program will print out the DataTimeOfCall,
 *              AnimalGroupParent, and WardCodeParent, of the first three record
 *              of the data, the middle three record, and the last three
 *              records.
 * @author Quan Nguyen
 * @course CSC460
 * @assignment Program#1: Creating and Interpolation-Searching a Binary File
 * @instructor Lester I. McCann
 * @ta Justin Do
 * @dueDate 8 September 2021
 * @language Java 16 
 * @problem 	The problem I met was that for the interpolation search, even though
 * 			    it is implemented correctly, the int data type was somehow couldn't able 
 *              to handle it. Thus, I needed to change it to long type for the value 
 *              to be used in interpolationSearch.
 * 
 */
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class Prog1B {
	public static void main(String[] args) {
		ArrayList<DataRecord> data = readBinary(args[0]);
		printData(data);
		Scanner getInput = new Scanner(System.in);
		while (true) {
			String wardCodeString = getInput.nextLine();
			try {
				int wardCodeToSearch = Integer.parseInt(wardCodeString);
				printSearch(data, wardCodeToSearch);
			} catch (NumberFormatException nfe) {
				System.out.println("Should be integer entered");
				break;
			}
		}
		getInput.close();
	}

	/**
	 * searchAll(ArrayList<DataRecord> data, int wardCodeToSearch) -- Search all of
	 * objects in data which has the same WardCode as wardCodeToSearch. Then, print
	 * all of those objects out.
	 * 
	 * @param data             the Array List which contains the data of the file
	 * @param wardCodeToSearch the WardCode value of the desired object
	 */
	private static void printSearch(ArrayList<DataRecord> data, int wardCodeToSearch) {
		ArrayList<Long> indexFound = new ArrayList<Long>();
		ArrayList<DataRecord> temp = new ArrayList<DataRecord>();
		for (int i = 0; i < data.size(); i++) {
			temp.add(data.get(i));
		}
		long index = interpolationSearch(temp, wardCodeToSearch);
		while (index != -1) {
			indexFound.add(index);
			temp.remove((int) index);
			index = interpolationSearch(temp, wardCodeToSearch);
		}

		for (long i : indexFound) {
			printRecord(data, (int) i, (int) i + 1);
		}
	}

	/**
	 * interpolationSearch(ArrayList<DataRecord> data, int wardCodeToSearch) --
	 * using interpolation search, search the desired DataRecord which has the same
	 * as WardCode as the target and return the index of that object
	 * 
	 * @param data
	 * @param wardCodeToSearch
	 * @param low
	 * @param high
	 */
	private static long interpolationSearch(ArrayList<DataRecord> data, int target) {
		long low = 0;
		long high = data.size() - 1;
		long probe;
		while ((data.get((int) high) != data.get((int) low)) && (target >= data.get((int) low).getWardCode())
				&& (target <= data.get((int) high).getWardCode())) {
			probe = low + (long) Math.ceil((target - data.get((int) low).getWardCode()) * (high - low)
					/ (data.get((int) high).getWardCode() - data.get((int) low).getWardCode()));
			if (data.get((int) probe).getWardCode() < target)
				low = probe + 1;
			else if (target < data.get((int) probe).getWardCode())
				high = probe - 1;
			else
				return probe;
		}

		if (target == data.get((int) low).getWardCode())
			return low;
		else
			return -1;
	}

	/**
	 * printData(ArrayList<DataRecord> data) -- print out the DataTimeOfCall,
	 * AnimalGroupParent, and WardCodeParent, of the first three record of the data,
	 * the middle three record, and the last three records.
	 * 
	 * @param data the ArrayList which contains the data of the binary file
	 */
	private static void printData(ArrayList<DataRecord> data) {
		if (data.size() <= 3) {
			printRecord(data, 0, data.size());
			printRecord(data, 0, data.size());
			printRecord(data, 0, data.size());
			System.out.println(DataRecord.numberOfRecord);
			return;
		}
		printRecord(data, 0, 3); // first three record
		printRecord(data, data.size() / 2 - 1, data.size() / 2);
		printRecord(data, data.size() / 2, data.size() / 2 + 1);
		if (data.size() % 2 != 0) {// or size is odd, thus, we need to print middle three
			printRecord(data, data.size() / 2 + 1, data.size() / 2 + 2);
		}
		printRecord(data, data.size() - 3, data.size()); // last three record
		System.out.println(DataRecord.numberOfRecord);

	}

	/**
	 * printRecord(ArrayList<DataRecord> data, int begin, int end) -- print out
	 * every DataRecord's content in the Array List indexed from begin to end.
	 * 
	 * @param data  the ArrayList which contains the data of the binary file
	 * @param begin the index which we would begin printing
	 * @param end   the index which we would stop printing
	 */
	private static void printRecord(ArrayList<DataRecord> data, int begin, int end) {
		for (int i = begin; i < end; i++) {
			DataRecord record = data.get(i);
			System.out.println("[" + record.getDateTimeOfCall() + "]" + "[" + record.getAnimalGroupParent() + "]" + "["
					+ record.getWardCode() + "]");
		}
	}

	/**
	 * readBinary() -- Read in the binary file with the name fileName and read in
	 * the content of it. The data will be put inside an ArrayList of DataRecord.
	 * Return the Array List.
	 * 
	 * @param fileName the name of the file to be read
	 */
	private static ArrayList<DataRecord> readBinary(String fileName) {
		File fileRef = new File(fileName); // used to create the file
		RandomAccessFile dataStream = null; // specializes the file I/O
		ArrayList<DataRecord> readData = new ArrayList<DataRecord>();
		try {
			dataStream = new RandomAccessFile(fileRef, "rw");
		} catch (IOException e) {
			System.out
					.println("I/O ERROR: Something went wrong with the " + "creation of the RandomAccessFile object.");
			System.exit(-1);
		}
		getLengthFromFile(dataStream);
		/*
		 * Move the file pointer (which marks the byte with which the next access will
		 * begin) to the front of the file (that is, to byte 0).
		 */
		try {
			dataStream.seek(0);
		} catch (IOException e) {
			System.out.println("I/O ERROR: Seems we can't reset the file " + "pointer to the start of the file.");
			System.exit(-1);
		}

		/*
		 * Read the records and display their content to the screen.
		 */
		long numberOfRecords = 0; // loop counter for reading file

		try {
			numberOfRecords = (dataStream.length() - 12) / DataRecord.RecordLength;
		} catch (IOException e) {
			System.out.println("I/O ERROR: Couldn't get the file's length.");
			System.exit(-1);
		}
		DataRecord.numberOfRecord = numberOfRecords;
		while (numberOfRecords > 0) {
			DataRecord newRecord = new DataRecord();
			newRecord.fetchObject(dataStream);
			numberOfRecords--;
			readData.add(newRecord);
		}
		// Clean-up by closing the file
		try {
			dataStream.close();
		} catch (IOException e) {
			System.out.println("VERY STRANGE I/O ERROR: Couldn't close " + "the file!");
		}
		return readData;
	}

	/**
	 * getLengthFromFile(RandomAccessFile dataStream) -- read in the dataStream last
	 * 12 bytes which would contains the 3 ints for the max length of the fields of
	 * DateTimeOfCall, AnimalGroupParent, and Ward. After read that, set the
	 * appropriate information for the class DataRecord.
	 * 
	 * @param dataStream the RandomAccessFile object which contains information of
	 *                   the binary file.
	 */
	private static void getLengthFromFile(RandomAccessFile dataStream) {
		// navigate to the end of the file
		try {
			dataStream.seek(dataStream.length() - 12);
		} catch (IOException e) {
			System.out.println("I/O ERROR: Seems we can't navigate the file " + "pointer to the end of the file.");
			System.exit(-1);
		}

		// trying to get the data length information at the end of the file.
		int dateLength = 0, groupLength = 0, wardLength = 0;
		try {
			dateLength = dataStream.readInt();
			groupLength = dataStream.readInt();
			wardLength = dataStream.readInt();
		} catch (IOException e) {
			System.out.println("I/O ERROR: Couldn't read from the file;\n\t" + "is the file accessible?");
			System.exit(-1);
		}
		DataRecord.setAllLength(dateLength, groupLength, wardLength);
	}
}
