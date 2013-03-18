import java.util.LinkedList;

public class KdTree {
    private class Node {
        private static final boolean DIMENSION_X = true;
        private Point2D mPoint;
        private Node mLeft;
        private Node mRight;
        private boolean mDimension;
        private int mSize;
        private RectHV mRectL;
        private RectHV mRectR;

        Node(Point2D p, Node left, Node right, boolean dimension, RectHV rect) {
            mPoint = p;
            mLeft = left;
            mRight = right;
            mDimension = dimension;
            mSize = 1;
            if (null != left)
                mSize += left.mSize;
            if (null != right)
                mSize += right.mSize;

            if (Node.DIMENSION_X == dimension) {
                mRectL = new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax());
                mRectR = new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax());
            } else {
                mRectL = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
                mRectR = new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
            }
        }
    }

    private Node mRoot;

    // construct an empty set of points
    public KdTree() {
        mRoot = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return null == mRoot;
    }

    // number of points in the set
    public int size() {
        return mRoot.mSize;
    }

    // add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p.x() < 0 || p.x() >= 1 || p.y() < 0 || p.y() >= 1)
            return;
        mRoot = insert(mRoot, null, p, new RectHV(0, 0, 1, 1));
    }

    private Node insert(Node node, Node parent, Point2D p, RectHV rect) {
        if (null == p)
            return node;

        boolean dimension;
        if (null == parent)
            dimension = Node.DIMENSION_X;
        else
            dimension = !parent.mDimension;

        if (null == node) {
            return new Node(p, null, null, dimension, rect);
        } else {
            if (dimension == Node.DIMENSION_X) {
                if (p.x() < node.mPoint.x()) {
                    node.mLeft = insert(node.mLeft, node, p, node.mRectL);
                } else {
                    node.mRight = insert(node.mRight, node, p, node.mRectR);
                }
            } else {
                if (p.y() < node.mPoint.y()) {
                    node.mLeft = insert(node.mLeft, node, p, node.mRectL);
                } else {
                    node.mRight = insert(node.mRight, node, p, node.mRectR);
                }
            }
        }

        return node;
    }

    // does the set contain the point p?
    public boolean contains(Point2D p) {
        return false;
    }

    // draw all of the points to standard draw
    public void draw() {
        draw(mRoot);
    }

    private void draw(Node node) {
        if (null != node) {
            StdDraw.filledCircle(node.mPoint.x(), node.mPoint.y(), 0.002);
            draw(node.mLeft);
            draw(node.mRight);
        }
    }

    // all points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        LinkedList<Point2D> result = new LinkedList<Point2D>();
        rangeSearch(mRoot, rect, result);
        return result;
    }

    private void rangeSearch(Node node, RectHV rect, LinkedList<Point2D> result) {
        if (null == node || null == rect || null == result)
            return;

        if (rect.contains(node.mPoint))
            result.add(node.mPoint);

        if (rect.intersects(node.mRectL))
            rangeSearch(node.mLeft, rect, result);

        if (rect.intersects(node.mRectR))
            rangeSearch(node.mRight, rect, result);

    }

    // a nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        return nearest(mRoot, p, mRoot.mPoint);
    }

    private Point2D nearest(Node node, Point2D target, Point2D nearestSoFar) {
        if (null == node || null == target)
            return nearestSoFar;

        Point2D nearest = nearestSoFar;
        if (node.mPoint.distanceTo(target) < nearest.distanceTo(target))
            nearest = node.mPoint;

        double threshold;
        if (null == nearest)
            threshold = Double.MAX_VALUE;
        else
            threshold = nearest.distanceTo(target);

        if (Node.DIMENSION_X == node.mDimension) {
            if (target.x() < node.mPoint.x()) {
                if (node.mRectL.distanceTo(target) < threshold) {
                    nearest = nearest(node.mLeft, target, nearestSoFar);
                }
                if (node.mRectR.distanceTo(target) < threshold) {
                    nearest = nearest(node.mRight, target, nearestSoFar);
                }
            } else {
                if (node.mRectR.distanceTo(target) < threshold) {
                    nearest = nearest(node.mRight, target, nearestSoFar);
                }
                if (node.mRectL.distanceTo(target) < threshold) {
                    nearest = nearest(node.mLeft, target, nearestSoFar);
                }
            }
        } else {
            if (target.y() < node.mPoint.y()) {
                if (node.mRectL.distanceTo(target) < threshold) {
                    nearest = nearest(node.mLeft, target, nearestSoFar);
                }
                if (node.mRectR.distanceTo(target) < threshold) {
                    nearest = nearest(node.mRight, target, nearestSoFar);
                }
            } else {
                if (node.mRectR.distanceTo(target) < threshold) {
                    nearest = nearest(node.mRight, target, nearestSoFar);
                }
                if (node.mRectL.distanceTo(target) < threshold) {
                    nearest = nearest(node.mLeft, target, nearestSoFar);
                }
            }
        }
        return nearest;
    }
}