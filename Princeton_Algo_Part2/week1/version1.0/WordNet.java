import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;

public class WordNet {
    private class Noun implements Comparable<Noun>{
        private String name;
        private ArrayList<Integer> id = new ArrayList<Integer>();
        public Noun(String noun){
            this.name = noun;
        }
        @Override
        public int compareTo(Noun that){
            return this.name.compareTo(that.name);
        }
        public ArrayList<Integer> getId(){
            return this.id;
        }
        public void addId(Integer x){
            this.id.add(x);
        }
    }
    private SET<Noun> nounSet;
    private ArrayList<String> idList;
    private Digraph G;
    private SAP sap;
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms){
        // Step_1 - init
        idList = new ArrayList<String>();
        nounSet = new SET<Noun>();
        // Step_2 - read synset from file synsetFileName.txt
        In inSynset = new In(synsets);
        int maxVertex = 0;
        String line = inSynset.readLine();
        while(line!=null){
            String[] synset = line.split(",");
            Integer synsetId = Integer.parseInt(synset[0]);
            String[] synsetName = synset[1].split(" ");
            for(String nounName:synsetName){
                Noun nounAdd = new Noun(nounName);
                if(nounSet.contains(nounAdd)){
                    if(!nounSet.ceiling(nounAdd).id.contains(synsetId)) nounSet.ceiling(nounAdd).addId(synsetId);
                }
                else{
                    nounAdd.addId(synsetId);
                    nounSet.add(nounAdd);
                }
            }
            idList.add(synset[1]);
            maxVertex++;
            line = inSynset.readLine();
        }
        // Step_3 - read hypernyms from file hypernymsFileName.txt
        In inHypernyms = new In(hypernyms);
        G = new Digraph(maxVertex);
        line = inHypernyms.readLine();
        boolean[] isNotRoot = new boolean[maxVertex];
        while(line!=null){
            String[] hypernym = line.split(",");
            int v = Integer.parseInt(hypernym[0]);
            isNotRoot[v] = true;
            for(int i=1;i<hypernym.length;i++){
                G.addEdge(v, Integer.parseInt(hypernym[i]));
            }
            line = inHypernyms.readLine();
        }
        int rootCnt = 0;
        for(boolean notRoot:isNotRoot){
            if(notRoot == false) rootCnt++;
        }
        if(rootCnt > 1) {
            StdOut.println(rootCnt);
            throw new java.lang.IllegalArgumentException();
        }
        DirectedCycle dCyc = new DirectedCycle(G);
        if(dCyc.hasCycle()) throw new java.lang.IllegalArgumentException();
        // step_4 - construct sap
        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns(){
        Queue<String> que = new Queue<String>();
        for(Noun noun:nounSet){
            que.enqueue(noun.name);
        }
        return que;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word){
        if(word == null) throw new java.lang.IllegalArgumentException();
        Noun nounIn = new Noun(word);
        return nounSet.contains(nounIn);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB){
        if (!isNoun(nounA)){  
            throw new java.lang.IllegalArgumentException();  
        }  
        if (!isNoun(nounB)){  
            throw new java.lang.IllegalArgumentException();  
        }  
        Noun nounA_ = nounSet.ceiling(new Noun(nounA));  
        Noun nounB_ = nounSet.ceiling(new Noun(nounB));  
        return sap.length(nounA_.getId(), nounB_.getId());
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB){
        if (!isNoun(nounA)){  
            throw new java.lang.IllegalArgumentException();  
        }  
        if (!isNoun(nounB)){  
            throw new java.lang.IllegalArgumentException();  
        }  
        Noun nounA_ = nounSet.ceiling(new Noun(nounA));  
        Noun nounB_ = nounSet.ceiling(new Noun(nounB));  
        return idList.get(sap.ancestor(nounA_.getId(), nounB_.getId()));
    }

    // do unit testing of this class
    public static void main(String[] args){
        WordNet wn = new WordNet(args[0],args[1]);
        for(String nounName:wn.nouns()){
            StdOut.printf("%s ",nounName);
        }
        StdOut.println();
    }
}