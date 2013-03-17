import java.util.Iterator;

public class Board {
    private int[][] mBlocks;

    // construct a board from an N-by-N array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blk) {
        int N = blk[0].length;
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = blk[i][j];
        mBlocks = blocks;
    }

    // board dimension N
    public int dimension() {
        return mBlocks.length;
    }

    // number of blocks out of place
    public int hamming() {
        int N = this.dimension();
        int hamming = 0;
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                int value = mBlocks[y][x];
                if (0 == value)
                    continue;
                int goalY = (value - 1) / N;
                int goalX = (value - 1) % N;
                if ((goalY != y) || (goalX != x))
                    hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int N = this.dimension();
        int manhattan = 0;
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                int value = mBlocks[y][x];
                if (0 == value)
                    continue;
                int goalY = (value - 1) / N;
                int goalX = (value - 1) % N;
                manhattan += Math.abs(goalY - y) + Math.abs(goalX - x);
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // a board obtained by exchanging two adjacent blocks in the same row
    public Board twin() {

        // create a new block
        int[][] blocks = copyBlocks();

        // to find two adjacent blocks in a same row that neither is gap
        int N = this.dimension();
        for (int y = 0; y < N; y++) {
            boolean prevNotGap = false;
            for (int x = 0; x < N; x++) {
                if (prevNotGap && blocks[y][x] != 0) {
                    // found!
                    exchange(blocks, y, x, y, x - 1);
                    return new Board(blocks);
                }
                prevNotGap = (blocks[y][x] != 0);
            }
        }

        return null;
    }

    // does this board equal y?
    public boolean equals(Object o) {
        if (null == o)
            return false;

        try {
            Board b = (Board) o;

            if (this.manhattan() != b.manhattan())
                return false;
        } catch (ClassCastException e) {
            return this.toString().equals(o.toString());
        }

        return this.toString().equals(o.toString());

        // for (int i = 0; i < N; i++) {
        // for (int j = 0; j < N; j++) {
        // if (mBlocks[i][j] != b.getBlocks()[i][j]) {
        // return false;
        // }
        // }
        // }
        // return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // 1. find 0
        int N = this.dimension();
        int gapX = -1, gapY = -1;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (mBlocks[i][j] == 0) {
                    gapX = j;
                    gapY = i;
                }

        // 2. try move gap to 4 directions
        Board[] boards = new Board[4];
        int cnt = 0;
        // left
        if (gapX > 0) {
            int[][] moveLeftBlocks = copyBlocks();
            exchange(moveLeftBlocks, gapY, gapX, gapY, gapX - 1);
            boards[cnt++] = new Board(moveLeftBlocks);
        }
        // up
        if (gapY > 0) {
            int[][] moveUpBlocks = copyBlocks();
            exchange(moveUpBlocks, gapY, gapX, gapY - 1, gapX);
            boards[cnt++] = new Board(moveUpBlocks);
        }
        // right
        if (gapX < N - 1) {
            int[][] moveRightBlocks = copyBlocks();
            exchange(moveRightBlocks, gapY, gapX, gapY, gapX + 1);
            boards[cnt++] = new Board(moveRightBlocks);
        }
        // down
        if (gapY < N - 1) {
            int[][] moveDownBlocks = copyBlocks();
            exchange(moveDownBlocks, gapY, gapX, gapY + 1, gapX);
            boards[cnt++] = new Board(moveDownBlocks);
        }

        // create iterator
        Itr itr = null;
        if (cnt > 0) {
            Board[] retBoards = new Board[cnt];
            for (int i = 0; i < cnt; i++) {
                retBoards[i] = boards[i];
            }
            itr = new Itr(retBoards);
        }
        return itr;
    }

    // string representation of the board (in the output format specified below)
    public String toString() {
        int N = this.dimension();
        StringBuilder str = new StringBuilder();
        str.append(Integer.toString(N)).append("\n");
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                str.append(mBlocks[y][x]).append(" ");
            }
            str.append("\n");
        }
        
        return str.toString();
    }

    private static void exchange(int[][] blocks, int y1, int x1, int y2, int x2) {
        int temp = blocks[y1][x1];
        blocks[y1][x1] = blocks[y2][x2];
        blocks[y2][x2] = temp;
    }

    private int[][] copyBlocks() {
        int N = this.dimension();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = mBlocks[i][j];
        return blocks;
    }

    private class Itr implements Iterator<Board>, Iterable<Board> {
        private Board[] mBoards;
        private int mNext;

        Itr(Board[] boards) {
            mBoards = boards;
            mNext = 0;
        }

        @Override
        public boolean hasNext() {
            return mNext <= mBoards.length - 1;
        }

        @Override
        public Board next() {
            return mBoards[mNext++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Iterator<Board> iterator() {
            return this;
        }

    }
}