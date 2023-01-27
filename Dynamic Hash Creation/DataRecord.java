/**
 * @description This is the supporting class for the Prog1A. 
 * 				This class contains the datatype of DataRecord, which each 
 * 				object will contains the 8 fields of each line in a CSV file 
 * 				with a binary format. This will help me to write and read into 
 * 				the binary file very easily.
 * @author Quan Nguyen
 * @course CSC460
 * @assignment Program#1: Creating and Interpolation-Searching a Binary File
 * @instructor Lester I. McCann
 * @ta Justin Do
 * @dueDate 1 September 2021
 * @language Java 16
 * 
 */
import java.io.IOException;
import java.io.RandomAccessFile;
public class DataRecord {
	public static long RecordLength;
	public static long numberOfRecord; 
	private static int dateLength = 0;
	private static int groupLength = 0;
	private static int wardLength = 0;
	// DateTimeOfCall,HourlyNotionalCost,IncidentNotionalCost,AnimalGroupParent,WardCode,Ward,Latitude,Longitude
	// fields of the each row of the csv
	private String dateTimeOfCall;
	private int hourlyNotionalCost;
	private int incidentNotionalCost;
	private String animalGroupParent;
	private int wardCode;
	private String ward;
	private double latitude;
	private double longitude;

	/**
	 * Set each field dateLength, groupLength, and wardLength if date, group, ward
	 * is larger than them.
	 * 
	 * @param date
	 * @param group
	 * @param ward
	 */
	public static void setAllLength(int date, int group, int ward) {
		if (date > dateLength) {
			dateLength = date;
		}
		if (group > groupLength) {
			groupLength = group;
		}
		if (ward > wardLength) {
			wardLength = ward;
		}
		// the 2 costs are int (4 bytes), the WardCode is also int (4 bits), and the
		// Latitude,Longitude are double (8 bytes)
		RecordLength = dateLength + 4 * 2 + groupLength + 4 + wardLength + 8 * 2;
	}
	// 'Setters' for the data field values

	public void setDateTimeOfCall(String newDateTimeOfCall) {
		this.dateTimeOfCall = newDateTimeOfCall;
	}

	public void setHourlyNotionalCost(int newHourlyNotionalCost) {
		this.hourlyNotionalCost = newHourlyNotionalCost;
	}

	public void setIncidentNotionalCost(int newIncidentNotionalCost) {
		this.incidentNotionalCost = newIncidentNotionalCost;
	}

	public void setAnimalGroupParent(String newAnimalGroupParent) {
		this.animalGroupParent = newAnimalGroupParent;
	}

	public void setWardCode(int newWardCode) {
		this.wardCode = newWardCode;
	}

	public void setWard(String newWard) {
		this.ward = newWard;
	}

	public void setLatitude(double newLatitude) {
		this.latitude = newLatitude;
	}

	public void setLongitude(double newLongitude) {
		this.longitude = newLongitude;
	}
	public void setNumberOfRecord(int numRecord) {
		numberOfRecord = numRecord;
	}
	// 'Getters' for the data field values
	public String getDateTimeOfCall() {
		return (dateTimeOfCall);
	}

	public int getHourlyNotionalCost() {
		return (hourlyNotionalCost);
	}

	public int getIncidentNotionalCost() {
		return (incidentNotionalCost);
	}

	public String getAnimalGroupParent() {
		return (animalGroupParent);
	}

	public int getWardCode() {
		return (wardCode);
	}

	public String getWard() {
		return (ward);
	}

	public double getLatitude() {
		return (latitude);
	}

	public double getLongitude() {
		return (longitude);
	}

	public static int getDateLength() {
		return dateLength;
	}

	public static int getGroupLength() {
		return groupLength;
	}

	public static int getWardLength() {
		return wardLength;
	} 

	/**
	 * dumpObject(stream) -- write the content of the object's fields to the file
	 * represented by the given RandomAccessFile object reference. Primitive types
	 * (e.g., int) are written directly. Non-fixed-size values (e.g., strings) are
	 * converted to the maximum allowed size before being written. The result is a
	 * file of uniformly-sized records.
	 * 
	 * @param stream the content to write in the file.
	 */

	public void dumpObject(RandomAccessFile stream) {
		// paddable strings
		String date = String.format("%-" + dateLength + "s", this.dateTimeOfCall); 
		String group = String.format("%-" + groupLength + "s", this.animalGroupParent);
		String padWard  = String.format("%-" + wardLength + "s", this.ward);
		try {
			stream.writeBytes(date);
			stream.writeInt(hourlyNotionalCost);
			stream.writeInt(incidentNotionalCost);
			stream.writeBytes(group);
			stream.writeInt(wardCode);
			stream.writeBytes(padWard);
			stream.writeDouble(latitude);
			stream.writeDouble(longitude);
		} catch (IOException e) {
			System.out.println("I/O ERROR: Couldn't write to the file;\n\t" + "perhaps the file system is full?");
			System.exit(-1);
		}
	}

	/**
	 * fetchObject(stream) -- read the content of the object's fields from the file
	 * represented by the given RandomAccessFile object reference, starting at the
	 * current file position. Primitive types (e.g., int) are read directly. To
	 * create Strings containing the text, because the file records have text stored
	 * with one byte per character, we can read a text field into an array of bytes
	 * and use that array as a parameter to a String constructor.
	 * 
	 * @param stream the content to write in the file.
	 */

	public void fetchObject(RandomAccessFile stream) {
		byte[] dateBytes = new byte[dateLength]; // file -> byte[] -> String
		byte[] groupBytes = new byte[groupLength];
		byte[] wardBytes = new byte[wardLength];

		try {
			stream.readFully(dateBytes);
			dateTimeOfCall = new String(dateBytes);
			hourlyNotionalCost = stream.readInt();
			incidentNotionalCost = stream.readInt();
			stream.readFully(groupBytes);
			animalGroupParent = new String(groupBytes);
			wardCode = stream.readInt();
			stream.readFully(wardBytes);
			ward = new String(wardBytes);
			latitude = stream.readDouble();
			longitude = stream.readDouble();
		} catch (IOException e) {
			System.out.println("I/O ERROR: Couldn't read from the file;\n\t" + "is the file accessible?");
			System.exit(-1);
		}
	}

}
