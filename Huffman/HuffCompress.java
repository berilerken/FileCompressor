package Huffman;

import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.PriorityQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class HuffCompress {

    public static PriorityQueue<TREE> pq = new PriorityQueue<>();
    public static int[] freq = new int[300];
    public static String[] str = new String[300];
    public static int exbits;
    public static byte bt;
    public static int cnt; // number of differents

    //*****************************************************************
    //tree class
    static class TREE implements Comparable<TREE> {
        TREE left_child;
        TREE right_child;
        public String deb;
        public int Bite;
        public int Freqnc;

        public int compareTo(TREE T) {
            if (this.Freqnc < T.Freqnc)
                return -1;
            if (this.Freqnc > T.Freqnc)
                return 1;
            return 0;
        }
    }

    static TREE Root;

    //*****************************************************************
    //first we use the"clearing()" method for make free the freq & str arrays
    public static void clearing() {
        int i;
        cnt = 0;
        if (Root != null)
            freeTree(Root);
        for (i = 0; i < 300; i++) {
            freq[i] = 0;
            str[i] = "";
        }
        pq.clear();
    }
    //*****************************************************************
    //clear the tree
    public static void freeTree(TREE walk) {

        if (walk.left_child == null && walk.right_child == null) {
            walk = null;
            return;
        }
        if (walk.left_child != null)
            freeTree(walk.left_child);
        if (walk.right_child != null)
            freeTree(walk.right_child);
    }
    //*****************************************************************
    public static void frequency(String fname) {

        Byte bt;

        //openning the file
        File file = new File(fname);
        try {
            FileInputStream file_input = new FileInputStream(file);
            DataInputStream data_in = new DataInputStream(file_input);
            while (true) {
                try {
                    //reading the data in byte
                    bt = data_in.readByte();
                    freq[byteToBinary(bt)]++;
                } catch (EOFException eof) {
                    System.out.println("End of File");
                    break;
                }
            }
            file_input.close();
            data_in.close();
        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }
        file = null;
    }
    //*****************************************************************
    //byte to binary conversion
    public static int byteToBinary(Byte b) {
        int ret = b;
        if (ret < 0) {
            ret = ~b;
            ret = ret + 1;
            ret = ret ^ 255;
            ret += 1;
        }
        return ret;
    }
    //*****************************************************************
    //Making all the nodes in a priority Q making the tree
    public static void makeTree() {
        //first we clear the pqueue
        pq.clear();

        for (int i = 0; i < 300; i++) {
            if (freq[i] != 0) {
                //making the tree nodes
                TREE tmp = new TREE();
                tmp.Bite = i;
                tmp.Freqnc = freq[i];
                tmp.left_child = null;
                tmp.right_child = null;
                pq.add(tmp);
                cnt++;
            }

        }
        TREE Tmp1, Tmp2;

        if (cnt == 0) {
            return;
        }
        else if (cnt == 1) {
            for (int i = 0; i < 300; i++)
                if (freq[i] != 0) {
                    str[i] = "0";
                    break;
                }
            return;
        }

        // will there be a problem if the file is empty
        // a bug is found if there is only one character
        while (pq.size() != 1) {
            TREE Temp = new TREE();
            Tmp1 = pq.poll();
            Tmp2 = pq.poll();
            Temp.left_child = Tmp1;
            Temp.right_child = Tmp2;
            Temp.Freqnc = Tmp1.Freqnc + Tmp2.Freqnc;
            pq.add(Temp);
        }
        //first in first out-poll() method
        Root = pq.poll();
    }

    //*****************************************************************
    //Depth-First Search algorithm is used on the TREE for free the nodes
    public static void dfs(TREE walk, String st) {
        walk.deb = st;
        if ((walk.left_child == null) && (walk.right_child == null)) {
            str[walk.Bite] = st;
            return;
        }
        if (walk.left_child != null)
            dfs(walk.left_child, st + "0");
        if (walk.right_child != null)
            dfs(walk.right_child, st + "1");
    }
    //*****************************************************************
    //fakefile is used to make a fake file of our opened file with binary
    public static void fakeFile(String fname) {

        File filei, fileo;
        int i;

        filei = new File(fname);
        fileo = new File("fakefile.txt");
        try {
            FileInputStream file_input = new FileInputStream(filei);
            DataInputStream data_in = new DataInputStream(file_input);
            PrintStream ps = new PrintStream(fileo);

            while (true) {
                try {
                    bt = data_in.readByte();
                    ps.print(str[byteToBinary(bt)]);
                } catch (EOFException eof) {
                    System.out.println("End of File");
                    break;
                }
            }

            file_input.close();
            data_in.close();
            ps.close();

        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }

        filei = null;
        fileo = null;
    }
    //*****************************************************************
    //realfile is used to make a real compressed file of our fake file
    public static void realFile(String fname, String fname1) {
        File filei, fileo;
        int i = 10;
        Byte btt;

        filei = new File(fname);
        fileo = new File(fname1);

        try {
            FileInputStream file_input = new FileInputStream(filei);
            DataInputStream data_in = new DataInputStream(file_input);
            FileOutputStream file_output = new FileOutputStream(fileo);
            DataOutputStream data_out = new DataOutputStream(file_output);

            data_out.writeInt(cnt);
            for (i = 0; i < 256; i++) {
                if (freq[i] != 0) {
                    btt = (byte) i;
                    data_out.write(btt);
                    data_out.writeInt(freq[i]);
                }
            }
            long texbits;
            texbits = filei.length() % 8;
            texbits = (8 - texbits) % 8;
            exbits = (int) texbits;
            data_out.writeInt(exbits);
            while (true) {
                try {
                    bt = 0;
                    byte ch;
                    for (exbits = 0; exbits < 8; exbits++) {
                        ch = data_in.readByte();
                        bt *= 2;
                        if (ch == '1')
                            bt++;
                    }
                    data_out.write(bt);

                } catch (EOFException eof) {
                    int x;
                    if (exbits != 0) {
                        for (x = exbits; x < 8; x++) {
                            bt *= 2;
                        }
                        data_out.write(bt);
                    }

                    exbits = (int) texbits;
                    System.out.println("extrabits: " + exbits);
                    System.out.println("End of File");
                    break;
                }
            }
            data_in.close();
            data_out.close();
            file_input.close();
            file_output.close();
            System.out.println("output file's size: " + fileo.length());

        } catch (IOException e) {
            System.out.println("IO exception = " + e);
        }
        filei.delete();
        filei = null;
        fileo = null;
    }

    //*****************************************************************
    //In GUI_Main class it called this(huffCompressing) method then it continue with methods inside of that method
    public static void huffCompressing(String arg) {
        clearing(); //for clear everything
        frequency(arg); // calculating the frequency
        makeTree(); // making the nodes
        if (cnt > 1)
            dfs(Root, ""); // dfs algorithm to visit nodes
        fakeFile(arg); // fake file has the binary input of our file and it creates a fakefile.txt file
        realFile("fakefile.txt", arg + ".huff"); // making the real compressed file
        clearing();
    }
}