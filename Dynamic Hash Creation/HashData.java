
/**
 * @description This is the supporting class for the Prog2A. This class 
 * contains the data type of HashData which represent each record of the 
 * Hash Bucket File as described in the spec. Each record is simple, it will 
 * has two fields, one fields contains the WardCode to search, and another field
 * that contains the location of the DataRecord that contains those WardCode field 
 * in the binary file created in Prog1A.java.
 * @author Quan Nguyen
 * @course CSC460
 * @assignment Program#2: Dynamic Hashing
 * @instructor Lester I. McCann
 * @ta Justin Do
 * @dueDate 22 September 2021
 * @language Java 16
 * 
 */
import java.io.IOException;
import java.io.RandomAccessFile;
public class HashData {
	// the wardCode are int and binIndex are long
	public static int RecordLength = 4 + 8;
	public static int numberOfRecord; 
	/**
	 * The wardCode of the DataRecord object in the binary file.
	 */
	private int wardCode; 
	/**
	 * The index of the DataRecord object in the binary file. 
	 */
	private long binIndex; 

	
	//Getters for the fields
	public void setWardCode(int newWardCode) {
		this.wardCode = newWardCode;
	}
	
	public void setBinIndex(long binIndex) {
		this.binIndex = binIndex;
	}
	
	//Setters for the fields
	public int getWardCode() {
		return wardCode;
	} 
	public long getBinIndex() {
		return binIndex;
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

		try {
			stream.writeInt(wardCode);
			stream.writeLong(binIndex);
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

		try {
			wardCode = stream.readInt();
			binIndex = stream.readLong();
		} catch (IOException e) {
			System.out.println("I/O ERROR: Couldn't read from the file;\n\t" + "is the file accessible?");
			System.exit(-1);
		}
	}

}
