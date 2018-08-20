import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
//import edu.princeton.cs.algs4.HexDump;
//import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BurrowsWheeler {
    private static final int R = 256;
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform(){
        // step_1 readin Str
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        // step_2 find the row and write
        for (int i = 0; i < csa.length(); i++){
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        // step_3 write the transformed string
         for (int i = 0; i < s.length(); i++) {
            BinaryStdOut.write(s.charAt((csa.index(i) + s.length() - 1) % s.length()));
        }
        // step_4 close output stream
        BinaryStdOut.close();
    }
    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform(){
        // step_1 readin Str
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        int N = s.length();
        // step_2 inverseTransfrom - get the next[]
        int[] count = new int[R + 1];
        int[] next = new int[N];
        for (int i = 0; i < N; i++)
            count[s.charAt(i) + 1]++;
        for (int r = 0; r < R; r++)
            count[r + 1] += count[r];
        for (int i = 0; i < N; i++) 
            next[count[s.charAt(i)]++] = i;
        // step_2 inverseTransfrom - decode message
        for (int i = next[first], c = 0; c < N; i = next[i], c++) 
            BinaryStdOut.write(s.charAt(i));    
        // step_3 close output stream
        BinaryStdOut.close();
    }
    
    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args){
        if(args[0].equals("-")) transform();
        else if(args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Illegal command line argument"); 
    }
}