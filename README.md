# N Queens with 3 in a line constraint

This is the solution to the N-queens problem (place N queens on an NxN chess board so they don't attack each other) with the additional constraint that no 3 queens are in a straight line at any angle

# Solutions

## NQueensBacktrack

This uses a simple array (index is the row number and the value is the column nuber) to store the position of the queens in the board. We go row by row to find a solution and use backtracking when we have no solution or when we have a solution (which we store), but want to find others. We also optimize checks for row (no check due to the way the algorithm runs), column (we store the num queens per column) and also the main and secondary diagonals (by storing the difference and sum of the x and y positions). This works well for small N and works better if we are only finding the first solution.

## NQueensMinConflict

This uses a min conflict algorithm. We don't include the structures from the backtracking algorithm since we need to count the number of conflicts -- solving for the 3 in a line can help solve for the column and diagonal. In this approach, rather than go row by row and backtrack, we start from an initial random position of the queens and then we pick a random queen that has conflits and move that queen to a position where it has the least amount of conflicts. In case of multiple such positions we pick a random one. Doing this converges the solution to the minimum. To prevent getting stuck in local mimima we have a max tries and if no solution is found in that, we start from another randome initial position. This solution performs much better than the backtracking solution. Without the extra contstraint, it finds a solution for upto N=100 in less than a sec and for N=500 in around a min.

# Running the program using gradle

To run use:

    ./gradlew run -PappArgs="[8]"

This will run the min conflict algorithm for 8x8. To try out the backtracking algorithm and list all solutions use

    ./gradlew run -PappArgs="[8, 'b', true]"

# Running the tests using gradle

To run the tests:

    ./gradlew test

This will run the tests for both the algorithms for a few cases and check the results.

# Additional notes and observations

I started this by implementing the backtracking algorithm. The advantage of the backtracking algorithm is that it can produce all the solutions (if we want). So I added this as a parameter, where if you want all the solutions, it takes a bit more time, but it will print all solutions. But by just requesting the first solution, we can do this faster and get faster results for a bigger n. In spite of this improvement, this was not good enough for big N. So I implemented the min conflicts algorithm. Actually I implmented that first for just the simple N queens problem. This gave an incredible boost to performance. I then modified it to make it work for the 3 in a line constraint. The performance, while not as good as the traditional N queens was still significantly better than that backtracking problem. I have included both the solutions here for comparison.
