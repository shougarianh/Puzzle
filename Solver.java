ackage edu.umb.cs210.p4;

import dsa.LinkedStack;
import dsa.MinPQ;
import stdlib.In;
import stdlib.StdOut;

import java.util.Comparator;

// A solver based on the A* algorithm for the 8-puzzle and its generalizations.
public class Solver {
    LinkedStack<Board> solution; // stack of solutions
    int moves; // number of moves
    
    // Helper search node class.
    private class SearchNode {
        Board board; // current board
        int moves; // number of moves
        private SearchNode previous; // previous node


        // Construct a new SearchNode
        SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board; // Constructor initializes board
            this.moves = moves; // Constructor initializes numver of moves
            this.previous = previous; // Constructor initializes node previous
        }
    }
     
    // Find a solution to the initial board (using the A* algorithm).
    public Solver(Board initial) {
        if (initial == null) { // null board exception
            throw new NullPointerException("null board");
        }
        else if (!initial.isSolvable()) { // unsolvable board exception
            throw new IllegalArgumentException("Unsolvable board");
        }
        // Create a manhattan ordered MinPQ called pq
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>(new ManhattanOrder());
        this.solution = solution; // initialize solution
        // insert initial board into the pq, with a null previous and 0 moves
        pq.insert(new SearchNode(initial, 0, null));
        while (!pq.isEmpty()) // While the pq is not empty
        {
            // node stores the minimum of pq
            SearchNode node = pq.delMin();
            // solution initialized as a linked stack containig Boards
            solution = new LinkedStack<Board>();
            if (node.board.isGoal()) // If node is the goal board
            {
                moves = node.moves; // Set moves to the number of moves in node
                // While the previous board is not null
                while (node.previous != null) {
                    // Push the current board (node) pn to solution stack
                    solution.push(node.board);
                    node = node.previous; // set node to the previous node
                }
                break; // break out of the whole loop
            }
            else {
                // Create an itterable to store neighboring boards
                Iterable<Board> neighborboards = node.board.neighbors();
                for (Board i:neighborboards) { // for each neighboring board
                    /* if the current board does not equal the previous board
                        and the previous board is not null
                     */
                    if (node.previous != null && i != node.previous.board) {
                        moves = node.moves + 1; // increment moves by one
                        // Insert current Board into the pq
                        pq.insert(new SearchNode(i, moves, node));
                    }
                    // If the previous board is null
                    else if (node.previous == null)
                    {
                        moves = node.moves + 1; // increment moves by one
                        // Insert the current board into the pq
                        pq.insert(new SearchNode(i, moves, node));
                    }
                }
            }
        }


    }

    // The minimum number of moves to solve the initial board.
    public int moves() {
        return moves; // returns the current number of moves
    }

    // Sequence of boards in a shortest solution.
    public Iterable<Board> solution() {
        return solution; // return the solution stack
    }

    // Helper hamming priority function comparator.
    private static class HammingOrder implements Comparator<SearchNode> {
        // new comparison method created with two searchnodes as arguments
        public int compare(SearchNode a, SearchNode b) {
            // if hamming diatance plus moves of a is less than that of b
            if (a.board.hamming() + a.moves < b.board.hamming() + b.moves)
            {
                return -1; // returns -1
            }
            // if hamming diatance plus moves of a is greater than that of b
            else if (a.board.hamming() + a.moves > b.board.hamming() + b.moves)
            {
                return 1; // returns 1
            }
            return 0; // Otherwise they are equal so we return 0
        }
    }
       
    // Helper manhattan priority function comparator.
    private static class ManhattanOrder implements Comparator<SearchNode> {
        // new comparison method created with two searchnodes as arguments
        public int compare(SearchNode a, SearchNode b) {
            // if manhattan diatance plus moves of a is less than that of b
            if (a.board.manhattan() + a.moves < b.board.manhattan() + b.moves)
            {
                return -1; // returns a -1
            }
            // if manhattan diatance plus moves of a is greater than that of b
            else if (a.board.manhattan() + a.moves >
                        b.board.manhattan() + b.moves)
            {
                return 1; // returns a 1
            }
            return 0; // They are equal so we return 0
        }
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
        else {
            StdOut.println("Unsolvable puzzle");
        }
    }
}
