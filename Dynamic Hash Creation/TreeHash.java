import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * @description This is a supporting class for the Prog2 class. This class represent 
 * a single node of a tree. One node will represent a single digit of the "WardCode" fields 
 * in order to access the HashFile easily. It will have ten tree pointers and ten hash file 
 * pointers. The HashFile pointer is the pointer to the hashData object. 
 * @author Quan Nguyen
 * @course CSC460
 * @assignment Program#2: Dynamic Hashing
 * @instructor Lester I. McCann
 * @ta Justin Do
 * @dueDate 22 September 2021
 * @language Java 16
 * 
 */
/**
 * Supporting class for the TreeHash. This will represent a single unit of a
 * node/object of the TreeHash (one node contains ten units which represent each
 * digit).
 * 
 * @author Quan Nguyen
 * @course CSC460
 * @assignment Program#2: Dynamic Hashing
 * @instructor Lester I. McCann
 * @ta Justin Do
 * @dueDate 22 September 2021
 * @language Java 16
 * 
 */
class TreeHashUnit {

	/**
	 * The digit that this unit represent
	 */
	private int digit;
	/**
	 * The pointer to the HashData, which is techically a bucket
	 */
	private long hashIndex;
	/**
	 * The current capacity of a bucket
	 */
	private int capacity;
	/**
	 * The pointer to another TreeHash object.
	 */
	private TreeHash treePointer;

	/**
	 * Constructor. Initialize this TreeHashUnit object. The codeDigit passed in to
	 * marked if this object represent what digit bucket.
	 * 
	 * @param codeDigit
	 */
	public TreeHashUnit(int codeDigit) {
		this.digit = codeDigit;
		this.capacity = 0;
		this.treePointer = null;
	}

	// Setter for the fields except digit since it is should be fixed
	public void setHashIndex(long newHashIndex) {
		this.hashIndex = newHashIndex;
	}

	public void setCapacity(int newCap) {
		this.capacity = newCap;
	}

	public void setTreePointer(TreeHash newTreeNode) {
		this.treePointer = newTreeNode;
	}

	// Getters for the fields
	public int getDigit() {
		return digit;
	}

	public long getHashIndex() {
		return hashIndex;
	}

	public int getCapacity() {
		return capacity;
	}

	public TreeHash getTreePointer() {
		return this.treePointer;
	}

	/**
	 * Return true if this unit has a child and false if it is not
	 * 
	 * @return indicate if this unit has a child (to a TreeHash)
	 */
	public boolean hasChild() {
		return !(this.treePointer == null);
	}
}

public class TreeHash {
	// the data stream for the file for easier I/O access
	// to minimal I/O operation
	public static RandomAccessFile dataStreamHash;
	public static RandomAccessFile dataStreamBin;
	/**
	 * Max capacity allowed for a bucket
	 */
	private static int MAX_CAPACITY = 50;
	private ArrayList<TreeHashUnit> root;
	// the level of this node. It would be 1 if it is the root
	private int level;

	/**
	 * Constructor. Create a root node for the tree which has 10 tree pointers and
	 * 10 hash file "pointers".
	 */
	public TreeHash() {
		root = new ArrayList<TreeHashUnit>(10);
		for (int i = 0; i < 10; i++) {
			root.add(new TreeHashUnit(i));
			root.get(i).setHashIndex(i * HashData.RecordLength * MAX_CAPACITY);
		}

	}

	/**
	 * Get the level of this node (how many level deep)
	 * 
	 * @return the level of the node which indicate how many level deep is this root
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Set the level of this node
	 * 
	 * @param newLevel the newLevel to set
	 */
	public void setLevel(int newLevel) {
		this.level = newLevel;
	}

