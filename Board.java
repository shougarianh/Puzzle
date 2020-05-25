
package edu.umb.cs210.p4;

import dsa.LinkedQueue;
import stdlib.In;
import stdlib.StdOut;

// Models a board in the 8-puzzle game or its generalization.
public class Board {
    private final int[][] tiles;    // tiles in the board
    private int N;                  // board size
    private int hamming; // hamming distance
    private int manhattan; // manhattan distance
    
    // Construct a board from an N-by-N array of tiles, where 
    // tiles[i][j] = tile at row i and column j, and 0 represents the blank 
    // square.
    public Board(int[][] tiles) {
        this.N = tiles.length; // Initialize N as the of the board
        this.tiles = new int[N][N]; // Make a new empty board of size N
        for (int i = 0; i < N; i++) // Itterate through columns
        {
            for (int j = 0; j < N; j++) // Itterate through rows
                // Fill the new board respectively
                this.tiles[i][j] = tiles[i][j];
        }
        hamming = 0; // initialize hamming distance to 0
        for (int i = 0; i < N; i++) // Itterate through columns
        {
            for (int j = 0; j < N; j++) // Itterate through rows
            {
                /* If the board at index (i, j) is not it's ordered value
                and the value at index (i, j) is not the blank space
                 */
                if ((this.tiles[i][j] != N * i + j + 1)
                        && (this.tiles[i][j] != 0))
                {
                    hamming++; // Increment hamming distance by one
                }
            }
        }
        manhattan = 0; // Initialize manhattan distance to 0
        for (int i = 0; i < N; i++) // Itterate through the columns
        {
            for (int j = 0; j < N; j++) // Itterate through rows
            {
                if (tiles[i][j] == 0) // If it's the blank space
                {
                    continue; // Move on to the next index
                }
                else {
                    manhattan += Math.abs(((tiles[i][j] - 1) / N) - i)
                            + Math.abs(((tiles[i][j] - 1) % N) - (j % N));
                }
            }
        }
    }

    // Tile at row i and column j.
    public int tileAt(int i, int j) {
        return tiles[i][j]; // Return the tile at that position
    }
    
    // Size of this board.
    public int size() {
        return N; // return the size of tiles
    }

    // Number of tiles out of place.
    public int hamming() {
        return hamming; // return hamming distance
    }

    // Sum of Manhattan distances between tiles and goal.
    public int manhattan() {
        return manhattan; // return manhattan distance
    }

    // Is this board the goal board?
    public boolean isGoal() {
        // return distance to goal board is 0
        return manhattan() == 0;
    }

    // Is this board solvable?
    public boolean isSolvable() {
        if (N % 2 == 0) // if N is even
        {
            // Sum is the row of the blank square plus the number of inversion
            int sum = ((blankPos() - 1) / N) + inversions();
            // Each legal move changes the sum by an even number
            if (sum % 2 == 0) // Therefore if the sum is even
            {
                return false; // It cannot lead to a goal board
            }
            return true; // If it is odd, it does lead to a goal board
        }
        // if N is odd
        else {
            // If the number of inversions is even
            if (inversions() % 2 == 0)
            {
                return true; // Board is solvable
            }
            return false; // If inversions are odd, board is nor solvable
        }
    }

    // Does this board equal that?
    public boolean equals(Object that) {
        // Equivelency check
        if (this == that)
        {
            return true;
        }
        Board thatBoard = (Board) that;
        // Type equivelency check
        if (this.N != thatBoard.N) {
            return false;
        }
        // Itterate through columns
        for (int i = 0; i < N; i++)
        {
            // Itterate through rows
            for (int j = 0; j < N; j++)
            {
                // Index-wise equivelency check
                if (this.tiles[i][j] != thatBoard.tiles[i][j]) {
                    return false; // return false if they are not equal
                }
            }
        }
        return true; // return true otherwise
    }

