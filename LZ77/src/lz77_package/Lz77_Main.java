package lz77_package;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Fernando Padilla, Sergi Diaz
 */
public class Lz77_Main {

    // Default parameters
    private static int Ment = 4;        // size of the search window
    private static int Mdes = 8;        // size of the sliding window
    private static int entrySize = 25;  // size of the input string
    private static String fileEntry = "";

    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {
        System.out.println("Starting...");
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter file input: ");
        fileEntry = sc.nextLine();
        System.out.print("Enter the binary sequence length : ");
        try {
            entrySize = sc.nextInt();
        } catch (InputMismatchException e) {;
            System.out.println("Length set as default " + entrySize);
            sc.next();
        }

        System.out.print("Enter the INPUT WINDOW size: ");
        try {
            Ment = sc.nextInt();;
        } catch (InputMismatchException e) {;
            System.out.println("Size set as default " + Ment);
            sc.next();
        }

        System.out.print("Enter the SLIDING WINDOW size: ");
        try {
            Mdes = sc.nextInt();;
        } catch (InputMismatchException e) {;
            System.out.println("Size set as default " + Mdes);
        }

        if (!configControl(Ment, Mdes, entrySize)) {
            System.out.println("Incorrect initial configuration.");
            System.exit(0);
        } else {
            LZ77 lz77 = new LZ77(Mdes, Ment);
            String compressed_bits = lz77.compress(bits);
            if (compressed_bits == null) {
                System.out.println("Error Compressing, Found no matches in the window.");

            } else {
                System.out.println("Compressed Bits: \t" + compressed_bits);
                String decompressed_bits = lz77.decompress(compressed_bits);
                System.out.println("Ment: " + Ment);
                System.out.println("Mdes: " + Mdes);
                System.out.println("Original Bits: \t" + bits.length());
                System.out.println("Compressed Bits: \t" + compressed_bits.length());
                System.out.println("Decompressed Bits: \t" + decompressed_bits.length());
                System.out.println("Compression: \t"
                        + (float) (compressed_bits.length() / (float) decompressed_bits.length()) * 100 + "%");
                System.out.println("Decompressed sequence: " + bits);
                if (bits.equals(decompressed_bits)) {
                    System.out.println("Compression completed");
                } else {
                    System.out.println("Compression error");
                }
            }
        }
    }

    /**
     * Returns true if compression can be performed using the given parameters.
     *
     * @param Ment input window size
     * @param Mdes sliding window size
     * @param entrySize input string size
     * @return compression can be performed.
     */
    private static boolean configControl(int Ment, int Mdes, int entrySize) {
        if (Ment > Mdes) {
            System.out.println("Ment>Mdes");
            return false;
        }
        if (Mdes + Ment > entrySize) {
            System.out.println("Mdes+Ment>entrySize");
            return false;
        }
        if ((Ment % 2 != 0) || (Mdes % 2 != 0)) {
            System.out.println("(Ment%2!=0) || (Mdes%2!=0)");
            return false;
        }
        if (!isPowerOfTwo(Mdes)) {
            return false;
        }
        if (!isPowerOfTwo(Ment)) {
            return false;
        }
        return true;
    }

    /**
     * Given an integer, returns true if the value is power of two.
     *
     * @param x integer
     * @return x is power of two
     */
    private static boolean isPowerOfTwo(int x) {
        return (x != 0) && ((x & (x - 1)) == 0);
    }

    private void readFiles(String path) {
        BufferedReader br = null;
        File file = new File("src/recources/" + path);
        file.getPath();

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader("C:\\testing.txt"));

            while ((sCurrentLine = br.readLine()) != null) {
                System.out.println(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
