//import edu.princeton.cs.algs4.BinaryStdIn;
//import edu.princeton.cs.algs4.BinaryStdOut;
//import edu.princeton.cs.algs4.HexDump;
//import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
    private int length;
    private int[] index;
    private String[] oriStr;
    // circular suffix array of s
    public CircularSuffixArray(String s){
        if(s == null) throw new IllegalArgumentException();
        length = s.length();
        index = new int[length];
        for (int i = 0; i < length; i++)
            index[i] = i;
        // step_1 begin sort from s
        sort(s, 0, length-1 , 0);
    }
    private void sort(String s, int lo, int hi, int d){
        if(hi <= lo) return;
        int lt = lo, gt = hi, v = charAt(s, index[lo], d), i = lo + 1;
        while (i <= gt) {
            int t = charAt(s, index[i], d);
            if      (t < v) exch(lt++, i++);
            else if (t > v) exch(i, gt--);
            else            i++;
        }
        sort(s, lo, lt-1, d);
        if(v >= 0) sort(s, lt, gt, d+1);
        sort(s, gt+1, hi, d);
    }
    private char charAt(String s, int i, int d) {
        return s.charAt((i + d) % length);
    }
    private void exch(int i, int j) {
        int swap = index[i];
        index[i] = index[j];
        index[j] = swap;
    }
    // length of s
    public int length(){
        return length;
    }
    // returns index of ith sorted suffix
    public int index(int i){
        if (i < 0 || i >= length) throw new IllegalArgumentException();
        return index[i];
    }
    // unit testing (required)
    public static void main(String[] args){
        String str = "ABRACADABRA!";
        CircularSuffixArray csa = new CircularSuffixArray(str);
        StdOut.printf("\nstr = %s\n",str);
        StdOut.printf("len = %d\n",csa.length());
        for(int i = 0; i < csa.length(); i++){
            StdOut.printf("oriIndex = %d  index = %d\n", i, csa.index(i));
        }
    }
}