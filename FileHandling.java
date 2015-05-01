import java.io.*;

/*

A class to hold all the functions related to file io or modification of raw input / output.

*/

public class FileHandling 
{
	
	/*
		readFileToBytes
		A method to open a file and read it byte by byte into a byte array. The buffer is made to 
		the file size attained from .available(), which returns the amount if bytes left in a file (which
		should be the whole filesize as no processing has occured yet). The whiole file is then read into the buffer.
	*/
	public static byte[] readFileToBytes( String inputFile) throws Exception {

		FileInputStream inputStream = null;
		int fileSize;
		byte[] fileBuffer;

		try {

			inputStream = new FileInputStream( inputFile);
			fileSize = inputStream.available();
			fileBuffer = new byte[fileSize];

			inputStream.read( fileBuffer);
			inputStream.close();

		} catch ( IOException e) {

			try {

				if( inputStream != null)
					inputStream.close();

			} catch ( IOException e2) {}

			throw new Exception( e.getMessage());

		}

		return fileBuffer;

	}

	/*
		binaryToFile
		Take the final bitstring and outputs to a file byte by byte, by parsing each byte value 8 chars at time using
		Integer.parseInt() with a radix of 2.
	*/
	public static void binaryToFile( String binaryString, String fileName) throws Exception {

		FileOutputStream outStream = null;

		try {

			outStream = new FileOutputStream( fileName);

			for( int ii = 0; ii <= binaryString.length() - 8; ii += 8) {

				outStream.write( Integer.parseInt( binaryString.substring(ii, ii + 8), 2));

			}

			outStream.close();

		} catch ( IOException e) {

			try {

				if( outStream != null)
					outStream.close();

			} catch ( IOException e2) {}

			throw new Exception( e.getMessage());

		}

	}

	/*
		inputToBinary
		The handler function to open and read a file, available to use for both key and data input.
		Given a String file name, will open the file into a byte array using readFileToBytes, and will
		process each byte into a bit string (binary string trurns '0's into ' 's, so must be replaced).
	*/
	public static String inputToBinary( String inputFile) throws Exception {

		String inputString = "";

		try {

			byte[] inputBytes = readFileToBytes( inputFile);

			for( int ii = 0; ii < inputBytes.length; ii++) {

				int thisByte = (((int)inputBytes[ii]) + 256) % 256;
				inputString += String.format( "%8s", Integer.toBinaryString(thisByte)).replace(' ', '0');

			}

		} catch( Exception e) {

			throw new Exception( e.getMessage());

		}

		return inputString;

	}

	/*
		inputPadding
		padding to ensure all files are an exact multiple of 64 bits. If the last block is less than 64 bits, 
		will add '1' (as a a marker) followed by the required amount 0f '0's to make 64 bits.
		If the block is already 64 bits, will add a whole block of a '1' followed by 63 '0's, to ensure
		padding is consistent and removal of padding always works without corrupting data.
	*/

	public static String inputPadding( String inputBinary) {

		String paddedString = inputBinary;

		int difference = paddedString.length() % 64;

		if( difference == 0) {

			paddedString += "1";
			for( int i = 0; i < 63; i++) {

				paddedString += "0";

			}

		} else {

			paddedString += "1";
			for( int ii = 0; ii < (64 - difference - 1); ii++) {

				paddedString += "0";

			}

		}

		return paddedString;

	}

}