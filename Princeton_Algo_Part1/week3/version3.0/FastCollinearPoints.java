import java.util.ArrayList;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    private int numOfSeg;
    private Point[] sortedPoints;
    private ArrayList<LineSegment> segmentList;
    
    public FastCollinearPoints(Point[] points){
        if(points == null) throw new IllegalArgumentException("NULL Point");
        for(int i=0;i<points.length;i++){
            if(points[i] == null) throw new IllegalArgumentException("null member");
        }
        numOfSeg = 0;
        segmentList = new ArrayList<LineSegment>();
        sortedPoints = pointSort(points);
        for(int i=1;i<sortedPoints.length;i++){
            if(sortedPoints[i].compareTo(sortedPoints[i-1]) == 0) throw new IllegalArgumentException("repeat");
        }
        for(int idx=0;idx<sortedPoints.length-3;idx++){
            Double[] tan = new Double[sortedPoints.length - 1];
            Point[] tanPoints = new Point[sortedPoints.length - 1];
            for(int i = 0, j = 0;i<tan.length;){
                if(j == idx){
                    j++;
                    continue;
                }
                tan[i] = sortedPoints[idx].slopeTo(sortedPoints[j]);
                tanPoints[i] = sortedPoints[j];
                i++;
                j++;
            }
            tanMergeSort(tan, tanPoints, 0, tan.length-1);
            int i=0;
            int j=0;
            while(j<tan.length){
                if(tan[i]==Double.POSITIVE_INFINITY && tan[j]==Double.POSITIVE_INFINITY){
                    j++;
                    continue;
                }
                if(tan[i]-tan[j] == 0 || tan[i]==tan[j]){
                    j++;
                    continue;
                }
                if(j-i<3){
                    i = j;
                }
                if(j-i>=3){
                    Point point_high = sortedPoints[idx].compareTo(tanPoints[i])>0?sortedPoints[idx]:tanPoints[i];
                    Point point_low = sortedPoints[idx].compareTo(tanPoints[j-1])<0?sortedPoints[idx]:tanPoints[j-1];
                    LineSegment lineAdd = new LineSegment(point_low,point_high);
                    boolean tagRepeat = false;
                    for(LineSegment line:segmentList){
                        String linestr = line.toString();
                        String lineaddstr = lineAdd.toString();
                        if(linestr.equals(lineaddstr)){
                            tagRepeat = true;
                            break;
                        }
                    }
                    if(tagRepeat == false){
                        segmentList.add(lineAdd);
                        numOfSeg++;
                    }
                    i = j;
                }
            }
            if(j==tan.length && j-i>=3){
                Point point_high = sortedPoints[idx].compareTo(tanPoints[i])>0?sortedPoints[idx]:tanPoints[i];
                Point point_low = sortedPoints[idx].compareTo(tanPoints[j-1])<0?sortedPoints[idx]:tanPoints[j-1];
                LineSegment lineAdd = new LineSegment(point_low,point_high);
                boolean tagRepeat = false;
                for(LineSegment line:segmentList){
                    String linestr = line.toString();
                    String lineaddstr = lineAdd.toString();
                    if(linestr.equals(lineaddstr)){
                        tagRepeat = true;
                        break;
                    }
                }
                if(tagRepeat == false){
                    segmentList.add(lineAdd);
                    numOfSeg++;
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
    private void tanMergeSort(Double[] tan_in, Point[] tanPoints_in, int left, int right){
        if(left>=right) return;
        int mid = (left+right)/2;
        tanMergeSort(tan_in, tanPoints_in, left, mid);
        tanMergeSort(tan_in, tanPoints_in, mid+1, right);
        tanMerge(tan_in,tanPoints_in,left,right);
    }
    private void tanMerge(Double[] tan_in, Point[] tanPoints_in, int left, int right){
        Point[] tmp_points = new Point[right-left+1];
        Double[] tmp_tan = new Double[right-left+1];
        int mid = (left+right)/2;
        int i = left;
        int j = mid+1;
        int cnt = 0;
        while(i<=mid && j<=right){
            if(tan_in[i]<tan_in[j]){
                tmp_tan[cnt] = tan_in[i];
                tmp_points[cnt] = tanPoints_in[i];
                cnt++;
                i++;
            }
            else{
                tmp_tan[cnt] = tan_in[j];
                tmp_points[cnt] = tanPoints_in[j];
                cnt++;
                j++;
            }
        }
        while(i<=mid){
            tmp_tan[cnt] = tan_in[i];
            tmp_points[cnt] = tanPoints_in[i];
            cnt++;
            i++;
        }
        while(j<=right){
            tmp_tan[cnt] = tan_in[j];
            tmp_points[cnt] = tanPoints_in[j];
            cnt++;
            j++;
        }
        cnt = 0;
        while(cnt<tmp_tan.length){
            tan_in[left] = tmp_tan[cnt];
            tanPoints_in[left] = tmp_points[cnt];
            left++;
            cnt++;
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
        
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}