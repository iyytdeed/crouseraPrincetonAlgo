import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.TreeSet;
import java.util.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    // PRIVATE VAR --BEGIN--
    private TreeSet<Point2D> myPointsSet;
    private int size;
    // PRIVATE VAR --END--
    
    // PUBLIC FUNC --BEGIN--
    // construct an empty set of points 
    public PointSET(){
        myPointsSet = new TreeSet<Point2D>();
        size = 0;
    }
    // is the set empty? 
    public boolean isEmpty(){
        return size==0;
    }
    // number of points in the set 
    public int size(){
        return size;
    }
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p){
        if(myPointsSet.add(p) == true)
            size++;
    }
    // does the set contain point p? 
    public boolean contains(Point2D p){
        return myPointsSet.contains(p);
    }
    // draw all points to standard draw 
    public void draw(){
        for(Point2D p:myPointsSet){
            p.draw();
        }
    }
    // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect){
        Stack<Point2D> stk = new Stack<Point2D>();
        for(Point2D p:myPointsSet){
            if(rect.contains(p)) stk.push(p);
        }
        return stk;
    }
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p){
        if(this.isEmpty()) return null;
        Point2D res = new Point2D(Double.MAX_VALUE,Double.MAX_VALUE);
        double res_dist = p.distanceSquaredTo(res);
        for(Point2D p_ele:myPointsSet){
            if(p.distanceSquaredTo(p_ele) < res_dist){
                res_dist = p.distanceSquaredTo(p_ele);
                res = p_ele;
            }
        }
        return res;
    }
    // PUBLIC FUNC --END--
    
    // unit testing of the methods (optional) 
    public static void main(String[] args){
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        // rect test
        /*for(Point2D p:brute.range(new RectHV(0,0,1,0.5))){
            p.draw();
        }*/
        // nearest test
        //brute.nearest(new Point2D(0.5,0.6)).draw();
        //ts.draw();
    }
}