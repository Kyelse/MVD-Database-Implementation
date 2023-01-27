
/**
 * @description This is the Dynamic Hashing implementation using the previous 
 * binary file of Prog1A. The Dynamic Hashing will use a 10m tree (with 10 node) 
 * as pointers to the Hashing Index file of (bucket). We will index the WardCode 
 * fields, using the digit in reversed (right to left). This program will prompt 
 * the user to enter zero or more suffixes of the wardCode, then it will print out 
 * every record matches the suffix combines with the number of matches there are on the 
 * database using newly built Hash file and index tree. 
 * @author Quan Nguyen
 * @course CSC460
 * @assignment Program#1: Creating and Interpolation-Searching a Binary File
 * @instructor Lester I. McCann
 * @ta Justin Do
 * @dueDate 8 September 2021
 * @language Java 16 
 * @problem: To be honest, I should get it done before the deadline but the tree structures 
 * was so hard and confuse to debug. I forgot to increment the offsets for the bits when 
 * repopulating the new buckets so I will got a correct records number matches, but some 
 * duplicate in my tree. It took me 2 days just do debug that one line. 
 * 
 */
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class Prog2 {
	// the data stream for the file for easier I/O access
	// to minimal I/O operation
	public static RandomAccessFile dataStreamHash;
	public static RandomAccessFile dataStreamBin;
	private static int recordPrint;

	public static void main(String[] args) {
		TreeHash data = readBinary(args[0]);
		Scanner getInput = new Scanner(System.in);
		System.out.print("Please type in an input: ");
		String wardCodeString = getInput.nextLine();
		while (!wardCodeString.equals("0000000")) {
			if (isValid(wardCodeString)) {
				recordPrint = 0;
				printSearch(data, wardCodeString, wardCodeString);
				System.out.println(recordPrint + " records matched your query.");
				recordPrint = 0;
			} else {
				System.out.println("Not a valid input! Try again");
			}
			System.out.print("Please type in an input ");
			wardCodeString = getInput.nextLine();
		}
		System.out.println("Exiting...");
		getInput.close();
		// Clean-up by closing the file
		try {
			dataStreamBin.close();
			dataStreamHash.close();
		} catch (IOException e) {
			System.out.println("VERY STRANGE I/O ERROR: Couldn't close " + "the file!");
		}
		System.out.println("Exit Success!");
	}

	/*
	 * isValid() -- Determine if it is a valid WardCode number. Return true if the
	 * string is true and false if it is not.
	 * 
	 * @param input the string representation of the input
	 * 
	 * @return a boolean value. true if the string is a valid input
	 */
	private static boolean isValid(String input) {
		if (input == null || input.trim().equals("")) {
			return false;
		}
		try {
			int val = Integer.parseInt(input);
			return true;
		} catch (NumberFormatException nfe) {
			System.out.println("Should be integer entered");
			return false;
		}
	}

	/**
	 * printSearch(TreeHash data, String suffix) -- Search all of objects in data
	 * which has the same suffix as suffix. Then, print all of those objects out.
	 * 
	 * @param data             the TreeHash which contains the data of the file
	 * @param wardCodeToSearch the WardCode suffix of the desired object
	 */
	private static void printSearch(TreeHash data, String suffix, String original) {
		if (suffix.length() == 0) {
			printData(data, original);
			return;
		} else {
			int lastDigit = suffix.charAt(suffix.length() - 1) - '0';
			TreeHashUnit unit = data.get(lastDigit);
			if (unit.hasChild()) { // mean that this one is full and have a child
				printSearch(unit.getTreePointer(), suffix.substring(0, suffix.length() - 1), original);
			} else { // if the unit is not full, means that there is at most 50 wardCode left
						// we have to search through the wardCode and compare the suffix
				printRecord(unit, original);
			}
		}
	}

	/**
	 * printData(TreeHash data, String original) -- print out the DataTimeOfCall,
	 * AnimalGroupParent, and WardCodeParent of every single object in this tree
	 * (the leaf object only)
	 * 
	 * @param data     the TreeHash which is contains the location of each hash
	 *                 record which contains the location in the binary file
	 * @param original the original suffix entered by the user
	 */
	static int id = 0;

	private static void printData(TreeHash data, String original) {
		for (int i = 0; i < 10; i++) {
			TreeHashUnit unit = data.get(i);
			if (unit.hasChild()) {
				printData(unit.getTreePointer(), original);
			} else {
				printRecord(unit, original);
			}
		}
	}

	/**
	 * printRecord(TreeHashUnit unit, String original) -- print out every
	 * DataRecord's content in the TreeHashUnit which has the same suffix as
	 * original.
	 * 
	 * @param data     the Unit of the treeHash whose content will be printed out
	 * @param original the orignal suffix entered in to make an extra sure
	 *                 comparision
	 */
	private static void printRecord(TreeHashUnit unit, String original) {
		// get the hashRecord stored in the unit
		try {
			dataStreamHash.seek(unit.getHashIndex());
		} catch (IOException e) {
			System.out.println("I/O ERROR: Seems we can't reset the file " + "pointer to the start of the file.");
			System.exit(-1);
		}
		int numberOfHashRecords = unit.getCapacity();
		while (numberOfHashRecords > 0) {
			HashData newRecord = new HashData();
			newRecord.fetchObject(dataStreamHash);
			try {
				dataStreamBin.seek(newRecord.getBinIndex());
			} catch (IOException e) {
				System.out.println("I/O ERROR: Seems we can't reset the file " + "pointer to the start of the file.");
				System.exit(-1);
			}
			// Get and print out all of the dataRecord padded in with spaces for the two
			// strings
			DataRecord record = new DataRecord();
			record.fetchObject(dataStreamBin);
			numberOfHashRecords--;
			String codeString = String.valueOf(record.getWardCode());
			if (codeString.length() < original.length()) {
				continue;
			}
			if (codeString.substring(codeString.length() - original.length()).equals(original)) {
				System.out.println("[" + record.getDateTimeOfCall() + "]" + "[" + record.getAnimalGroupParent() + "]"
						+ "[" + record.getWardCode() + "]");
				recordPrint++;
			}

		}
	}

	/**
	 * readBinary() -- Read in the binary file with the name fileName and read in
	 * the content of it. The data will be put inside an ArrayList of DataRecord.
	 * Return the Array List.
	 * 
	 * @param fileName the name of the file to be read
	 */
	private static TreeHash readBinary(String fileName) {
		File fileRef = new File(fileName); // used to create the file
		File fileRefHash = new File("hashFile.bin"); // used to create the file
		if (fileRefHash.exists()) {
			fileRefHash.delete();
		}
		try {
			fileRefHash.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Prog2.dataStreamBin = new RandomAccessFile(fileRef, "rw");
			Prog2.dataStreamHash = new RandomAccessFile(fileRefHash, "rw");
		} catch (IOException e) {
			System.out
					.println("I/O ERROR: Something went wrong with the " + "creation of the RandomAccessFile object.");
			System.exit(-1);
		}
		TreeHash.dataStreamBin = dataStreamBin;
		TreeHash.dataStreamHash = dataStreamHash;
		getLengthFromFile();
		TreeHash readData = new TreeHash();
		readData.setLevel(0);
		/*
		 * Move the file pointer (which marks the byte with which the next access will
		 * begin) to the front of the file (that is, to byte 0).
		 */
		try {
			dataStreamBin.seek(0);
		} catch (IOException e) {
			System.out.println("I/O ERROR: Seems we can't reset the file " + "pointer to the start of the file.");
			System.exit(-1);
		}

		/*
		 * Read the records and display their content to the screen.
		 */
		long numberOfRecords = 0; // loop counter for reading file

		try {
			numberOfRecords = (dataStreamBin.length() - 12) / DataRecord.RecordLength;
		} catch (IOException e) {
			System.out.println("I/O ERROR: Couldn't get the file's length.");
			System.exit(-1);
		}
		DataRecord.numberOfRecord = numberOfRecords;
		long recordSoFar = 0;
		while (numberOfRecords > 0) {
			DataRecord newRecord = new DataRecord();
			newRecord.fetchObject(dataStreamBin);
			numberOfRecords--;
			if (newRecord.getWardCode() != 0) {
				readData.insert(newRecord.getWardCode(), recordSoFar * DataRecord.RecordLength);
			}
			recordSoFar++;
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
	private static void getLengthFromFile() {
		// navigate to the end of the file
		try {
			dataStreamBin.seek(dataStreamBin.length() - 12);
		} catch (IOException e) {
			System.out.println("I/O ERROR: Seems we can't navigate the file " + "pointer to the end of the file.");
			System.exit(-1);
		}

		// trying to get the data length information at the end of the file.
		int dateLength = 0, groupLength = 0, wardLength = 0;
		try {
			dateLength = dataStreamBin.readInt();
			groupLength = dataStreamBin.readInt();
			wardLength = dataStreamBin.readInt();
		} catch (IOException e) {
			System.out.println("I/O ERROR: Couldn't read from the file;\n\t" + "is the file accessible?");
			System.exit(-1);
		}
		DataRecord.setAllLength(dateLength, groupLength, wardLength);
	}
}
