package lz77_package;

import java.util.concurrent.TimeUnit;

/**
 * Fernando Padilla
 */
public class LZ77 {
    // Size of the sliding window, size of the search window.
    private final int Mdes, Ment;

    /**
     * Creates a new compressor that uses given window sizes.
     * 
     * @param Mdes  search window size
     * @param Ment  sliding window size
     */
    public LZ77(int Mdes, int Ment) {
        this.Mdes = Mdes;
        this.Ment = Ment;
    }

    /**
     * Uses Lz77 algorithm to compress the input string. Returns the compressed string.
     * 
     * @param bits  string to be compressed
     * @return      compressed string
     */
    public String compress(String bits) {
 
        long time1= System.nanoTime();
        boolean done = false;
        int i = 0;
        String Ment_str;
        String Mdes_str;
        String res = bits.substring(0, this.Mdes);
        String printable_res = bits.substring(0, this.Mdes);
        while (!done) {
            Mdes_str = bits.substring(0, this.Mdes);
            Ment_str = bits.substring(Mdes, Mdes + Ment);
            boolean found = false;
            for (int j = 0; j < this.Ment; j++) {
                int index = Mdes_str.indexOf(Ment_str);

                if (index >= 0) {
                    res = res + toBinary((Ment_str.length()), Mdes_str.length() - index, this.Ment, this.Mdes);
                    printable_res = printable_res + "(" + (Ment_str.length()) + "," + (Mdes_str.length() - index) + ")";
                    bits = bits.substring(Ment_str.length(), bits.length());
                    found = true;
                    break;
                } else {
                    Ment_str = Ment_str.substring(0, Ment_str.length() - 1);
                }
            }
            if (!found) {
                return null;
            }

            if (bits.length() < this.Ment + this.Mdes) {
                res = res + bits.substring(this.Mdes);
                printable_res = printable_res + bits.substring(this.Mdes);
                done = true;
                //System.out.println("Compression: "+printable_res);
            }
            i++;
        }
        long time2 = System.nanoTime();
        long timeSpent = time2-time1;
        System.out.println("Nano time: "+ timeSpent/1000000000.0);
        return res;
    }

    /**
     * Decompresses the input string and returns the result of such decompression.
     * Correctness depends on input string having been compressed using Lz77.
     * 
     * @param bits  string to be decompressed
     * @return      decompressed string
     */
    public String decompress(String bits) {
        String res = bits.substring(0, this.Mdes);
        String remainingBits = bits.substring(this.Mdes);
        int bitsMent = (int) (Math.log(this.Ment) / Math.log(2));
        int bitsMdes = (int) (Math.log(this.Mdes) / Math.log(2));
        int bitsTotal = bitsMent + bitsMdes;
        int forValue = (int) Math.floor((float) remainingBits.length() / (float) bitsTotal);
        for (int i = 0; i < forValue; i++) {
            if (remainingBits.length() < this.Ment) {
                break;
            }
            String Mdes_str = res.substring(res.length() - this.Mdes);
            String bocaBits = remainingBits.substring(0, bitsTotal);
            remainingBits = remainingBits.substring(bitsTotal);
            String size1 = bocaBits.substring(0, bitsMent);
            String len1 = bocaBits.substring(bitsMent);
            int size = adaptLenBinary(size1);
            int len = adaptLenBinary(len1);
            int start = this.Mdes - len;
            res = res + Mdes_str.substring(start, start + size);
        }
        res = res + remainingBits;
        return res;
    }

    /**
     *
     * @param number
     * @return
     */
    private int adaptLenBinary(String number) {
        int out = Integer.parseInt(number, 2);
        if (out == 0) {
            number = "1" + number;
            out = Integer.parseInt(number, 2);
        }
        return out;
    }

    /**
     *
     * @param n1
     * @param n2
     * @param Ment
     * @param Mdes
     * @return
     */
    private String toBinary(int n1, int n2, int Ment, int Mdes) {
        int bitsMent = (int) (Math.log(Ment) / Math.log(2));
        int bitsMdes = (int) (Math.log(Mdes) / Math.log(2));
        String output = adaptLenBinary(n1, bitsMent) + adaptLenBinary(n2, bitsMdes);
        return output;
    }

    /**
     *
     * @param number
     * @param size
     * @return
     */
    public String adaptLenBinary(int number, int size) {
        String out = Integer.toBinaryString(number);
        if (out.length() > size) {
            out = out.substring(out.length() - size);
        } else if (out.length() < size) {
            int len = out.length();
            for (int i = 0; i < (size - len); i++) {
                out = "0" + out;
            }
        }
        return out;
    }
}
