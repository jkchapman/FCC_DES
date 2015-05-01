import java.math.BigInteger;

/*

A class to hold all the functions to preform the DEA.

*/

public class DESOperations
{

	/*	
		encodeBlock - SWITCH FUNCTION
		Takes a 64 bit block of data (as a binary string of 1's and 0's) and the array of subkeys.
		Then proceeds to the initial permutation, splits the block into a new array of 16 levels, preforms the
		feistel on the first round, and then loops through the next 15 rounds. Due to the xor function using BigIntegers,
		any leading 0's in the bitstring will be removed, so padZeroes must be called (with the expected bitlength) to
		readd and missing leading 0's. Finally, the halfs are readded together into the correct order and the inverse of
		the initial permutation is preformed.
	*/
	public static String encodeBlock( String block, String[] key) throws Exception {

		String encodedBlock;
		String[][] splitEncodedBlock;

		if( block.length() != 64) {

			throw new Exception( "Block size is not 64 bits.");

		}

		encodedBlock = Permutations.permutate( block, Permutations.ip);

		splitEncodedBlock = splitBlock( encodedBlock);

		String temp = splitEncodedBlock[0][0];
		splitEncodedBlock[0][0] = splitEncodedBlock[1][0];
		splitEncodedBlock[1][0] = xor( temp, f( splitEncodedBlock[0][0], key[0]));
		splitEncodedBlock[1][0] = padZeroes( splitEncodedBlock[1][0], 32);

		for( int ii = 1; ii < 16; ii++) {

			splitEncodedBlock[0][ii] = splitEncodedBlock[1][ii - 1];
			splitEncodedBlock[1][ii] = xor( splitEncodedBlock[0][ii - 1], f( splitEncodedBlock[1][ii - 1], key[ii]));
			splitEncodedBlock[1][ii] = padZeroes( splitEncodedBlock[1][ii], 32);
			
		}

		encodedBlock = splitEncodedBlock[1][15] + splitEncodedBlock[0][15];

		encodedBlock = Permutations.permutate( encodedBlock, Permutations.iip);

		return encodedBlock;

	}

	/*
		decodeBlock
		Essentially the same as the encode function, however the subkeys are applied in reverse order.
	*/

	public static String decodeBlock( String block, String[] key) throws Exception {

		String decodedBlock;
		String[][] splitDecodedBlock;

		if( block.length() != 64) {

			throw new Exception( "Block size is not 64 bits.");
			
		}

		decodedBlock = Permutations.permutate( block, Permutations.ip);

		splitDecodedBlock = splitBlock( decodedBlock);

		String temp = splitDecodedBlock[0][0];
		splitDecodedBlock[0][0] = splitDecodedBlock[1][0];
		splitDecodedBlock[1][0] = xor( temp, f( splitDecodedBlock[0][0], key[15]));
		splitDecodedBlock[1][0] = padZeroes( splitDecodedBlock[1][0], 32);

		for( int ii = 1, jj = 14; ii < 16; ii++, jj--) {

			splitDecodedBlock[0][ii] = splitDecodedBlock[1][ii - 1];
			splitDecodedBlock[1][ii] = xor( splitDecodedBlock[0][ii - 1], f( splitDecodedBlock[1][ii - 1], key[jj]));
			splitDecodedBlock[1][ii] = padZeroes( splitDecodedBlock[1][ii], 32);

		}

		decodedBlock = splitDecodedBlock[1][15] + splitDecodedBlock[0][15];

		decodedBlock = Permutations.permutate( decodedBlock, Permutations.iip);

		return decodedBlock;

	}

	/*
		splitBlock
		Takes the intitial bit string and splits into two, storing into the first level of a
		16 level array to hold each round.
	*/
	public static String[][] splitBlock( String encodedBlock) {

		String[][] splitEncodedBlock = new String[2][16];

		splitEncodedBlock[0][0] = encodedBlock.substring( 0, 32);
		splitEncodedBlock[1][0] = encodedBlock.substring( 32, 64);

		return splitEncodedBlock;

	}

	/*
		f
		The feistel function, working on the right half of the round. Takes the 32 bit right half and expands to 48 bits using
		the bit selection table. Afterwards, the expanded half is xord with the current subkey (and any missing 0's are readded again).
		The result of that or is then passed into the corresponding sboxes 6bits at a time, each producing a 4 bit output to build the new
		bitstring with. This results in a new 32bit bitstring which is permutated using table p to create the output.

	*/
	public static String f( String rightBlock, String key) throws Exception {

		String outputFBlock = "";
		String fBlock = "";

		try {

			fBlock = Permutations.expand( rightBlock, Permutations.bitSelectionTable);
			
			fBlock = xor( fBlock, key);
			
			fBlock = padZeroes( fBlock, 48);

			for( int ii = 0; ii < 8; ii ++) {

				outputFBlock += SBoxes.sBoxReturn( fBlock.substring( ii * 6, ii * 6 + 6), ii);

			}

			outputFBlock = Permutations.permutate( outputFBlock, Permutations.p);
		
		} catch( Exception e) {

			throw new Exception( e.getMessage());

		}

		return outputFBlock;

	}

	/*
		xor
		A function created to easily xor bit strings (Strings of 1's and 0's). A BigInteger is created of both inputs by passing\
		the bit string and radix of 2. The second bitstring is then xord against the other bitstring by calling BigInteger's built 
		in xor function. The result is then outputted. When converting bitstring to BigInteger, any leading 0's are lost
		(ex. "0011011" -> "11011"). Therefore it is necesary to know the expected length of the bitstring so that any missing 0's
		can be readded afterwards by calling padZeroes.
	*/
	public static String xor( String a, String b) {

		BigInteger bigA = new BigInteger( a, 2);
		BigInteger bigB = new BigInteger( b, 2);
		bigA = bigA.xor( bigB);

		String output = bigA.toString( 2);

		return output;

	}

	/*
		padZeroes
		Given a bitstring produced from a xor and the expected length, will readd any 0's missing from the beginning.
	*/
	public static String padZeroes( String bitString, int target) {

		String paddedBitString = new String( bitString);
		int differance = target - bitString.length();
		if( differance != 0) {

			for( int ii = 0; ii < differance; ii++) {

				paddedBitString = "0" + paddedBitString;

			}

		}

		return paddedBitString;

	}

	/*
		removePadding
		Used to remove the padding from a fully decrypted file before being written to the new file. 
		Simply search back from the end of the bitstring until it encounters the first '1' a,d then removes
		that 1 and any further 0's.
	*/
	public static String removePadding( String decodedBinary) {

		String unpaddedBinary;
		char c = '0';
		int ii = decodedBinary.length();
		while( c != '1') {

			ii--;
			c = decodedBinary.charAt( ii);

		}

		unpaddedBinary = decodedBinary.substring( 0, ii);

		return unpaddedBinary;

	}
 

}