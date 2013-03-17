import java.util.Arrays;
import java.util.LinkedList;

public class Fast {
    private static int mSize;
    private static Point[] mPoints;
    private static final double EPSILON = 0.00000001;
    private static final int POINTS_IN_LINE = 4; // >=

    public static void main(String[] args) {
        if (args.length < 1)
            StdOut.print("need input file name");

        In in = new In(args[0]);
        mSize = in.readInt();
        mPoints = new Point[mSize];

        for (int i = 0; i < mSize; i++) {
            int x = in.readInt();
            int y = in.readInt();
            mPoints[i] = new Point(x, y);
        }

        fastFindLine(mPoints);
    }


    private static void fastFindLine(Point[] oriPoints) {
        // make a copy for sort
        Point[] slopes = new Point[mSize];
        for (int i = 0; i < mSize; i++) {
            slopes[i] = oriPoints[i];
        }

        for (int p = 0; p < mSize; p++) {
            Point curPoint = oriPoints[p];

            // sort by slope from points[p]
            Arrays.sort(slopes, curPoint.SLOPE_ORDER);

            // find points with same slope
            double prevSlope = curPoint.slopeTo(slopes[0]);
            LinkedList<Point> pointsInLine = new LinkedList<Point>();
            for (int i = 0; i < mSize; i++) {
                double slope = curPoint.slopeTo(slopes[i]);

                // see a point with a different slope
                if (!isSame(prevSlope, slope)) {
                    // check size of points that has been found in line
                    if (pointsInLine.size() >= POINTS_IN_LINE - 1) {
                        // found!
                        pointsInLine.add(curPoint);
                        Point[] line = new Point[pointsInLine.size()];
                        pointsInLine.toArray(line);
                        Arrays.sort(line);
                        for (int j = 0; j < line.length; j++) {
                            StdOut.print(line[j].toString());
                            if (j < line.length - 1)
                                StdOut.print(" -> ");
                            else
                                StdOut.print("\n");
                        }
                    }

                    pointsInLine.clear();
                    prevSlope = slope;
                }
                pointsInLine.add(slopes[i]);
            }

            // leave loop when still have points in list
            if (pointsInLine.size() > POINTS_IN_LINE) {
                pointsInLine.add(curPoint);
                Point[] line = (Point[]) pointsInLine.toArray();
                Arrays.sort(line);
                for (int j = 0; j < line.length; j++) {
                    StdOut.print(line[j].toString());
                    if (j < line.length - 1)
                        StdOut.print(" -> ");
                    else
                        StdOut.print("\n");
                }

            }

        }
    }

    private static boolean isSame(double d1, double d2) {
        return d1 == d2 || Math.abs(d1 - d2) < EPSILON;
    }

}
