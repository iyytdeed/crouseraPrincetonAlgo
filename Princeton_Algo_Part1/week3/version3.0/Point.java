import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import java.lang.IllegalArgumentException;

public class Point implements Comparable<Point> {
    private final int x;
    private final int y;
    private class myCmp implements Comparator<Point>{
        private final Point p0;
        public myCmp(Point that){
            if(that.x>32767||that.x<0||that.y>32767||that.y<0) throw new IllegalArgumentException("x,y out of range");
            p0 = that;
        }
        public int compare(Point p1, Point p2){
            return Double.compare(this.p0.slopeTo(p1), this.p0.slopeTo(p2));
        }
    }
    
    public Point(int x, int y){
        if(x>32767||x<0||y>32767||y<0) throw new IllegalArgumentException("x,y out of range");
        this.x = x;
        this.y = y;
    }
    public void draw(){
        StdDraw.point(x, y);
    }
    public void drawTo(Point that){
        StdDraw.line(this.x, this.y, that.x, that.y);
    }
    public String toString(){
        return "(" + x + ", " + y + ")";
    }
    public int compareTo(Point that){
        if(this.y < that.y) return -1;
        if(this.y > that.y) return 1;
        if(this.x < that.x) return -1;
        if(this.x > that.x) return 1;
        return 0;
    }
    public double slopeTo(Point that){
        if(this.x == that.x && this.y == that.y) return Double.NEGATIVE_INFINITY;
        if(this.y == that.y) return 0;
        if(this.x == that.x) return Double.POSITIVE_INFINITY;
        return (double)(that.y-this.y)/(that.x-this.x);
    }
    public Comparator<Point> slopeOrder(){
        return new myCmp(this);
    }
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        Point p1 = new Point(3000, 7000);
        Point p2 = new Point(6000, 7000);
        System.out.println("p1.compareTo(p2)=" + p1.compareTo(p2));
        System.out.println("p1.slopeTo(p2)=" + p1.slopeTo(p2));
        Point p3 = new Point(14000, 15000);
        System.out.println("p1.slopeTo(p3)=" + p1.slopeTo(p3));
        Point p4 = new Point(4, 4);
        System.out.println("p3.compareTo(p4)=" + p3.compareTo(p4));
        System.out.println("p3.slopeTo(p4)=" + p3.slopeTo(p4));
        Point p5 = new Point(0, 0);
        System.out.println("p1.slopeTo(p5)=" + p1.slopeTo(p5));
        System.out.println(p1.slopeOrder().compare(p2,p4));
    }
}