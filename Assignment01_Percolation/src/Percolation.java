//Submit only Percolation.java (using the weighted quick-union algorithm
// as implemented in the WeightedQuickUnionUF class) and PercolationStats.java.
// We will supply stdlib.jar and WeightedQuickUnionUF. 
// Your submission may not call any library functions other than 
// those in java.lang, stdlib.jar, and WeightedQuickUnionUF.


public class Percolation {
	WeightedQuickUnionUF mUF; 
	private int mN;
	private int mNN;
	private int mVTop;
	private int mVBot;
	
	private int[][] mGrid;
	
	// create N-by-N grid, with all sites blocked
	public Percolation(int N) {
		mN = N;
		mNN= N*N;
		
		mUF = new WeightedQuickUnionUF(mNN + 2);// 2 more virtual sites
		
		mGrid = new int[N][N];
		for (int i = 0; i < N; i++){ for (int j = 0; j < N; j++) { mGrid[i][j] = 0; } }
		
		mVTop = mNN;
		mVBot = mNN + 1;
		
		// link virtual top and bottom sites
		for (int i = 0; i < N; i++) {
			mUF.union(mVTop, i);
			mUF.union(mVBot, mNN - N +i);
		}
	}

	// open site (row i, column j) if it is not already
	public void open(int i, int j) {
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
		return (1 == mGrid[i][j]);
	}

	// is site (row i, column j) full?
	public boolean isFull(int i, int j) {
		return (mUF.connected(mVTop, mN*i+j));
	}

	// does the system percolate?
	public boolean percolates() {
		return (mUF.connected(mVTop, mVBot));
	}
	
	private void Print() {
		String str = "-------------------------------------------------------------\n";
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
	}
}
