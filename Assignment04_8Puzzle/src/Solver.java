import java.util.Iterator;

public class Solver {
    private Board mInitialBoard;
    private Board mTwinBoard; 
    
    private Node mGoal = null;

    private boolean mSolutionFound = false;
    private boolean mSolutionFoundInTwin = false;

    private SolutionFindThread mInitialBoardThread;
    private SolutionFindThread mTwinBoardThread;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        mInitialBoard = initial;
        mTwinBoard = initial.twin();

        //StdOut.println("init board = \n" + mInitialBoard.toString());
        //StdOut.println("twin board = \n" + mTwinBoard.toString());

    }

    // is the initial board solvable?
    public boolean isSolvable() {
        if (!mSolutionFound)
            this.solution();
        if (null != mGoal && !mSolutionFoundInTwin) {
            return true;
        }
        return false;
    }

    // min number of moves to solve initial board; -1 if no solution
    public int moves() {
        if (!mSolutionFound)
            this.solution();
        if (null != mGoal && !mSolutionFoundInTwin) {
            return mGoal.mMovesCnt;
        }
        return -1;
    }

    // sequence of boards in a shortest solution; null if no solution
    public Iterable<Board> solution() {
        mInitialBoardThread = new SolutionFindThread(mInitialBoard);
        mTwinBoardThread = new SolutionFindThread(mTwinBoard);

        mInitialBoardThread.start();
        // mTwinBoardThread.start();

        while (!mSolutionFound) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        assert (mSolutionFound);

        if (null != mGoal)
            return new Solution(mGoal);

        return null;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        if (args.length < 1)
            return;
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();

        Board initial = new Board(blocks);
        Solver solver = new Solver(initial);
        if (solver.isSolvable()) {
            StdOut.println("Minimum number of moves = " + solver.moves() + "\n");
            for(Board b: solver.solution()) {
                StdOut.println(b.toString());
            }
        } else {
            StdOut.println("No solution possible");
        }
    }

    private class Node implements Comparable<Node> {
        public Board mBoard;
        public Node mPrevNode;
        public int mMovesCnt;

        Node(Board board, Node prev) {
            mBoard = board;
            mPrevNode = prev;
            if (null == prev) {
                mMovesCnt = 0;
            } else {
                mMovesCnt = prev.mMovesCnt + 1;
            }
        }

        @Override
        public int compareTo(Node node) {
            return (mBoard.manhattan() + mMovesCnt)
                    - (node.mBoard.manhattan() + node.mMovesCnt);
            // return (mBoard.hamming() + mMovesCnt) - (node.mBoard.hamming() +
            // node.mMovesCnt);
        }
    }

    private class Solution implements Iterable<Board> {
        Node mGoal;

        Solution(Node goal) {
            mGoal = goal;
        }

        @Override
        public Iterator<Board> iterator() {
            // solution moves include origin, which is moves count + 1
            Board[] boards = new Board[mGoal.mMovesCnt + 1];
            Node cur = mGoal;
            int index = mGoal.mMovesCnt;
            while (cur != null) {
                boards[index--] = cur.mBoard;
                cur = cur.mPrevNode;
            }

            return new BoardMoves(boards);
        }
    }

    private class BoardMoves implements Iterator<Board> {
        private Board[] mBoards;
        private int next;

        public BoardMoves(Board[] boards) {
            mBoards = boards;
            next = 0;
        }

        @Override
        public boolean hasNext() {
            return next <= mBoards.length - 1;
        }

        @Override
        public Board next() {
            return mBoards[next++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class SolutionFindThread extends Thread {
        Board mBoard;

        SolutionFindThread(Board board) {
            mBoard = board;
        }

        @Override
        public void run() {
            MinPQ<Node> pq = new MinPQ<Node>();
            Node initNode = new Node(mBoard, null);
            pq.insert(initNode);

            while (pq.size() > 0 && !mSolutionFound) {
                // get the min
                Node min = (Node) pq.delMin();
                if (min.mBoard.isGoal()) {
                    // found !
                    onSolutionFound(min);
                    return;
                }

                for (Board b : min.mBoard.neighbors()) {
                    if (b.equals(min))
                        continue;
                    pq.insert(new Node(b, min));
                }
            }
        }

        private synchronized void onSolutionFound(Node goal) {
            mSolutionFound = true;
            if (!mBoard.equals(mInitialBoard)) {
                mSolutionFoundInTwin = true;
            }
            mGoal = goal;
        }
    }

}
