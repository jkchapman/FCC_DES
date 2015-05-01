public class KeyOperations 
{

	//Order of left rotations for generating subkeys.
	private static final int[] rotations = { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

	/*
	genKeys
	Given the initial 64bit key will produce 16 subkeys by permutating pc1, 
	splitting each round in half, performing the required rotation
	as per the above table to each half individually, and then forming the next round by taking the
	previous rounds other half. The keys are then rejoined into a single array of 16 subkeys 
	and each subkey is permutated by pc2.
	*/
	public static String[] genKeys( String fileName) throws Exception {

		String keyString = "";
		String permKeyString = "";
		String[][] splitKeys;
		String[] joinedKeys;

		keyString = FileHandling.inputToBinary( fileName);

		keyString = keyString.substring(0, 64);

		permKeyString = Permutations.permutate( keyString, Permutations.pc1);

		splitKeys = splitKey( permKeyString);

		splitKeys[0][0] = rotateLeft( splitKeys[0][0], rotations[0]);
		splitKeys[1][0] = rotateLeft( splitKeys[1][0], rotations[0]);

		for( int ii = 0; ii < 2; ii ++) {

			for( int jj = 1; jj < 16; jj++) {

				splitKeys[ii][jj] = rotateLeft( splitKeys[ii][jj - 1], rotations[jj]);

			}

		}

		joinedKeys = joinKeys( splitKeys);

		try {
			for( int ii = 0; ii < 16; ii++) {

				joinedKeys[ii] = Permutations.permutate( joinedKeys[ii], Permutations.pc2);

			}
		} catch (Exception e) {

			throw new Exception( e.getMessage());

		}
		
		return joinedKeys;

	}

	/*
		splitKey
		given the initial key, will split into two halfs and put in the first level of a 
		2 dimensional array to hold the next 15 generated subkeys.
	*/

	public static String[][] splitKey( String binaryKey) {

		String[][] splitKeys = new String[2][16];

		splitKeys[0][0] = binaryKey.substring( 0, 28);
		splitKeys[1][0] = binaryKey.substring( 28, 56);

		return splitKeys;

	}

	/*
		rotateLeft
		Preforms the required rotation and wrap around as per the number provided from the above 
		rotation table.
	*/
	public static String rotateLeft( String inString, int rotAmount) {

		for( int ii = 0; ii < rotAmount; ii++) {

			char a = inString.charAt( 0);
			
			inString = inString.substring(1, inString.length()) + Character.toString( a);

		}

		return inString;

	}

	/*
		joinKeys
		Given the double array of seperate halfs of generated key, will recombine into a single
		array of whole subkeys.
	*/

	public static String[] joinKeys( String[][] splitKeys) {

		String[] joinedKeys = new String[16];

		for( int ii = 0; ii < 16; ii ++) {

			joinedKeys[ii] = splitKeys[0][ii] + splitKeys[1][ii];

		}

		return joinedKeys;

	}

}