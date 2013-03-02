public class PercolationStats {
    private int mN;
    private int mT;
    private double[] mXs;
    private double mMean = -1;
    private double mStddev = -1;

    // perform T independent computational experiments on an N-by-N grid
    public PercolationStats(int N, int T) {
        mN = N;
        mT = T;
        mXs = new double[T];
        int NN = N * N;

        // try T times
        StdRandom.setSeed(System.currentTimeMillis());
        for (int t = 0; t < T; t++) {
            int i, j, cnt = 0;
            Percolation per = new Percolation(N);
            boolean percolation = false;
            while (cnt < NN && !percolation) {
                i = StdRandom.uniform(N);
                j = StdRandom.uniform(N);
                if (per.isOpen(i, j))
                    continue;

                per.open(i, j);
                cnt++;
                percolation = per.percolates();
            }
            assert (percolation);
            mXs[t] = cnt / (double) NN;
        }
    };

    // sample mean of percolation threshold
    public double mean() {
        double total = 0;
        for (int cnt = 0; cnt < mT; cnt++) {
            total += mXs[cnt];
        }
        mMean = total / (double) mT;
        return mMean;
    };

    // sample standard deviation of percolation threshold
    public double stddev() {
        mStddev = StdStats.stddev(mXs);
        return mStddev;
    };

    // returns lower bound of the 95% confidence interval
    public double confidenceLo() {
        if (mMean < 0)
            mean();
        if (mStddev < 0)
            stddev();
        return (mMean - 1.96 * mStddev / Math.sqrt(mT));
    };

    // returns upper bound of the 95% confidence interval
    public double confidenceHi() {
        if (mMean < 0)
            mean();
        if (mStddev < 0)
            stddev();
        return (mMean + 1.96 * mStddev / Math.sqrt(mT));
    };

    // test client, described below
    public static void main(String[] args) {
        int N = 200;
        int T = 100;
        PercolationStats stats = new PercolationStats(N, T);
        System.out.print("N = " + N + ", T = " + T + "\n");
        System.out.print("mean   = " + stats.mean() + "\n");
        System.out.print("stddev = " + stats.stddev() + "\n");
        System.out.print("95% CI = " + stats.confidenceLo() + ", "
                + stats.confidenceHi() + "\n");
    };

}