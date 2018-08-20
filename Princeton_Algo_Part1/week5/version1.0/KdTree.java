import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Stack;
import edu.princeton.cs.algs4.Queue;

public class KdTree{
    // PRIVATE VAR --BEGIN--
    private Node root;
    private int size;
    private class Node{
        private Point2D p;
        private Node lchild;
        private Node rchild;
        private RectHV rect;
        private int ori;
        public Node(Point2D pInput, RectHV rectIn){
            this.p = pInput;
            this.lchild = null;
            this.rchild = null;
            this.rect = rectIn;
            ori = 1;
        }
        public int nodeCmp(Node that){
            if(this.p.equals(that.p)) return 0;
            if(that.ori == 1){
                if(this.p.x() < that.p.x()) return -1;
                else return 1;
            }
            else{
                if(this.p.y() < that.p.y()) return -1;
                else return 1;
            }
        }
    }
    // PRIVATE VAR --END--
    
    // PUBLIC FUNC --BEGIN--
    // basic function -begin-
    public KdTree(){
        root = null;
        size = 0;
    }
    public boolean isEmpty(){
        return size==0;
    }
    public int size(){
        return size;
    }
    // basic function -end- 
    // contains & insert function -begin-
    public boolean contains(Point2D p){
        Node pNode = new Node(p,null);
        return contains(root, pNode);
    }
    private boolean contains(Node rootIn, Node nodeIn){
        if(rootIn == null) return false;
        nodeIn.ori = -rootIn.ori;
        if(nodeIn.nodeCmp(rootIn) > 0) 
            return contains(rootIn.rchild, nodeIn);
        else if(nodeIn.nodeCmp(rootIn) < 0)
            return contains(rootIn.lchild, nodeIn);
        else return true;
    }
    public void insert(Point2D p){
        RectHV rect = new RectHV(0,0,1,1);
        Node pNode = new Node(p,rect);
        if(root == null){
             root = pNode;
             this.size++;
        }
        else{
            insert(this.root, pNode);
        }
    }
    private boolean insert(Node rootIn, Node nodeIn){
        if(nodeIn.nodeCmp(rootIn) == 0) return false;
        if(rootIn.lchild == null && nodeIn.nodeCmp(rootIn) < 0){
            if(rootIn.ori == 1 && nodeIn.p.x() >= rootIn.p.x()){
                RectHV rectAdd = new RectHV(rootIn.p.x(),rootIn.rect.ymin(),rootIn.rect.xmax(),rootIn.rect.ymax());
                Node nodeAdd = new Node(nodeIn.p, rectAdd);
                nodeAdd.ori = -rootIn.ori;
                root.lchild = nodeAdd;
            }
            else if(rootIn.ori == 1 && nodeIn.p.x() < rootIn.p.x()){
                RectHV rectAdd = new RectHV(rootIn.rect.xmin(),rootIn.rect.ymin(),rootIn.p.x(),rootIn.rect.ymax());
                Node nodeAdd = new Node(nodeIn.p, rectAdd);
                nodeAdd.ori = -rootIn.ori;
                rootIn.lchild = nodeAdd;
            }
            else if(rootIn.ori == -1 && nodeIn.p.y() >= rootIn.p.y()){
                RectHV rectAdd = new RectHV(rootIn.rect.xmin(),rootIn.p.y(),rootIn.rect.xmax(),rootIn.rect.ymax());
                Node nodeAdd = new Node(nodeIn.p, rectAdd);
                nodeAdd.ori = -rootIn.ori;
                rootIn.lchild = nodeAdd;
            }
            else if(rootIn.ori == -1 && nodeIn.p.y() < rootIn.p.y()){
                RectHV rectAdd = new RectHV(rootIn.rect.xmin(),rootIn.rect.ymin(),rootIn.rect.xmax(),rootIn.p.y());
                Node nodeAdd = new Node(nodeIn.p, rectAdd);
                nodeAdd.ori = -rootIn.ori;
                rootIn.lchild = nodeAdd;
            }
            this.size++;
            return true;
        }
        if(rootIn.rchild == null && nodeIn.nodeCmp(rootIn) > 0){
            if(rootIn.ori == 1 && nodeIn.p.x() >= rootIn.p.x()){
                RectHV rectAdd = new RectHV(rootIn.p.x(),rootIn.rect.ymin(),rootIn.rect.xmax(),rootIn.rect.ymax());
                Node nodeAdd = new Node(nodeIn.p, rectAdd);
                nodeAdd.ori = -rootIn.ori;
                rootIn.rchild = nodeAdd;
            }
            else if(rootIn.ori == 1 && nodeIn.p.x() < rootIn.p.x()){
                RectHV rectAdd = new RectHV(rootIn.rect.xmin(),rootIn.rect.ymin(),rootIn.p.x(),rootIn.rect.ymax());
                Node nodeAdd = new Node(nodeIn.p, rectAdd);
                nodeAdd.ori = -rootIn.ori;
                rootIn.rchild = nodeAdd;
            }
            else if(rootIn.ori == -1 && nodeIn.p.y() >= rootIn.p.y()){
                RectHV rectAdd = new RectHV(rootIn.rect.xmin(),rootIn.p.y(),rootIn.rect.xmax(),rootIn.rect.ymax());
                Node nodeAdd = new Node(nodeIn.p, rectAdd);
                nodeAdd.ori = -rootIn.ori;
                rootIn.rchild = nodeAdd;
            }
            else if(rootIn.ori == -1 && nodeIn.p.y() < rootIn.p.y()){
                RectHV rectAdd = new RectHV(rootIn.rect.xmin(),rootIn.rect.ymin(),rootIn.rect.xmax(),rootIn.p.y());
                Node nodeAdd = new Node(nodeIn.p, rectAdd);
                nodeAdd.ori = -rootIn.ori;
                rootIn.rchild = nodeAdd;
            }
            this.size++;
            return true;
        }
        if(nodeIn.nodeCmp(rootIn) < 0)
            return insert(rootIn.lchild, nodeIn);
        else
            return insert(rootIn.rchild, nodeIn);
    }
    // contains function & insert funtion -end-
    // draw function -begin-
    public void draw(){
        draw(root);
    }
    private void draw(Node rootIn){
        if(rootIn == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        StdDraw.point(rootIn.p.x(), rootIn.p.y());
        if(rootIn.ori == 1){
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.01);
            StdDraw.line(rootIn.p.x(), rootIn.rect.ymin(), rootIn.p.x(), rootIn.rect.ymax());
        }
        if(rootIn.ori == -1){
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.01);
            StdDraw.line(rootIn.rect.xmin(), rootIn.p.y(), rootIn.rect.xmax(), rootIn.p.y());
        }
        draw(rootIn.lchild);
        draw(rootIn.rchild);
    }
    // draw function -end-
    // range & nearest function -begin-
    public Iterable<Point2D> range(RectHV rect){
        Queue<Point2D> que = new Queue<Point2D>();
        Queue<Node> que_node = new Queue<Node>();
        if(isEmpty()) return que;
        que_node.enqueue(root);
        while(!que_node.isEmpty()){
            Node node_t = que_node.dequeue();
            if(rect.contains(node_t.p)) que.enqueue(node_t.p);
            if(node_t.lchild != null && rect.intersects(node_t.lchild.rect)) que_node.enqueue(node_t.lchild);
            if(node_t.rchild != null && rect.intersects(node_t.rchild.rect)) que_node.enqueue(node_t.rchild);
        }
        return que;
    }
    // Func Right, But error in Submit
    /*
    public Iterable<Point2D> range(RectHV rect){
        Stack<Point2D> stk = new Stack<Point2D>();
        if(root != null)
            inRange(root, rect, stk);
        return stk;
    }
    private void inRange(Node rootIn, RectHV rect, Stack<Point2D> stk){
        if(rect.contains(rootIn.p)) stk.push(rootIn.p);
        if(rootIn.lchild == null && rootIn.rchild == null) return;
        if(rootIn.lchild != null)
            inRange(rootIn.lchild, rect, stk);
        if(rootIn.rchild != null)
            inRange(rootIn.rchild, rect, stk);
    }*/
    public Point2D nearest(Point2D p){
        if (root == null) return null;
        Point2D ret = null;
        double mindis = Double.MAX_VALUE;
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            double dis = p.distanceSquaredTo(x.p);
            if (dis < mindis) {
                ret = x.p;
                mindis = dis; 
            }
            if (x.lchild != null && x.lchild.rect.distanceSquaredTo(p) < mindis) 
                queue.enqueue(x.lchild);
            if (x.rchild != null && x.rchild.rect.distanceSquaredTo(p) < mindis) 
                queue.enqueue(x.rchild);
        }
        return ret;
    }
    /*
    public Point2D nearest(Point2D p){
        if(root == null) return null;
        return nearest(root, p);
    }
    private Point2D nearest(Node rootIn, Point2D p){
        if(rootIn == null) return new Point2D(Double.MAX_VALUE,Double.MAX_VALUE);
        Point2D nearP;
        Point2D nearPL = nearest(rootIn.lchild, p);
        Point2D nearPR = nearest(rootIn.rchild, p);
        if(p.distanceSquaredTo(nearPL) < p.distanceSquaredTo(rootIn.p) && p.distanceSquaredTo(nearPL) < p.distanceSquaredTo(nearPR)){
            nearP = nearPL;
        }
        if(p.distanceSquaredTo(nearPR) < p.distanceSquaredTo(rootIn.p) && p.distanceSquaredTo(nearPR) < p.distanceSquaredTo(nearPL)){
            nearP = nearPR;
        }
        else{
            nearP = rootIn.p;
        };
        return nearP;
    }*/
    // range & nearest function -end-
    // PUBLIC FUNC --END--
    
    // TEST -BEGIN-
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        /*
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            StdDraw.setPenRadius(0.01);
            p.draw();
        }*/
        
        // Near Test
        /*
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        Point2D p = kdtree.nearest(new Point2D(0.4,0.7));
        StdOut.printf("%.8f  %.8f\n",p.x(),p.y());
        p.draw();
        */
        
        // Draw Test
        /*
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            StdOut.printf("%8.6f %8.6f\n", x, y);
            kdtree.insert(p);
        }
        kdtree.draw();*/
        
        // Random Generation - Insert Contains Test
        /*
        for (int i=0;i<10;i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            StdOut.printf("%8.6f %8.6f\n", x, y);
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            StdOut.println(kdtree.contains(p));
        }*/
    }
    // TEST -END-
}