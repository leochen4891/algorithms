public class Brute {
    private static int mSize;
    private static Point[] mPoints;
    private static final double EPSILON = 0.00000001;
    
    public static void main(String[] args) {
        if (args.length < 1) StdOut.print("need input file name");
        
        In in = new In(args[0]);
        mSize = in.readInt();
        mPoints = new Point[mSize];
        for (int i = 0; i < mSize; i++) {
            int x = in.readInt();
            int y = in.readInt();
            mPoints[i] = new Point(x, y);
        }
        
        bruteFindLine(mPoints);
    }
    
    // to find a line of 4 points with brute force
    private static void bruteFindLine(Point[] points) {
        for (int p = 0; p < points.length; p++) {
            for (int q = 0; q < points.length; q++) {
                for (int r = 0; r < points.length; r++) {
                    for (int s = 0; s < points.length; s++) {
                        if (p == q || p == r || p == s || q == r || q == s || r == s)
                            continue;
                        if (points[p].compareTo(points[q]) > 0 
                                || points[q].compareTo(points[r]) > 0 
                                || points[r].compareTo(points[s]) > 0)
                            continue;
                        
                        double slopePQ = points[p].slopeTo(points[q]);
                        double slopePR = points[p].slopeTo(points[r]);
                        double slopePS = points[p].slopeTo(points[s]);
                        if (isSame(slopePQ, slopePR) && isSame(slopePQ, slopePS)) {
                            // Found!
                            //StdOut.print("s1 = " + slopePQ 
                            //    + "\ns2 = " + slopePR 
                            //    + "\ns3 = " + slopePS + "\n");
                            StdOut.print(points[p].toString() 
                                  + " -> " + points[q].toString() 
                                  + " -> " + points[r].toString() 
                                  + " -> " + points[s].toString() + "\n");
                        }
                    }
                }
            }
        }

    }
    
    private static boolean isSame(double d1, double d2) {
        return d1 == d2 || Math.abs(d1 - d2) < EPSILON;
    }
}
