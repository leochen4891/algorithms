/*************************************************************************
 * Name:
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {
    private static final double POS_INF = Double.POSITIVE_INFINITY;
    private static final double NEG_INF = Double.NEGATIVE_INFINITY;
    private static final double EPSILON = 0.00000001;

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new SlopeComparator(this);

    private final int x; // x coordinate
    private final int y; // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        if (x == that.x && y == that.y) {
            // treat the slope of a degenerate line segment
            // (between a point and itself) as negative infinity.
            return NEG_INF;
        } else if (x == that.x) {
            // treat the slope of a vertical line segment as positive infinity;
            return POS_INF;
            // return (y < that.y?POS_INF:NEG_INF);
        } else if (y == that.y) {
            // Treat the slope of a horizontal line segment as positive zero
            return 0;
            // return (x < that.x?POS_ZERO:NEG_ZERO);
        } else {
            return ((double) that.y - (double) y) / ((double) that.x - (double) x);
        }
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        if (y != that.y) {
            return y - that.y;
        } else {
            return x - that.x;
        }
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) {
        /* YOUR CODE HERE */
    }

    private static boolean isSame(double d1, double d2) {
        return d1 == d2 || Math.abs(d1 - d2) < EPSILON;
    }

    private class SlopeComparator implements Comparator<Point> {
        private Point mPoint;

        SlopeComparator(Point p) {
            mPoint = p;
        }

        @Override
        public int compare(Point p1, Point p2) {
            double s1 = mPoint.slopeTo(p1);
            double s2 = mPoint.slopeTo(p2);
            if (isSame(s1, s2))
                return 0;

            if (s1 - s2 > 0)
                return 1;

            return -1;
        }
    }
}