package nqueens;

import java.util.ArrayList;
import java.util.HashMap;

public class NQueensBacktrack {
    // This stores the valid boards found from the algorithm
    private ArrayList<int[]> validBoards;

    // Size of the board (size x size)
    private int size;

    // This is the current state of the board we are operating on
    private int[] board;

    // This store the number of queens in a column
    private HashMap<Integer, Integer> queenColumns;

    // This stores count of the diff of x and y (used for main diag)
    private HashMap<Integer, Integer> mainDiags;

    // This stores count of the sum of x and y (used for main diag)
    private HashMap<Integer, Integer> secDiags;

    public NQueensBacktrack(int size) {
        this.size = size;
        // init board to all -1s -- no queens
        this.board = new int[size];
        for (int i = 0; i < size; i++) {
            this.board[i] = -1;
        }

        this.validBoards = new ArrayList<int[]>();

        this.queenColumns = new HashMap<Integer, Integer>(size);
        this.mainDiags = new HashMap<Integer, Integer>(2 * size);
        this.secDiags = new HashMap<Integer, Integer>(2 * size);
        for (int i = 0; i < size; i++) {
            this.queenColumns.put(i, 0);
        }

        for (int i = -size + 1; i < size; i++) {
            this.mainDiags.put(i, 0);

        }

        for (int i = 0; i < 2 * size; i++) {
            this.secDiags.put(i, 0);
        }

    }

    // adds a queen to the board at x,y
    // also adjusts queens in column and main and sec diag
    // to speed up conflict check
    private void addQueen(int x, int y) {
        this.board[x] = y;

        int diff = x - y;
        int sum = x + y;

        this.queenColumns.put(y, this.queenColumns.get(y) + 1);
        this.mainDiags.put(diff, this.mainDiags.get(diff) + 1);
        this.secDiags.put(sum, this.secDiags.get(sum) + 1);
    }

    // removes a queen to the board from x,y
    // also adjusts queens in column and main and sec diag
    // to speed up conflict check
    private void removeQueen(int x, int y) {
        this.board[x] = -1;

        int diff = x - y;
        int sum = x + y;
        this.queenColumns.put(y, this.queenColumns.get(y) - 1);
        this.mainDiags.put(diff, this.mainDiags.get(diff) - 1);
        this.secDiags.put(sum, this.secDiags.get(sum) - 1);
    }

    // clones the current board
    private int[] cloneBoard() {
        int[] newBoard = this.board.clone();

        return newBoard;
    }

    // adds the board to list of valid boards found
    private void addValidBoard() {
        this.validBoards.add(cloneBoard());
    }

    // prints the specified board
    private void printBoard(int[] board) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i] == j) {
                    System.out.print("1 ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }

    // prints all the valid boards
    private void printBoards() {
        for (int i = 0; i < this.validBoards.size(); i++) {
            System.out.println("Solution " + (i + 1));
            System.out.println("---------------");
            printBoard(this.validBoards.get(i));
            System.out.println();
        }
    }

    // checks to see if queens in 3 rows are in a line
    // uses slope to figure that out
    private boolean checkIfInLine(int x, int y, int x2, int x3) {
        int y2 = this.board[x2];
        int y3 = this.board[x3];

        if ((x - x2) * (y - y3) == (x - x3) * (y - y2)) {
            // in straight line
            return true;
        }

        return false;
    }

    // checks if queen can be placed at a given position
    // check column, main diag, sec diag and 3 in a line (row is automatic)
    private boolean checkIfPositionIsValid(int x, int y) {
        // we are placing in a row, so no need to check row again

        // first we check column
        if (this.queenColumns.get(y) > 0) {
            return false;
        }

        int diff = x - y;
        int sum = x + y;

        // Check main diag
        if (this.mainDiags.get(diff) > 0) {
            // removeQueen(x, y);
            return false;
        }

        // Check main diag
        if (this.secDiags.get(sum) > 0) {
            return false;
        }

        // now we will check for 3 in a line
        int x1 = x - 1;
        int x2;
        while (x1 >= 0) {
            x2 = x1 - 1;
            while (x2 >= 0) {
                if (checkIfInLine(x, y, x1, x2)) {
                    // found a line
                    return false;
                }
                x2--;
            }
            x1--;
        }

        return true;
    }

    // this checks the positions for a row to figure out where to place the queen
    // it places the queen in a valid position and moves to the next row
    // if no valid positions (or we want to find all solutions) then we backtrack
    private boolean computePositionsPerRow(int row, boolean findAll) {
        if (row == this.size) {
            addValidBoard();
            return true;
        } else {
            for (int j = 0; j < this.size; j++) {
                // check if we can place the queen here
                if (checkIfPositionIsValid(row, j)) {
                    // can place, add queen there and go to next row
                    addQueen(row, j);
                    boolean retVal = computePositionsPerRow(row + 1, findAll);

                    if (retVal && !findAll) {
                        return true;
                    }

                    // now backtrack to find other solutions
                    removeQueen(row, j);
                }
            }
        }

        return false;
    }

    /**
     * 
     * @param findAll determines whether we want all solutions found or just the
     *                first one
     * @return number of solutions found (if findAll is false it will return 1 or 0
     *         )
     */
    public int computePositions(boolean findAll) {
        computePositionsPerRow(0, findAll);

        // Only print solution for small boards (if only finding one)
        // OR for even small size (if finding all)
        if (findAll && this.size <= 10 || !findAll && this.size <= 20) {
            printBoards();
        }

        return this.validBoards.size();
    }
}
