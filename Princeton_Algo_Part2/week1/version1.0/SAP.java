import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {
    private Digraph G;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G){
       this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        return shortest(v,w)[1];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w){
        return shortest(v,w)[0];
    }
    
    private int[] shortest(int v, int w){
        if(v>G.V()-1||v<0) throw new java.lang.IllegalArgumentException();
        if(w>G.V()-1||w<0) throw new java.lang.IllegalArgumentException();
        // step_1 - get acsV
        int[] acsV = new int[G.V()];
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        for(int i=0;i<G.V();i++){
            acsV[i] = bfsV.distTo(i);
        }
        // step_2 - get acsW
        int[] acsW = new int[G.V()];
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        for(int i=0;i<G.V();i++){
            acsW[i] = bfsW.distTo(i);
        }
        // step_3 - find common minimum acs
        int minPath = Integer.MAX_VALUE;
        int comAcs = -1;
        for(int i=0;i<G.V();i++){
            if(acsV[i] == Integer.MAX_VALUE || acsW[i] == Integer.MAX_VALUE) continue;
            if(acsV[i] + acsW[i] < minPath){
                minPath = acsV[i] + acsW[i];
                comAcs = i;
            }
        }
        // step_4 - package results;
        int[] res = new int[2];
        if(minPath == Integer.MAX_VALUE){
            res[0] = -1;
            res[1] = -1;
        }
        else{
            res[0] = comAcs;
            res[1] = minPath;
        }
        return res;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        return shortest(v,w)[1];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        return shortest(v,w)[0];
    }
    
    private int[] shortest(Iterable<Integer> v, Iterable<Integer> w){
        if(v==null||w==null) throw new java.lang.IllegalArgumentException();
        int minPath = Integer.MAX_VALUE;
        int acs = -1;
        for(int vi:v){
            for(int wi:w){
                int[] resTmp = shortest(vi,wi);
                if(resTmp[0] == -1 && resTmp[1] == -1){
                    return resTmp;
                }
                if(minPath > resTmp[1]){
                    minPath = resTmp[1];
                    acs = resTmp[0];
                }
            }
        }
        int[] res = new int[2];
        res[0] = acs;
        res[1] = minPath;
        return res;
    }

    // do unit testing of this class
    public static void main(String[] args){
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}