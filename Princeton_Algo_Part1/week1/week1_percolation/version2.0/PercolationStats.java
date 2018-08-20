import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private int totalExpNum;
    private double[] threshold;
    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials){
        if(n<1||trials<1) throw new IllegalArgumentException("error input arguments");
        totalExpNum = trials;
        threshold = new double[trials];
        for(int idx=0;idx<trials;idx++){
            Percolation perc = new Percolation(n);
            while(true){
                int posX = StdRandom.uniform(1,n+1);
                int posY = StdRandom.uniform(1,n+1);
                perc.open(posX,posY);
                if(perc.percolates()) break;
            }
            double n_square = n*n;
            threshold[idx] = perc.numberOfOpenSites()/n_square;
        }
    }
    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(threshold);
    }
    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(threshold);
    }
    // low  endpoint of 95% confidence interval
    public double confidenceLo(){
        return mean() - 1.96*stddev()/Math.sqrt(totalExpNum);
    }
    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return mean() + 1.96*stddev()/Math.sqrt(totalExpNum);
    }
    // test client
    public static void main(String[] args){
    }
}