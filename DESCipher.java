import java.io.*;

public class DESCipher 
{
	
	public static void main( String[] args) {

		String keys[];
		String inputBinary;

		//Error checking: Checks to see if the required number of arguments are passed. If less, will show usage and terminate. If more, will simply ignore any extra.
		if( args.length < 4) {

			System.out.println( "USAGE: java DESCipher [E/D] [KEY FILE] [INPUT FILE] [[OUTPUT]");

		} else {

			try {

				keys = KeyOperations.genKeys( args[1]);

				inputBinary = FileHandling.inputToBinary( args[2]);
				switch( args[0].charAt(0)) {

					case 'e':
					case 'E':
						inputBinary = FileHandling.inputPadding( inputBinary);
						String encodedBinary = "";
						for( int ii = 0; ii <= inputBinary.length() - 64; ii += 64) {

							encodedBinary += DESOperations.encodeBlock( inputBinary.substring(ii, ii + 64), keys);

						}
						FileHandling.binaryToFile( encodedBinary, args[3]);
						break;
					case 'd':
					case 'D':
						String decodedBinary = "";
						for( int ii = 0; ii <= inputBinary.length() - 64; ii += 64) {

							decodedBinary += DESOperations.decodeBlock( inputBinary.substring(ii, ii + 64), keys);

						}
						decodedBinary = DESOperations.removePadding( decodedBinary);
						FileHandling.binaryToFile( decodedBinary, args[3]);
						break;
					default:
						break;
				}

			} catch ( Exception e) {

				System.out.println( "ERROR: " + e.getMessage());

			}
		}

		System.exit(0);

	}

}