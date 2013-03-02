public class PercolationStats {
	private int mN;
	private int mT;
	private Percolation mPer;
	
	// perform T independent computational experiments on an N-by-N grid
	public PercolationStats(int N, int T) {
		mN = N;
		mT = T;
		mPer = new Percolation(N);
	};

	// sample mean of percolation threshold
	public double mean() {
		return 0;
	};

	// sample standard deviation of percolation threshold
	public double stddev() {
		return 0;
	};

	// returns lower bound of the 95% confidence interval
	public double confidenceLo() {
		return 0;
	};

	// returns upper bound of the 95% confidence interval
	public double confidenceHi() {
		return 0;
	};

	// test client, described below
	public static void main(String[] args) {
	};
}