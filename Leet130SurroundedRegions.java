package leet;

/*
Leet #130: Surrounded Regions

Given an m x n matrix board containing 'X' and 'O', capture all regions that are 4-directionally surrounded by 'X'.

A region is captured by flipping all 'O's into 'X's in that surrounded region.

Example 1:
Input: board = [["X","X","X","X"],["X","O","O","X"],["X","X","O","X"],["X","O","X","X"]]
Output: [["X","X","X","X"],["X","X","X","X"],["X","X","X","X"],["X","O","X","X"]]
Explanation: Surrounded regions should not be on the border, which means that any 'O' on the border of the board are not flipped to
'X'. Any 'O' that is not on the border and it is not connected to an 'O' on the border will be flipped to 'X'. Two cells are
connected if they are adjacent cells connected horizontally or vertically.

Example 2:
Input: board = [["X"]]
Output: [["X"]]

Constraints:
m == board.length
n == board[i].length
1 <= m, n <= 200
board[i][j] is 'X' or 'O'.

Leet Result:
Runtime: 1 ms, faster than 99.82% of Java online submissions for Surrounded Regions.
Memory Usage: 40.9 MB, less than 88.90% of Java online submissions for Surrounded Regions.
*/
public class Leet130SurroundedRegions
{
    private int maxRowIdx;
    private int maxColIdx;

    // Basic approach is to do a DFS from borders inward to mark all reached positions as '#', then finally to mark
    // 'O's (unreached) as 'X' and change the '#' back to 'O'.
    //
    // For efficiency:
    // - Filter boards that have too little rows/columns (all positions reachable from border).
    // - Don't bother setting the outer border to '#' (i.e. process a window that is one less than the outer border).
    public void solve(char[][] board) {
        int rowCount = board.length;
        int colCount = board[0].length;

        if (rowCount <= 2 || colCount <= 2)
            return;

        maxRowIdx = rowCount - 1;
        maxColIdx = colCount - 1;

        int idx;

        // Process second row by checking above row.
        for (idx = 1; idx < maxColIdx; idx++) {
            if (board[1][idx] == 'O' && board[0][idx] == 'O')
                markFirstReached(board, 1, idx);
        }

        // Process second last row by checking last row.
        int prevIdx = maxRowIdx - 1;

        for (idx = 1; idx < maxColIdx; idx++) {
            if (board[prevIdx][idx] == 'O' && board[maxRowIdx][idx] == 'O')
                markFirstReached(board, prevIdx, idx);
        }

        // Process second column by checking first column.
        for (idx = 1; idx < maxRowIdx; idx++) {
            if (board[idx][1] == 'O' && board[idx][0] == 'O')
                markFirstReached(board, idx, 1);
        }

        // Process second last column by checking last column.
        prevIdx = maxColIdx - 1;

        for (idx = 1; idx < maxRowIdx; idx++) {
            if (board[idx][prevIdx] == 'O' && board[idx][maxColIdx] == 'O')
                markFirstReached(board, idx, prevIdx);
        }

        // Finished processing the board.
        // At this point, any 'O's are unreachable and any '#'s have been reached.
        // Note: idx=row and prevIdx=col
        for (idx = 1; idx < maxRowIdx; idx++) {
            for (prevIdx = 1; prevIdx < maxColIdx; prevIdx++) {
                if (board[idx][prevIdx] == 'O')
                    board[idx][prevIdx] = 'X';
                else if (board[idx][prevIdx] == '#')
                    board[idx][prevIdx] = 'O';
            }
        }
    }

    // Mark a row/col that we know exists and we know is 'O' as reached.
    private void markFirstReached(char[][] board, int row, int col) {
        board[row][col] = '#';

        markReached(board, row + 1, col);
        markReached(board, row - 1, col);
        markReached(board, row, col + 1);
        markReached(board, row, col - 1);
    }

    // If the passed row/col exists and has the value 'O', mark it as reached.
    private void markReached(char[][] board, int row, int col) {
        if (row < 1 || col < 1 || row >= maxRowIdx || col >= maxColIdx || board[row][col] != 'O')
            return;

        board[row][col] = '#';

        markReached(board, row + 1, col);
        markReached(board, row - 1, col);
        markReached(board, row, col + 1);
        markReached(board, row, col - 1);
    }
}