	/**
	 * insert(int wardCode) -- Insert a reference to the hash file (hashPointer)
	 * into the tree using the wardCode. Note that the insertion will follow the
	 * Dynamic Hashing principle. It will split after the "Hash Bucket" is full. In
	 * this case, 50 references. This will also "insert" the reference into the
	 * RandomAccessFile passed in.
	 * 
	 * @param suffix      the digit that the needed TreeUnit is representing
	 * @param hashPointer
	 * @param dataStream
	 */
	public void insert(int wardCode, long binIndex) {

		int suffix = wardCode % 10;
		int wardCodeLeft = wardCode / 10;
		int currentLevel = this.level;
		TreeHashUnit unit = root.get(suffix);
		// we need to reach the leaf node first in order to insert
		while (unit.hasChild()) {
			suffix = wardCodeLeft % 10;
			wardCodeLeft = wardCodeLeft / 10;
			unit = unit.getTreePointer().get(suffix);
			currentLevel++;
		}
		/**
		 * Splitting the tree when we get to the maximum capacity.
		 */
		if (unit.getCapacity() == MAX_CAPACITY) {
			repopulate(unit, currentLevel);
			suffix = wardCodeLeft % 10;
			wardCodeLeft = wardCodeLeft / 10;
			unit = unit.getTreePointer().get(suffix);
		}
		HashData hashRef = new HashData();
		hashRef.setWardCode(wardCode);
		hashRef.setBinIndex(binIndex);
		// System.out.println(wardCode + " and " + binIndex + " is inserted at " +
		// (unit.getHashIndex() + HashData.RecordLength*unit.getCapacity()) + " of the
		// hashFile");
		try {
			dataStreamHash.seek(unit.getHashIndex() + HashData.RecordLength * unit.getCapacity());
		} catch (IOException e) {
			System.out.println("I/O ERROR: Seems we can't navigate the file " + "pointer to the end of the file.");
			System.exit(-1);
		}
		hashRef.dumpObject(dataStreamHash); // add this record to the newly created bucket
		unit.setCapacity(unit.getCapacity() + 1);
	}

	/**
	 * get(int suffix) -- get the tree unit which represent the digit which is
	 * similar to index
	 * 
	 * @param suffix the digit that the needed TreeUnit is representing
	 * @return the TreeUnit representing the digit similar to the suffix
	 */
	public TreeHashUnit get(int suffix) {
		return root.get(suffix);
	}

	/**
	 * repopulate(TreeHashUnit unit, int currentLevel) -- Repopulate the HashBucket
	 * when the bucket is full by creating more bucket for the next digit at the end
	 * of the file (passed in).
	 * 
	 * @param dataStream the file stream to write on
	 * @param unit       the node which has a full HashBucket with MAX_CAPACITY
	 */
	private void repopulate(TreeHashUnit unit, int currentLevel) {
		// navigate to the end of the file
		long endFile = 0;
		try {
			endFile = dataStreamHash.length();
			dataStreamHash.seek(endFile);
		} catch (IOException e) {
			System.out.println("I/O ERROR: Seems we can't navigate the file " + "pointer to the end of the file.");
			System.exit(-1);
		}
		// Create the space for 10 new buckets in the hashFile with the suffix of
		// [0-9][unit.getDigit()] in the HashFile. Then, fill the *blank* with -1 values
		// in order to get the new endFile for later.
		unit.setTreePointer(new TreeHash());
		TreeHash newNode = unit.getTreePointer();
		currentLevel += 1;
		newNode.setLevel(currentLevel);
		for (int i = 0; i < 10; i++) {
			TreeHashUnit newUnit = newNode.get(i);
			try {
				endFile = dataStreamHash.length();
				dataStreamHash.seek(endFile);
			} catch (IOException e) {
				System.out.println("I/O ERROR: Seems we can't navigate the file " + "pointer to the end of the file.");
				System.exit(-1);
			}
			for (int j = 0; j < 50; j++) {
				HashData hashRecord = new HashData();
				hashRecord.setWardCode(-1);
				hashRecord.setBinIndex(-1);
				hashRecord.dumpObject(dataStreamHash);
			}
			newUnit.setHashIndex(endFile);
		}
		// add elements in filled bucket to new bucket
		int objectLeft = unit.getCapacity();
		int objectSoFar = 0;
		while (objectLeft > 0) {
			HashData hashRecord = new HashData();
			// Move to the beginning of the full buckets
			try {
				dataStreamHash.seek(unit.getHashIndex() + objectSoFar * HashData.RecordLength);
			} catch (IOException e) {
				System.out.println("I/O ERROR: Seems we can't navigate the file " + "pointer to the end of the file.");
				System.exit(-1);
			}
			hashRecord.fetchObject(dataStreamHash);
			objectLeft--;
			int indexToGo = (int) ((hashRecord.getWardCode() / Math.pow(10, currentLevel)) % 10);
			TreeHashUnit newUnit = newNode.get(indexToGo);
			// go to the end of this desired bucket
			try {
				dataStreamHash.seek(newUnit.getHashIndex() + HashData.RecordLength * newUnit.getCapacity());
			} catch (IOException e) {
				System.out.println("I/O ERROR: Seems we can't navigate the file " + "pointer to the end of the file.");
				System.exit(-1);
			}
			hashRecord.dumpObject(dataStreamHash); // add this record to the newly created bucket
			// increase indicating that this hashRecord is already added to the newly
			// created hashFile
			newUnit.setCapacity(newUnit.getCapacity() + 1);
			objectSoFar++;
		}

	}

}
