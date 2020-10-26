package nqueens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class NQueensMinConflict {
    // Size of the board (size x size)
    private int size;

    // This is the current state of the board we are operating on
    private Integer[] board;

    public NQueensMinConflict(int size) {
        this.size = size;
        this.board = new Integer[size];
    }

    // adds a queen to the board at x,y
    private void addQueen(int x, int y) {
        this.board[x] = y;
    }

    // prints the board
    private void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (this.board[i] == j) {
                    System.out.print("1 ");
                } else {
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }

    // This gets the rows for the queens that have conflicts
    private ArrayList<Integer> getConflictingPositions() {
        ArrayList<Integer> conflictingRows = new ArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            if (getNumConflicts(i) > 0) {
                conflictingRows.add(i);
            }
        }

        return conflictingRows;
    }

    // This gets the number of conflicts for a queen at a specific row
    private int getNumConflicts(int i) {
        int numConflicts = 0;
        for (int j = 0; j < this.size; j++) {
            if (i == j) {
                continue;
            }

            // column
            if (this.board[j] == this.board[i]) {
                numConflicts++;
            }

            // main diag
            if (j - this.board[j] == i - this.board[i]) {
                numConflicts++;
            }

            // sec diag
            if (j + this.board[j] == i + this.board[i]) {
                numConflicts++;
            }

            // 3 in a line
            for (int k = j + 1; k < this.size; k++) {
                if (i == k) {
                    continue;
                }

                if ((i - k) * (this.board[i] - this.board[j]) == (i - j) * (this.board[i] - this.board[k])) {
                    numConflicts++;
                }
            }
        }
        return numConflicts;

    };

    // utility to get a random entry from a list
    private Integer getRandomEntry(ArrayList<Integer> list) {
        int randomNum = ThreadLocalRandom.current().nextInt(0, list.size());
        return list.get(randomNum);
    }

    // this is the main algorithm
    // it gets the conflicting queens (if none then success)
    // we pick a ramdon queen and move it to the position in the row that minimizes
    // conflicts
    // we repeat till we find a solution or we hit max tries
    private boolean computePositionsRecursively(int numTries) {
        ArrayList<Integer> conflictingPositions = getConflictingPositions();

        final int MAX_TRIES = this.size * 50;

        if (conflictingPositions.size() == 0) {
            // no conflicts
            // solution found
            return true;
        } else if (numTries > MAX_TRIES) {
            return false;
        } else {
            // get random conflicting queen
            int x = getRandomEntry(conflictingPositions);

            int y = this.board[x];

            int minConflictNum = -1;
            ArrayList<Integer> minConflictIndices = new ArrayList<Integer>(this.size);
            ArrayList<Integer> minConflictIndicesWithoutCurrent = new ArrayList<Integer>(this.size);

            int[] conflictCounts = new int[this.size];

            for (int j = 0; j < this.size; j++) {
                // try moving that queen to any j
                // move where it minimizes conflict
                // don't keep in current position if possible
                addQueen(x, j);
                int numConflicts = getNumConflicts(x);

                if (minConflictNum == -1 || numConflicts <= minConflictNum) {
                    minConflictNum = numConflicts;
                }

                conflictCounts[j] = numConflicts;
            }

            for (int j = 0; j < this.size; j++) {
                if (conflictCounts[j] == minConflictNum) {
                    minConflictIndices.add(j);

                    if (j != y) {
                        minConflictIndicesWithoutCurrent.add(j);
                    }
                }
            }

            int minConflictIndex = -1;
            if (minConflictIndicesWithoutCurrent.size() > 0) {
                minConflictIndex = getRandomEntry(minConflictIndicesWithoutCurrent);
            } else {
                minConflictIndex = getRandomEntry(minConflictIndices);
            }

            // move the queen there
            addQueen(x, minConflictIndex);
            return computePositionsRecursively(numTries + 1);
        }
    }

    // this creates a random initialization for the board
    private void initializeRandomly() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < this.size; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        this.board = list.toArray(this.board);
    }

    /**
     * This computes the fist set of positions for the N queens problem
     * 
     * @return true if solution found, false otherwise
     */
    public boolean computePositions() {
        boolean retVal = false;

        // no solution for < 4
        if (this.size < 4) {
            return false;
        }

        final int MAX_RANDOM_STARTS = 100;
        int i = 0;

        while (!retVal && i < MAX_RANDOM_STARTS) {
            initializeRandomly();
            retVal = computePositionsRecursively(0);
            i++;
        }

        // Only print the solution for small boards
        if (this.size <= 20 && retVal) {
            printBoard();
        }

        return retVal;

    }
}
