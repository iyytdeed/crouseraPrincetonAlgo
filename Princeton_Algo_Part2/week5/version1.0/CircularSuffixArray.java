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
        // step_1 generate oriStr from s
        oriStr = new String[length];
        oriStr[0] = s;
        for(int i = 1; i < length; i++){
            oriStr[i] = oriStr[i-1].substring(1) + oriStr[i-1].substring(0,1);
        }
        // step_2 get index vec
        index = new int[length];
        int[] index_aux = new int[length];
        for(int i = 0; i < length; i++){
            index[i] = i;
            index_aux[i] = i;
        }
        String[] aux = new String[length];
        sort(oriStr, aux, index_aux, 0, length-1, 0);
        /*
        for(int i = 0; i < length; i++){
            StdOut.printf("\n  %s index = %d ", oriStr[i],index[i]);
        }*/
    }
    // MSD string sort
    private void sort(String[] a, String[] aux, int[] index_aux, int lo, int hi, int d){
        if(hi <= lo) return;
        int[] count = new int[258];
        for(int i = lo; i <= hi; i++){
            count[a[i].charAt(d) + 2]++;
        }
        for(int r = 0; r < 257; r++){
            count[r+1] += count[r];
        }
        for(int i = lo; i <= hi; i++){
            aux[count[a[i].charAt(d) + 1]] = a[i];
            index_aux[count[a[i].charAt(d) + 1]] = index[i];
            count[a[i].charAt(d) + 1]++;
        }
        for(int i = lo; i <= hi; i++){
            a[i] = aux[i - lo];
            index[i] = index_aux[i-lo];
        }
        for(int r = 0; r < 256; r++){
            sort(a, aux , index_aux, lo + count[r], lo + count[r+1] - 1, d+1);
        }
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