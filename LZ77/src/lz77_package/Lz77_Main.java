package lz77_package;

import java.util.Scanner;

/**
 * Fernando Padilla Sergi Díaz
 */
public class Lz77_Main {

    private static int Ment = 32;
    private static int Mdes = 64;
    private static int entrySize = 16;

    public static void main(String[] args) {
        System.out.println("Starting...");
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the binary sequence length : ");
        entrySize = sc.nextInt();

        String bits = create_binary_String(entrySize);
        entrySize = bits.length();

        System.out.print("Enter the INPUT WINDOW size: ");
        Ment = sc.nextInt();
        System.out.print("Enter the SLIDING WINDOW size: ");
        Mdes = sc.nextInt();

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
                System.out.println("Compression: \t" + (float) (compressed_bits.length() / (float) decompressed_bits.length()) * 100 + "%");
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
     *
     * @param size
     * @return
     */
    private static String create_binary_String(int size) {
        String ret = "";
        for (int i = 0; i < size; i++) {
            ret = ret + Math.round(Math.random());
        }
        return ret;
    }

    /**
     *
     * @param Ment
     * @param Mdes
     * @param entrySize
     * @return
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
     *
     * @param x
     * @return
     */
    private static boolean isPowerOfTwo(int x) {
        return (x != 0) && ((x & (x - 1)) == 0);
    }
}
