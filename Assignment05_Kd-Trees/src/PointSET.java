import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> mPoints;
    
    // construct an empty set of points
    public PointSET() {
        mPoints = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return mPoints.isEmpty();
    }

    // number of points in the set
    public int size() {
        return mPoints.size();
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
        mPoints.add(p);
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return mPoints.contains(p);
    }

    // draw all of the points to standard draw
    public void draw() {
        for (Point2D p: mPoints) {
            StdDraw.filledCircle(p.x(), p.y(), 0.002);
        }
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        TreeSet<Point2D> ret = new TreeSet<Point2D>();
        for (Point2D p: mPoints) {
            if (rect.contains(p)) {
                ret.add(p);
            }
        }
        return ret;
    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        Point2D nearest = null;
        double nearestDistance = Double.MAX_VALUE;
        for (Point2D cur: mPoints) {
            double distance = cur.distanceTo(p);
            if (nearestDistance > distance) {
                nearest = cur;
                nearestDistance = distance;
            }
        }
        return nearest;
    }
}