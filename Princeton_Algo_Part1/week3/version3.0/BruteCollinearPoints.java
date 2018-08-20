import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private int numOfSeg;
    private Point[] sortedPoints;
    private ArrayList<LineSegment> segmentList;
    public BruteCollinearPoints(Point[] points){
        if(points == null) throw new IllegalArgumentException("NULL Point");
        for(int i=0;i<points.length;i++){
            if(points[i] == null) throw new IllegalArgumentException("null member");
        }
        numOfSeg = 0;
        sortedPoints = pointSort(points);
        segmentList = new ArrayList<LineSegment>();
        for(int i=1;i<sortedPoints.length;i++){
            if(sortedPoints[i].compareTo(sortedPoints[i-1]) == 0) throw new IllegalArgumentException("repeat");
        }
        for(int ifirst = 0;ifirst<sortedPoints.length-3;ifirst++){
            for(int isecond = ifirst+1;isecond<sortedPoints.length-2;isecond++){
                double slope_1 = sortedPoints[ifirst].slopeTo(sortedPoints[isecond]);
                for(int ithird = isecond+1;ithird<sortedPoints.length-1;ithird++){
                    double slope_2 = sortedPoints[ifirst].slopeTo(sortedPoints[ithird]);
                    if(slope_1 != slope_2) continue;
                    for(int ifourth = ithird+1;ifourth<sortedPoints.length;ifourth++){
                        double slope_3 = sortedPoints[ifirst].slopeTo(sortedPoints[ifourth]);
                        if(slope_1 != slope_3) continue;
                        segmentList.add(new LineSegment(sortedPoints[ifirst],sortedPoints[ifourth]));
                        numOfSeg++;
                    }
                }
            }
        }
    }
    public int numberOfSegments(){
        return numOfSeg;
    }
    public LineSegment[] segments(){
        LineSegment[] lineSeg = new LineSegment[numOfSeg];
        int cnt = 0;
        for(LineSegment seg:segmentList){
            lineSeg[cnt++] = seg;
        }
        return lineSeg;
    }
    private Point[] pointSort(Point[] points){
        Point[] sortedpoints = new Point[points.length];
        for(int idx=0;idx<points.length;idx++) sortedpoints[idx] = points[idx];
        mergeSort(sortedpoints, 0, sortedpoints.length-1);
        return sortedpoints;
    }
    private void mergeSort(Point[] points, int left, int right){
        if(left>=right) return;
        int mid = (left+right)/2;
        mergeSort(points, left, mid);
        mergeSort(points, mid+1, right);
        merge(points,left,right);
    }
    private void merge(Point[] points, int left, int right){
        Point[] tmp = new Point[right-left+1];
        int mid = (left+right)/2;
        int i = left;
        int j = mid+1;
        int cnt = 0;
        while(i<=mid && j<=right){
            if(points[i].compareTo(points[j]) < 0) tmp[cnt++] = points[i++];
            else tmp[cnt++] = points[j++];
        }
        while(i<=mid){
            tmp[cnt++] = points[i++];
        }
        while(j<=right){
            tmp[cnt++] = points[j++];
        }
        cnt = 0;
        while(cnt<tmp.length){
            points[left++] = tmp[cnt++];
        }
    }
    
    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        //In in = new In("collinear/input8.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(0.01);
        for (Point p : points) {
            //StdOut.println(p);
            p.draw();
        }
        StdDraw.show();
        // print and draw the line segments'
        
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
