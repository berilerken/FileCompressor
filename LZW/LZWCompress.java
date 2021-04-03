package LZW;
import java.util.HashMap;
import java.util.Map;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.EOFException;
import java.io.IOException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class LZWCompress {


    public static String bigs;
    public static int btsz;

    //***********************************************
    //calculate the length of the dictionary with map
    public static void LZWdic(String fileis) {
        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        //ascii table is used in dictionary
        int dictSize = 256;
        for (int i = 0; i < 256; i++)
            dictionary.put("" + (char) i, i);
        int mpsz = 256;
        String x = "";


        File filei = new File(fileis);

        try {
            FileInputStream file_input = new FileInputStream(filei);
            DataInputStream data_in = new DataInputStream(file_input);

            Byte c;
            int ch;
            while (true) {
                try {
                    c = data_in.readByte();
                    ch = byteToInt(c);
                    String xc = x + (char) ch;
                    if (dictionary.containsKey(xc))
                        x = xc;
                    else {
                        if (mpsz < 100000) {
                            dictionary.put(xc, dictSize++);
                            mpsz += xc.length();
                        }
                        x = "" + (char) ch;
                    }
                } catch (EOFException eof) {
                    System.out.println("End of File");
                    break;
                }
            }
            file_input.close();
            data_in.close();
        } catch (IOException e) {
            System.out.println("IO exception = " + e);
        }

        //if file is empty
        if (dictSize <= 1) {
            btsz = 1;
        } else {
            btsz = 0;
            long i = 1;
            while (i < dictSize) {
                i *= 2;
                btsz++;
            }
        }
        filei = null;
    }
    //***********************************************
    //converting byte to integer
    public static int byteToInt(Byte b) {
        int ret = b;
        if (ret < 0) {
            ret += 256;
        }
        return ret;
    }
    //***********************************************
    //compressing part of the code
    public static void compress(String fileis) {
        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        int dictSize = 256;
        bigs = "";
        for (int i = 0; i < 256; i++)
            dictionary.put("" + (char) i, i);
        int mpsz = 256;
        String x = "";
        String fileos = fileis + ".lzw";

        File filei = new File(fileis);
        File fileo = new File(fileos);

        try {
            FileInputStream file_input = new FileInputStream(filei);
            DataInputStream data_in = new DataInputStream(file_input);
            FileOutputStream file_output = new FileOutputStream(fileo);
            DataOutputStream data_out = new DataOutputStream(file_output);

            data_out.writeInt(btsz);
            Byte c;
            int ch;
            while (true) {
                try {
                    c = data_in.readByte();
                    ch = byteToInt(c);
                    String xc = x + (char) ch;
                    if (dictionary.containsKey(xc))
                        x = xc;
                    else {
                        bigs += intToBin(dictionary.get(x));
                        while (bigs.length() >= 8) {
                            data_out.write(stringToByte(bigs.substring(0, 8)));
                            bigs = bigs.substring(8, bigs.length());
                        }

                        if (mpsz < 100000) {
                            dictionary.put(xc, dictSize++);
                            mpsz += xc.length();
                        }
                        x = "" + (char) ch;
                    }
                } catch (EOFException eof) {
                    System.out.println("End of File");
                    break;
                }
            }

            if (!x.equals("")) {
                bigs += intToBin(dictionary.get(x));
                while (bigs.length() >= 8) {
                    data_out.write(stringToByte(bigs.substring(0, 8)));
                    bigs = bigs.substring(8, bigs.length());
                }
                if (bigs.length() >= 1) {
                    data_out.write(stringToByte(bigs));
                }
            }
            data_in.close();
            data_out.close();
            file_input.close();
            file_output.close();
        } catch (IOException e) {
            System.out.println("IO exception = " + e);
        }
        filei = null;
        fileo = null;
    }
    //***********************************************
    //converting int to binary
    public static String intToBin(int inp) {
        String ret = "", r1 = "";
        if (inp == 0)
            ret = "0";
        int i;
        while (inp != 0) {
            if ((inp % 2) == 1)
                ret += "1";
            else
                ret += "0";
            inp /= 2;
        }
        for (i = ret.length() - 1; i >= 0; i--) {
            r1 += ret.charAt(i);
        }
        while (r1.length() != btsz) {
            r1 = "0" + r1;
        }
        return r1;
    }
    //***********************************************
    //converting string to byte
    public static Byte stringToByte(String in) {

        int i, n = in.length();
        byte ret = 0;
        for (i = 0; i < n; i++) {
            ret *= 2;
            if (in.charAt(i) == '1')
                ret++;
        }
        for (n = 0; n < 8; n++)
            ret *= 2;
        Byte r = ret;
        return r;
    }


    public static void LZWCompressing(String arg) {
        btsz = 0;
        bigs = "";
        LZWdic(arg);
        compress(arg);
        btsz = 0;
        bigs = "";
    }
}