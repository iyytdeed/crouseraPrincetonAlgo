import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private double[][] picEnergyMatrix;
    private int width;
    private int height;
    private class Edge implements Comparable<Edge>{
        private int[] pFrom;
        private int[] pTo;
        private double dist;
        public Edge(int rowFrom, int colFrom, int rowTo, int colTo, double dist){
            pFrom = new int[2];
            pTo = new int[2];
            this.pFrom[0] = rowFrom;
            this.pFrom[1] = colFrom;
            this.pTo[0] = rowTo;
            this.pTo[1] = colTo;
            this.dist = dist;
        }
        public int compareTo(Edge that){
            if(this.dist < that.dist) return -1;
            else if(this.dist > that.dist) return 1;
            else return 0;
        }
    }
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture){
        this.picture = picture;
        // step_1 inital picPixelMatrix from picture;
        width = picture.width();
        height = picture.height();
        double[][][] picPixelMatrix = new double[height][width][3];
        Color color;
        for(int row=0;row<height;row++){
            for(int col=0;col<width;col++){
                color = picture.get(col,row);
                picPixelMatrix[row][col][0] = color.getRed();
                picPixelMatrix[row][col][1] = color.getGreen();
                picPixelMatrix[row][col][2] = color.getBlue();
            }
        }
        // step_2 inital picEnergyMartrix from picPixelMatrix;
        picEnergyMatrix = new double[height][width];
        for(int row=0;row<height;row++){
            for(int col=0;col<width;col++){
                if(row == 0 || row == height-1 || col == 0 || col == width-1){
                    picEnergyMatrix[row][col] = 1000;
                }
                else{
                    double xEnergySquare = Math.pow(picPixelMatrix[row][col+1][0] - picPixelMatrix[row][col-1][0], 2) + Math.pow(picPixelMatrix[row][col+1][1] - picPixelMatrix[row][col-1][1], 2) + Math.pow(picPixelMatrix[row][col+1][2] - picPixelMatrix[row][col-1][2], 2);
                    double yEnergySquare = Math.pow(picPixelMatrix[row+1][col][0] - picPixelMatrix[row-1][col][0], 2) + Math.pow(picPixelMatrix[row+1][col][1] - picPixelMatrix[row-1][col][1], 2) + Math.pow(picPixelMatrix[row+1][col][2] - picPixelMatrix[row-1][col][2], 2);
                    picEnergyMatrix[row][col] = Math.sqrt(xEnergySquare + yEnergySquare);
                }
            }
        }
    }
    // current picture
    public Picture picture(){
        return this.picture;
    }
    // width of current picture
    public int width(){
        return width;
    }
    // height of current picture
    public int height(){
        return height;
    }
    // energy of pixel at column x and row y
    public double energy(int x, int y){
        if(x<0||x>width-1) throw new java.lang.IllegalArgumentException();
        if(y<0||y>height-1) throw new java.lang.IllegalArgumentException();
        return picEnergyMatrix[y][x];
    }
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam(){
        // step_1 intial var
        MinPQ<Edge> minPQ = new MinPQ<Edge>();
        double[][] distTo = new double[height][width];
        int[][][] edgeTo = new int[height][width][2];
        for(int row=1;row<height-1;row++){
            minPQ.insert(new Edge(0,0,row,1,picEnergyMatrix[row][1]));
        }
        for(int col=0,row=0;row<height;row++){
            distTo[row][0] = 0;
            distTo[row][1] = picEnergyMatrix[row][1];
        }
        // step_2 dijkstra algo
        int tail_row;
        while(true){
            Edge edg = minPQ.delMin();
            int row = edg.pTo[0];
            int col = edg.pTo[1];
            double dist = edg.dist;
            // if never visited or distlongerthannow , then refresh
            if(row+1<height-1){
                if(distTo[row+1][col+1] == 0 || dist+picEnergyMatrix[row+1][col+1]<distTo[row+1][col+1]){
                    distTo[row+1][col+1] = dist + picEnergyMatrix[row+1][col+1];
                    edgeTo[row+1][col+1][0] = row;
                    edgeTo[row+1][col+1][1] = col;
                    minPQ.insert(new Edge(row,col,row+1,col+1,distTo[row+1][col+1]));
                }
            }
            if(row-1>0){
                if(distTo[row-1][col+1] == 0 || dist+picEnergyMatrix[row-1][col+1]<distTo[row-1][col+1]){
                    distTo[row-1][col+1] = dist + picEnergyMatrix[row-1][col+1];
                    edgeTo[row-1][col+1][0] = row;
                    edgeTo[row-1][col+1][1] = col;
                    minPQ.insert(new Edge(row,col,row-1,col+1,distTo[row-1][col+1]));
                }
            }
            if(distTo[row][col+1] == 0 || dist+picEnergyMatrix[row][col+1]<distTo[row][col+1]){
                distTo[row][col+1] = dist + picEnergyMatrix[row][col+1];
                edgeTo[row][col+1][0] = row;
                edgeTo[row][col+1][1] = col;
                minPQ.insert(new Edge(row,col,row,col+1,distTo[row][col+1]));
            }
            // check if reach the tail
            if(col+1 == width-1){
                tail_row = row;
                break;
            }
        }
        // step_3 get the resSeq;
        int[] resSeq = new int[width];
        int tail_col = width-1;
        while(tail_col != 0){
            resSeq[tail_col] = tail_row;
            int tmprow = edgeTo[tail_row][tail_col][0];
            int tmpcol = edgeTo[tail_row][tail_col][1];
            tail_row = tmprow;
            tail_col = tmpcol;
        }
        resSeq[0] = resSeq[1];
        return resSeq;
    }
    // sequence of indices for vertical seam
    public int[] findVerticalSeam(){
        // step_1 intial var
        MinPQ<Edge> minPQ = new MinPQ<Edge>();
        double[][] distTo = new double[height][width];
        int[][][] edgeTo = new int[height][width][2];
        for(int col=1;col<width-1;col++){
            minPQ.insert(new Edge(0,0,1,col,picEnergyMatrix[1][col]));
        }
        for(int row=0,col=0;col<width;col++){
            distTo[0][col] = 0;
            distTo[1][col] = picEnergyMatrix[1][col];
        }
        // step_2 dijkstra algo
        int tail_col;
        while(true){
            Edge edg = minPQ.delMin();
            int row = edg.pTo[0];
            int col = edg.pTo[1];
            double dist = edg.dist;
            // if never visited or distlongerthannow , then refresh
            if(col+1<width-1){
                if(distTo[row+1][col+1] == 0 || dist+picEnergyMatrix[row+1][col+1]<distTo[row+1][col+1]){
                    distTo[row+1][col+1] = dist + picEnergyMatrix[row+1][col+1];
                    edgeTo[row+1][col+1][0] = row;
                    edgeTo[row+1][col+1][1] = col;
                    minPQ.insert(new Edge(row,col,row+1,col+1,distTo[row+1][col+1]));
                }
            }
            if(col-1>0){
                if(distTo[row+1][col-1] == 0 || dist+picEnergyMatrix[row+1][col-1]<distTo[row+1][col-1]){
                    distTo[row+1][col-1] = dist + picEnergyMatrix[row+1][col-1];
                    edgeTo[row+1][col-1][0] = row;
                    edgeTo[row+1][col-1][1] = col;
                    minPQ.insert(new Edge(row,col,row+1,col-1,distTo[row+1][col-1]));
                }
            }
            if(distTo[row+1][col] == 0 || dist+picEnergyMatrix[row+1][col]<distTo[row+1][col]){
                distTo[row+1][col] = dist + picEnergyMatrix[row+1][col];
                edgeTo[row+1][col][0] = row;
                edgeTo[row+1][col][1] = col;
                minPQ.insert(new Edge(row,col,row+1,col,distTo[row+1][col]));
            }
            // check if reach the tail
            if(row+1 == height-1){
                tail_col = col;
                break;
            }
        }
        // step_3 get the resSeq;
        int[] resSeq = new int[height];
        int tail_row = height-1;
        while(tail_row != 0){
            resSeq[tail_row] = tail_col;
            int tmprow = edgeTo[tail_row][tail_col][0];
            int tmpcol = edgeTo[tail_row][tail_col][1];
            tail_row = tmprow;
            tail_col = tmpcol;
        }
        resSeq[0] = resSeq[1];
        return resSeq;
    }
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam){
        if(seam == null) throw new java.lang.IllegalArgumentException();
        for(int i=0;i<seam.length;i++){
            if(seam[i]<0 || seam[i]>height-1) throw new java.lang.IllegalArgumentException();
        }
        Picture newPic = new Picture(width, height-1);
        // step_1 modify picEnergyMatrix
        for(int col=0;col<width;col++){
            for(int row=seam[col]+1;row<height;row++){
                double tmp = picEnergyMatrix[row-1][col];
                picEnergyMatrix[row-1][col] = picEnergyMatrix[row][col];
                picEnergyMatrix[row][col] = tmp;
            }
            picEnergyMatrix[height-1][col] = 1000;
        }
        // step_2 get new picture
        for(int col=0;col<width;col++){
            int flag = 0;
            for(int row=0;row<height;row++){
                if(row == seam[col]) flag++;
                if(row+flag == height) break;
                newPic.set(col, row, picture.get(col, row+flag));
            }
        }
        picture = newPic;
        // step_3 modify height and weight
        height--;
    }
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam){
        if(seam == null) throw new java.lang.IllegalArgumentException();
        for(int i=0;i<seam.length;i++){
            if(seam[i]<0 || seam[i]>width-1) throw new java.lang.IllegalArgumentException();
        }
        Picture newPic = new Picture(width-1, height);
        //for(int i=0;i<seam.length;i++) StdOut.printf(" %d",seam[i]);
        // step_1 modify picEnergyMatrix
        for(int row=0;row<height;row++){
            //StdOut.printf("\nrow-%d\n",row);
            for(int col=seam[row]+1;col<width;col++){
                //StdOut.printf("row-%d;move-%.2f ",row,picEnergyMatrix[row][col]);
                double tmp = picEnergyMatrix[row][col-1];
                picEnergyMatrix[row][col-1] = picEnergyMatrix[row][col];
                picEnergyMatrix[row][col] = tmp;
            }
            picEnergyMatrix[row][width-1] = 1000;
        }
        // step_2 get new picture
        for(int row=0;row<height;row++){
            int flag = 0;
            for(int col=0;col<width;col++){
                if(col == seam[row]) flag++;
                if(col+flag == width) break;
                newPic.set(col, row, picture.get(col+flag, row));
            }
        }
        picture = newPic;
        // step_3 modify height and weight
        width--;
    }
}