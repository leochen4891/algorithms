//Submit only Percolation.java (using the weighted quick-union algorithm
// as implemented in the WeightedQuickUnionUF class) and PercolationStats.java.
// We will supply stdlib.jar and WeightedQuickUnionUF. 
// Your submission may not call any library functions other than 
// those in java.lang, stdlib.jar, and WeightedQuickUnionUF.


public class Percolation {
    private WeightedQuickUnionUF mUF; 
    private int mN;
    private int mNN;
    private int mVTop;
    private int mVBot;
    
    private int[][] mGrid;
    
    private boolean mPercolation = false;

    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N < 1) throw new IllegalArgumentException("N should > 0");
        mN = N;
        mNN = N * N;

        mUF = new WeightedQuickUnionUF(mNN + 2); // 2 more virtual sites

        mGrid = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                mGrid[i][j] = 0;
            }
        }

        mVTop = mNN;
        mVBot = mNN + 1;

        // link virtual top and bottom sites
        for (int i = 0; i < N; i++) {
            mUF.union(mVTop, i);
            mUF.union(mVBot, mNN - N + i);
        }
    }

    // open site (row i, column j) if it is not already
    public void open(int i1, int j1) {

        int i = i1 - 1;
        int j = j1 - 1;
        // open a site means add 4 links
        //         i-1,j
        //           |
        // i,j-1 -- i,j -- i,j+1
        //           |
        //         i+1,j
        mGrid[i][j] = 1;
        if (i-1 >= 0 && 1 == mGrid[i-1][j]) {
            mUF.union(mN*i+j, mN*(i-1)+j);
        }
        if (j-1 >= 0 && 1 == mGrid[i][j-1]) {
            mUF.union(mN*i+j, mN*i+(j-1));
        }
        if (i+1 < mN && 1 == mGrid[i+1][j]) {
            mUF.union(mN*i+j, mN*(i+1)+j);
        }
        if (j+1 < mN && 1 == mGrid[i][j+1]) {
            mUF.union(mN*i+j, mN*i+(j+1));
        }
    }

    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        return (1 == mGrid[i-1][j-1]);
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        if (i < 1 || j < 1 || i > mN || j > mN)
            throw new ArrayIndexOutOfBoundsException("i,j > 0, <= N");
        return (mGrid[i - 1][j - 1] == 1 && mUF.connected(mVTop, mN * (i - 1)
                + (j - 1)));
    }

    // does the system percolate?
    public boolean percolates() {
        if (!mPercolation) {
            // check if 1st and last row has opened site
            boolean top = false, bot = false;
            for (int i = 0; i < mN; i++) {
                if (mGrid[0][i] == 1)
                    top = true;
                if (mGrid[mN - 1][i] == 1)
                    bot = true;
            }
            mPercolation = (top && bot && mUF.connected(mVTop, mVBot));
        }
        return mPercolation;
    }
    /*
    public void print() {
        String str = "----------------------------------------------------\n";
        for (int i = 0; i < mN; i++) {
            for (int j = 0; j < mN; j++) {
                if (mGrid[i][j] == 0)
                    str += "X"; // closed
                else if (mUF.connected(mVTop, mN*i+j))
                    str += "+"; // full
                else 
                    str += " "; // empty
            }
            str += "\n";
        }
        System.out.print(str);
    } // */
    
}
