// https://www.cnblogs.com/maverick-fu/p/4017865.html

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] matrix;
    private int rowN,colN;
    private WeightedQuickUnionUF wquUF;
    private WeightedQuickUnionUF wquUFTop;
    
    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if(n<1) throw new IllegalArgumentException("Illeagal Argument");
        wquUF = new WeightedQuickUnionUF(n*n+2);
        wquUFTop = new WeightedQuickUnionUF(n*n+1);
        rowN = n;
        colN = n;
        matrix = new boolean[n*n+1];
    }
    // open site (row, col) if it is not open already
    public void open(int row, int col){
        //validate(row,col);
        int curIdx = (row-1)*colN + col;
        matrix[curIdx] = true;
        if(row == 1){
            wquUF.union(curIdx,0);
            wquUFTop.union(curIdx, 0);
        }
        if(row == rowN){
            wquUF.union(curIdx, rowN*colN+1);
        }
        int[] dx = {1,-1,0,0};
        int[] dy = {0,0,1,-1};
        for(int dir=0;dir<4;dir++){
            int posX = row + dx[dir];
            int posY = col + dy[dir];
            if(validate(posX,posY) && isOpen(posX,posY)){
            //if(isOpen(posX,posY)){
                wquUF.union(curIdx,(posX-1)*colN+posY);
                wquUFTop.union(curIdx,(posX-1)*colN+posY);
            }
        }
    }
    // is site (row, col) open?
    public boolean isOpen(int row, int col){
        //validate(row,col);
        return matrix[(row-1)*colN+col];
    }
    // is site (row, col) full?
    public boolean isFull(int row, int col){
        validate(row,col);
        return wquUFTop.connected((row-1)*colN+col, 0);
    }
    // number of open sites
    public int numberOfOpenSites(){
        int cnt = 0;
        for(int dir=1;dir<rowN*colN+1;dir++){
            if(matrix[dir] == true) cnt++;
        }
        return cnt;
    }
    // does the system percolate?
    public boolean percolates(){
        return wquUF.connected(rowN*colN+1,0);
    }
    // SUPPORT_FUNC
    private boolean validate(int row, int col) {
        if (row < 1 || row > rowN){
            return false;
        }
        if (col < 1 || col > colN){
            return false;
        }
        return true;
    }
    // test client (optional)
    public static void main(String[] args){
        Percolation perc = new Percolation(2);
        perc.open(1, 1);
        perc.open(1, 2);
        perc.open(2, 1);
        System.out.println(perc.percolates());
    }
}