    // All neighboring boards.
    public Iterable<Board> neighbors() {
        int i = (blankPos() - 1) / N;
        int j = (blankPos() - 1) % N;
        // array to store neighbor set to null
        int [][] neighbor = null;
        // A linked queue is created to store neighbors
        LinkedQueue<Board> pq = new LinkedQueue<Board>();
        // if i isnt the left most value
        if (i > 0)
        {
            // neighbor stores cloned tiles
            neighbor = cloneTiles();
            // holder stores neighbor at index (i, j)
            int holder = neighbor[i][j];
            // value at (i, j) is set to the value at it's left
            neighbor[i][j] = neighbor[i - 1][j];
            // the new value on the left is equal to the holder
            neighbor[i - 1][j] = holder;
            // neighboring board is enqueued into pq
            pq.enqueue(new Board(neighbor));
        }
        // if j isnt the top most value
        if (j > 0)
        {
            // neighbor stores cloned tiles
            neighbor = cloneTiles();
            // holder stores neighbor at index (i, j)
            int holder = neighbor[i][j];
            // value at (i, j) is set to the value above j
            neighbor[i][j] = neighbor[i][j - 1];
            // the new value above j is equal to the holder
            neighbor[i][j - 1] = holder;
            // neighboring board is enqueued into pq
            pq.enqueue(new Board(neighbor));
        }
        // if i isnt the righ most value
        if (i < N - 1)
        {
            // neighbor stores cloned tiles
            neighbor = cloneTiles();
            // holder stores neighbor at index (i, j)
            int holder = neighbor[i][j];
            // value at (i, j) is set to the value at it's right
            neighbor[i][j] = neighbor[i + 1][j];
            // the new value on the left is equal to the holder
            neighbor[i + 1][j] = holder;
            // neighboring board is enqueued into pq
            pq.enqueue(new Board(neighbor));
        }
        // if j isnt the bottom most value
        if (j < N - 1)
        {
            // neighbor stores cloned tiles
            neighbor = cloneTiles();
            // holder stores neighbor at index (i, j)
            int holder = neighbor[i][j];
            // value at (i, j) is set to the value below j
            neighbor[i][j] = neighbor[i][j + 1];
            // the new value below j is equal to the holder
            neighbor[i][j + 1] = holder;
            // neighboring board is enqueued into pq
            pq.enqueue(new Board(neighbor));
        }

        return pq; // pq is returned containing all neighboring boards
    }

    // String representation of this board.
    public String toString() {
        StringBuilder s = new StringBuilder(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d", tiles[i][j]));
                if (j < N - 1) {
                    s.append(" ");
                }
            }
            if (i < N - 1) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    // Helper method that returns the position (in row-major order) of the 
    // blank (zero) tile.
    private int blankPos() {
        for (int i = 0; i < N; i++) // itterate through columns
        {
            for (int j = 0; j < N; j++) // iterate through rows
            {
                // if we find the blank tile
                if (tileAt(i, j) == 0)
                {
                    // return it's row ordered position
                    return (N * i + j + 1);
                }
            }
        }
        return 0; // otherwise return 0
    }

    // Helper method that returns the number of inversions.
    private int inversions() {
        int count = 0; // count will be the number of inversions
        // four for loops representing indecies and row ordered positions
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < N; k++) {
                    for (int l = 0; l < N; l++) {
                        /* if the value in a tile is invereted
                            with a neighboring tile
                         */
                        if ((k * N + l) > (i * N + j) && tileAt(i, j) != 0
                            && tileAt(k, l) != 0 && tileAt(i, j)
                                > tileAt(k, l))
                        {
                            count++; // Increment the number of inversions by 1

                        }
                    }
                }
            }
        }
        return count; // return the number of inversions
    }

    // Helper method that clones the tiles[][] array in this board and 
    // returns it.
    private int[][] cloneTiles() {
        // New array to store cloned tiles
        int [][] clonetiles = new int[N][N];
        for (int i = 0; i < N; i++) // iterate through columns
        {
            for (int j = 0; j < N; j++) // itterate through rows
            {
                // clonetiles at (i, j) is equal to tiles at (i, j)
                clonetiles[i][j] = this.tiles[i][j];
            }
        }
        return clonetiles; // return the cloned tiles
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
        Board board = new Board(tiles);
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        StdOut.println(board.isGoal());
        StdOut.println(board.isSolvable());
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }
    }
}
