import edu.princeton.cs.algs4.MinPQ;
import java.util.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    // PRIVATE VAR
    // -- BEGIN --
    private SearchNode cur;
    private boolean isSolvable;
    private class SearchNode implements Comparable<SearchNode>{
        private Board bd;
        private int moves;
        private int priority;
        private SearchNode preSearchNode;
        private boolean isInitialParity;
        public SearchNode(Board bd, SearchNode preSearchNode, boolean isInitialParity){
            this.bd = bd;
            if(isInitialParity == true){
                this.preSearchNode = null;
                this.moves = 0;
                this.priority = bd.manhattan();
                this.isInitialParity = true;
            }
            else{
                this.preSearchNode = preSearchNode;
                this.moves = preSearchNode.moves+1;
                this.priority = this.bd.manhattan() + this.moves;
                this.isInitialParity = false;
            }
        }
        public int compareTo(SearchNode that){
            if(this.priority == that.priority) return this.bd.manhattan() - that.bd.manhattan();
            return this.priority - that.priority;
        }
    }
    // PRIVATE VAR
    // -- END --
    
    // PUBLICK METHOD
    // -- BEGIN --
    // find a solution to the initial board (using the A* algorithm)
    // func_1
    public Solver(Board initial){
        if(initial == null) throw new IllegalArgumentException("Board initial == null");
        MinPQ<SearchNode> minPQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> minPQTwin = new MinPQ<SearchNode>();
        minPQ.insert(new SearchNode(initial, null, true));
        minPQTwin.insert(new SearchNode(initial.twin(), null, true));
        SearchNode curTwin;
        while(true){
            SearchNode now = minPQ.delMin();
            SearchNode nowTwin = minPQTwin.delMin();
            cur = now;
            curTwin = nowTwin;
            if(now.bd.isGoal() || nowTwin.bd.isGoal()) break;
            for(Board neighbor : now.bd.neighbors()){
                if(now.preSearchNode != null && neighbor.equals(now.preSearchNode.bd)) continue;
                minPQ.insert(new SearchNode(neighbor, now, false));
            }
            for(Board neighbor : nowTwin.bd.neighbors()){
                if(nowTwin.preSearchNode != null && neighbor.equals(nowTwin.preSearchNode.bd)) continue;
                minPQTwin.insert(new SearchNode(neighbor, nowTwin, false));
            }
        }
        if(cur.bd.isGoal()) isSolvable = true;
        if(curTwin.bd.isGoal()) isSolvable = false;
    }
    // is the initial board solvable?
    // func_2
    public boolean isSolvable(){
        return isSolvable;
    }
    // min number of moves to solve initial board; -1 if unsolvable
    // func_3
    public int moves(){
        if(!isSolvable) return -1;
        return cur.moves;
    }
    // sequence of boards in a shortest solution; null if unsolvable
    // func_4
    public Iterable<Board> solution(){
        if(!isSolvable) return null;
        Stack<Board> solutionStk = new Stack<Board>();
        SearchNode curhere = cur;
        while(!curhere.isInitialParity){
            solutionStk.push(curhere.bd);
            curhere = curhere.preSearchNode;
        }
        solutionStk.push(curhere.bd);
        Stack<Board> stk2 = new Stack<Board>();
        while(!solutionStk.empty()){
            stk2.push(solutionStk.pop());
        }
        return stk2;
    }
    // PUBLICK METHOD
    // -- END -- 
    
    // solve a slider puzzle (given below)
    public static void main(String[] args){}
}