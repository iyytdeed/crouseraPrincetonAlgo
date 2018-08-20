
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.Stack;

public class Board {
    // PRIVATE VAR
    // -- BEGIN -- 
    private final int[] nbynVec;
    private final int n;
    private int hammingDist;
    private int manhattanDist;
    // PRIVATE VAR
    // -- END --
    
    // PUBLICK METHOD 
    // - BEGIN --
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    // func_1
    public Board(int[][] blocks){
        // save dimension n
        n = blocks.length;
        // save nbynVec
        nbynVec = new int[n * n];
        for(int col = 0; col < n; col++){
            for(int row = 0; row < n; row++){
                nbynVec[row * n + col] = blocks[row][col];
            }
        }
        // save hammingDist
        hammingDist = 0;
        for(int idx = 0; idx < n*n-1; idx++){
            if(nbynVec[idx] != idx+1) hammingDist++;
        }
        // save manhattanDist
        manhattanDist = 0;
        for(int col = 0; col < n; col++){
            for(int row = 0; row < n; row++){
                if(nbynVec[row * n + col] != row * n + col + 1){
                    if(nbynVec[row * n + col] == 0) continue;
                    int row_shouldbe = nbynVec[row * n + col] / n;
                    int col_shouldbe = nbynVec[row * n + col] % n;
                    if(col_shouldbe == 0){
                        row_shouldbe--;
                        col_shouldbe = n-1;
                    }
                    else{
                        col_shouldbe--;
                    }
                    manhattanDist += Math.abs(row - row_shouldbe) + Math.abs(col - col_shouldbe);
                }
            }
        }
    }
    // board dimension n
    // func_2
    public int dimension(){ return n; }
    // number of blocks out of place
    // func_3
    public int hamming(){ return hammingDist; }
    // sum of Manhattan distances between blocks and goal
    // func_4
    public int manhattan(){ return manhattanDist; }
    // is this board the goal board?
    // func_5
    public boolean isGoal(){
        if(hammingDist == 0) return true;
        return false;
    }
    // a board that is obtained by exchanging any pair of blocks
    // func_6
    public Board twin(){
        int[][] twinBlocks = new int[n][n];
        for(int col = 0; col < n; col++){
            for(int row = 0; row < n; row++){
                twinBlocks[row][col] = nbynVec[row*n+col];
            }
        }
        if(twinBlocks[0][0] == 0){
            int tmp = twinBlocks[0][1];
            twinBlocks[0][1] = twinBlocks[n-1][n-1];
            twinBlocks[n-1][n-1] = tmp;
            return new Board(twinBlocks);
        }
        if(twinBlocks[n-1][n-1] == 0){
            int tmp = twinBlocks[0][0];
            twinBlocks[0][0] = twinBlocks[n-1][n-2];
            twinBlocks[n-1][n-2] = tmp;
            return new Board(twinBlocks);
        }
        int tmp = twinBlocks[0][0];
        twinBlocks[0][0] = twinBlocks[n-1][n-1];
        twinBlocks[n-1][n-1] = tmp;
        return new Board(twinBlocks);
    }
    // does this board equal y?
    // func_7
    public boolean equals(Object y){
        if(y == this) return true;
        if(y == null) return false;
        Board y_new = (Board) y;
        if(y_new.dimension() != this.n) return false;
        for(int idx = 0; idx < n*n; idx++){
            if(this.nbynVec[idx] != y_new.nbynVec[idx]) return false;
        }
        return true;
    }
    // all neighboring boards
    // func_8
    public Iterable<Board> neighbors(){
        Stack<Board> neighborsStk = new Stack<Board>();
        int rowBlank = -1;
        int colBlank = -1;
        for(int col = 0; col < n; col++){
            for(int row = 0; row < n; row++){
                if(nbynVec[row*n + col] == 0){
                    rowBlank = row;
                    colBlank = col;
                    row = n;
                    col = n;
                }
            }
        }
        if(rowBlank >= 1){
            int[][] neighborBlocks = new int[n][n];
            for(int col = 0; col < n; col++){
                for(int row = 0; row < n; row++){
                    neighborBlocks[row][col] = nbynVec[row*n+col];
                }
            }
            int tmp = neighborBlocks[rowBlank][colBlank];
            neighborBlocks[rowBlank][colBlank] = neighborBlocks[rowBlank-1][colBlank];
            neighborBlocks[rowBlank-1][colBlank] = tmp;
            neighborsStk.push(new Board(neighborBlocks));
        }
        if(rowBlank <= n-2){
            int[][] neighborBlocks = new int[n][n];
            for(int col = 0; col < n; col++){
                for(int row = 0; row < n; row++){
                    neighborBlocks[row][col] = nbynVec[row*n+col];
                }
            }
            int tmp = neighborBlocks[rowBlank][colBlank];
            neighborBlocks[rowBlank][colBlank] = neighborBlocks[rowBlank+1][colBlank];
            neighborBlocks[rowBlank+1][colBlank] = tmp;
            neighborsStk.push(new Board(neighborBlocks));
        }
        if(colBlank >= 1){
            int[][] neighborBlocks = new int[n][n];
            for(int col = 0; col < n; col++){
                for(int row = 0; row < n; row++){
                    neighborBlocks[row][col] = nbynVec[row*n+col];
                }
            }
            int tmp = neighborBlocks[rowBlank][colBlank];
            neighborBlocks[rowBlank][colBlank] = neighborBlocks[rowBlank][colBlank-1];
            neighborBlocks[rowBlank][colBlank-1] = tmp;
            neighborsStk.push(new Board(neighborBlocks));
        }
        if(colBlank <= n-2){
            int[][] neighborBlocks = new int[n][n];
            for(int col = 0; col < n; col++){
                for(int row = 0; row < n; row++){
                    neighborBlocks[row][col] = nbynVec[row*n+col];
                }
            }
            int tmp = neighborBlocks[rowBlank][colBlank];
            neighborBlocks[rowBlank][colBlank] = neighborBlocks[rowBlank][colBlank+1];
            neighborBlocks[rowBlank][colBlank+1] = tmp;
            neighborsStk.push(new Board(neighborBlocks));
        }
        return neighborsStk;
    }
    // string representation of this board (in the output format specified below)
    // func_9
    public String toString(){
        String nbynVecStr = "";
        nbynVecStr = String.valueOf(n) + "\n";
        for(int col = 0; col < n; col++){
            for(int row = 0; row < n; row++){
                nbynVecStr += " " + String.valueOf(nbynVec[col*n+row]);
            }
            nbynVecStr += "\n";
        }
        return nbynVecStr;
    }
    // PUBLICK METHOD
    // -- END --
    
    public static void main(String[] args){
        for (String filename : args) {
            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }
            // solve the slider puzzle
            Board initial = new Board(tiles);
            StdOut.println(initial.toString());
            for(Board ele:initial.neighbors()){
                StdOut.println(ele.toString());
            }
        }
    }
}