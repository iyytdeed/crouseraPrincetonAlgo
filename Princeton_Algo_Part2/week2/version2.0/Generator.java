import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class Generator{
    public Generator(){
        java.util.Random ran = new java.util.Random();
        Picture pic = new Picture(6,6);
        pic.setRGB(0,0,0x080107);
        pic.setRGB(1,0,0x090300);
        pic.setRGB(2,0,0x070706);
        pic.setRGB(3,0,0x040904); 
        pic.setRGB(4,0,0x070106);
        pic.setRGB(5,0,0x010201);
        
        pic.setRGB(0,1,0x020805); 
        pic.setRGB(1,1,0x070109);
        pic.setRGB(2,1,0x070105);
        pic.setRGB(3,1,0x070303); 
        pic.setRGB(4,1,0x020304);
        pic.setRGB(5,1,0x070705);
        
        pic.setRGB(0,2,0x020404);
        pic.setRGB(1,2,0x020401);
        pic.setRGB(2,2,0x080507);
        pic.setRGB(3,2,0x040100); 
        pic.setRGB(4,2,0x020602);
        pic.setRGB(5,2,0x080109);
        
        pic.setRGB(0,3,0x000303);
        pic.setRGB(1,3,0x030400);
        pic.setRGB(2,3,0x050506);
        pic.setRGB(3,3,0x050106); 
        pic.setRGB(4,3,0x060006);
        pic.setRGB(5,3,0x050703);
        
        pic.setRGB(0,4,0x040802); 
        pic.setRGB(1,4,0x010207);
        pic.setRGB(2,4,0x000108);
        pic.setRGB(3,4,0x080202); 
        pic.setRGB(4,4,0x090402);
        pic.setRGB(5,4,0x090803);
        
        pic.setRGB(0,4,0x070708);
        pic.setRGB(1,4,0x050803);
        pic.setRGB(2,4,0x070105);
        pic.setRGB(3,4,0x070302); 
        pic.setRGB(4,4,0x090505);
        pic.setRGB(5,4,0x050406);
        
        pic.save("myTest.png");
    }
    public static void main(String[] args) {
        Generator g = new Generator();
    }
}