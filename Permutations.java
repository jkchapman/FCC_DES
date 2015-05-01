public class Permutations
{

	//First permutation to change the original 64bit key into a 56bit key.
	public static final int[] pc1 = {

		57,   49,   41,   33,   25,   17,    9,
		 1,   58,   50,   42,   34,   26,   18,
		10,    2,   59,   51,   43,   35,   27,
		19,   11,    3,   60,   52,   44,   36,
		63,   55,   47,   39,   31,   23,   15,
		 7,   62,   54,   46,   38,   30,   22,
		14,   06,   61,   53,   45,   37,   29,
		21,   13,    5,   28,   20,   12,    4
              
	};
	//Second permutation table to change the 56bit key into a 48bit key.
	public static final int[] pc2 = {

		14,   17,   11,   24,   1,   5,
         3,   28,   15,    6,  21,  10,
        23,   19,   12,    4,  26,   8,
        16,    7,   27,   20,  13,   2,
        41,   52,   31,   37,  47,  55,
        30,   40,   51,   45,  33,  48,
        44,   49,   39,   56,  34,  53,
        46,   42,   50,   36,  29,  32

	};

	//Initial permutation preformed when encoding.
	public static final int[] ip = {

		58,   50,   42,   34,   26,   18,   10,   2,
		60,   52,   44,   36,   28,   20,   12,   4,
		62,   54,   46,   38,   30,   22,   14,   6,
		64,   56,   48,   40,   32,   24,   16,   8,
		57,   49,   41,   33,   25,   17,    9,   1,
		59,   51,   43,   35,   27,   19,   11,   3,
		61,   53,   45,   37,   29,   21,   13,   5,
		63,   55,   47,   39,   31,   23,   15,   7

	};

	//Inverse of the initial permutation preformed at the end.
	public static final int[] iip = {

		40, 8, 48, 16, 56, 24, 64, 32, 
		39, 7, 47, 15, 55, 23, 63, 31, 
		38, 6, 46, 14, 54, 22, 62, 30, 
		37, 5, 45, 13, 53, 21, 61, 29, 
		36, 4, 44, 12, 52, 20, 60, 28, 
		35, 3, 43, 11, 51, 19, 59, 27, 
		34, 2, 42, 10, 50, 18, 58, 26, 
		33, 1, 41, 9, 49, 17, 57, 25 

	};

	//bit selection table for expanding a 32bit binary into 48bits.
	public static final int[] bitSelectionTable = {

		32,    1,    2,    3,    4,    5,
		 4,    5,    6,    7,    8,    9,
		 8,    9,   10,   11,   12,   13,
		12,   13,   14,   15,   16,   17,
		16,   17,   18,   19,   20,   21,
		20,   21,   22,   23,   24,   25,
		24,   25,   26,   27,   28,   29,
		28,   29,   30,   31,   32,    1

	};

	//the final permuation from the f function.
	public static final int[] p = {

		16,   7,  20,  21,
		29,  12,  28,  17,
		1,  15,  23,  26,
		5,  18,  31,  10,
		2,   8,  24,  14,
		32,  27,   3,   9,
		19,  13,  30,   6,
		22,  11,   4,  25

	};

	/*
		permutate
		Given a binary string of correct length and a permutation table, will permute all the bits into
		the new order given by the table.
	*/
	public static String permutate( String binaryKey, int[] perm) throws Exception {

		String newBinary = "";

		if( binaryKey.length() < perm.length)
			throw new Exception( "The string length " + binaryKey.length() +  " and permutation table " + perm.length + "length do not match!");

		for( int ii = 0; ii < perm.length; ii++) {

			if( perm[ii] - 1 < 0 || perm[ii] - 1 > binaryKey.length() - 1) {

				throw new Exception( "Permutation table contains permutation of " + perm[ii] + ", which is out of bounds of the binary string length of " + binaryKey.length() + ".");

			}

			newBinary += Character.toString(binaryKey.charAt( perm[ii] - 1));

		}

		return newBinary;

	}

	/*
		expand
		similar to expand except will allow lengths to be differant so the newly returned bitstring is longer than the original.
	*/

	public static String expand( String binary, int[] table) throws Exception {

		String newBinary = "";

		for( int ii = 0; ii < table.length; ii++) {

			if( table[ii] - 1 < 0 || table[ii] - 1 > binary.length() - 1) {

				throw new Exception( "Permutation table contains permutation of " + table[ii] + ", which is out of bounds of the binary string length of " + binary.length() + ".");

			}

			newBinary += Character.toString(binary.charAt( table[ii] - 1));

		}

		return newBinary;

	}

}