package leet;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
Leet #782: Transform to Chessboard

You are given an n x n binary grid board. In each move, you can swap any two rows with each other, or any two columns with each
other.

Return the minimum number of moves to transform the board into a chessboard board. If the task is impossible, return -1.

A chessboard board is a board where no 0's and no 1's are 4-directionally adjacent.

Example 1:
Input: board = [[0,1,1,0],[0,1,1,0],[1,0,0,1],[1,0,0,1]]
Output: 2
Explanation: One potential sequence of moves is shown.
The first move swaps the first and second column.
The second move swaps the second and third row.

Example 2:
Input: board = [[0,1],[1,0]]
Output: 0
Explanation: Also note that the board with 0 in the top left corner, is also a valid chessboard.

Example 3:
Input: board = [[1,0],[1,0]]
Output: -1
Explanation: No matter what sequence of moves you make, you cannot end with a valid chessboard.

Constraints:

n == board.length
n == board[i].length
2 <= n <= 30
board[i][j] is either 0 or 1.

Leet Result:
Runtime: 1 ms, faster than 100.00% of Java online submissions for Transform to Chessboard.
Memory Usage: 38.7 MB, less than 64.29% of Java online submissions for Transform to Chessboard.
*/
public class Leet782TransformToChessboard
{
    @DataProvider
    public Object[][] movesToChessboardProvider() {
        return(new Object[][] {
                    { new int[][] {{0,1,1,0},{0,1,1,0},{1,0,0,1},{1,0,0,1}},    2 },
                    { new int[][] {{0,1},{1,0}},                                0 },
                    { new int[][] {{1,0},{1,0}},                                -1 },
                    { new int[][] {{1,1,1,0,0},{1,1,1,0,0},{1,1,1,0,0},{0,0,0,1,1},{0,0,0,1,1}},   2 },
                    { new int[][] {{0,1,0,1,1},{1,0,1,0,0},{0,1,0,1,1},{1,0,1,0,0},{0,1,0,1,1}},   2 },
                    { new int[][] {{0,1,0,1,1},{0,1,0,1,1},{0,1,0,1,1},{1,0,1,0,0},{1,0,1,0,0}},   3 },
                    { new int[][] {{1,1,0},{0,0,1},{0,0,1}},   2 },
                    { new int[][] {{1,0,0},{0,1,1},{0,1,1}},   2 },
                    { new int[][] {{0,1,0},{1,0,1},{1,0,1}},   1 },
                    { new int[][] {{0,0,1,0,1,1},{1,1,0,1,0,0},{1,1,0,1,0,0},{0,0,1,0,1,1},{1,1,0,1,0,0},{0,0,1,0,1,1}},   2 },
                    { new int[][] {{1,1,1,0,0,1,0,0},{0,0,0,1,1,0,1,1},{1,1,1,0,0,1,0,0},{0,0,0,1,1,0,1,1},
                                   {0,0,0,1,1,0,1,1},{0,0,0,1,1,0,1,1},{1,1,1,0,0,1,0,0},{1,1,1,0,0,1,0,0}},               3 },


        });
    }

    @Test(dataProvider = "movesToChessboardProvider")
    public void movesToChessboardTest(int[][] board, int expectedResult) {
        int result = movesToChessboard(board);

        // Verify
        assertEquals(result, expectedResult);
    }

    public int movesToChessboard(int[][] board) {
        int boardLength = board[0].length;
        int idx;

        // Count the zero's and one's to ensure same (or out by 1 for odd length board).
        int zeroCount = 0;
        int colZeroCount = 0;

        for (idx = 0; idx < boardLength; idx++) {
            if (board[0][idx] == 0)   // each column for row zero.
                zeroCount++;

            if (board[idx][0] == 0)   // each row for column zero.
                colZeroCount++;
        }

        // Validate that the first row and first column have the correct number of zero's and one's.
        // - This will be the same count for and even length board, and a difference of 1 for an odd length board.
        //
        // Also calculate leadingDigitForFirstRow and leadingDigitForFirstCol.
        //
        // For even length boards:
        // - The leading digit is the one that will have more digits starting in the correct position.
        // - For smaller boards (length 2 or 4), we can just keep board[0][0] as we'll have either zero or one move.
        //
        // For odd length boards:
        // - The leading digit is the one that occurs more frequently (e.g. must be 1 in 10101).
        int oneCount = boardLength - zeroCount;
        int leadingDigitForFirstRow = 0;
        int leadingDigitForFirstCol = 0;

        if (boardLength % 2 == 0) {
            if (zeroCount != oneCount || zeroCount != colZeroCount)
                return -1;

            if (boardLength > 4) {
                // Re-use these variables to indicate the number of matches against the zeroth element (board[0][0]).
                zeroCount = 1;
                colZeroCount = 1;

                for (idx = 2; idx < boardLength; idx += 2) {
                    if (board[0][idx] == board[0][0])
                        zeroCount++;

                    if (board[idx][0] == board[0][0])
                        colZeroCount++;
                }

                int reqdMatchCount = (boardLength / 4);
                leadingDigitForFirstRow = (zeroCount > reqdMatchCount) ? board[0][0] : board[0][0] ^ 1;
                leadingDigitForFirstCol = (colZeroCount > reqdMatchCount) ? board[0][0] : board[0][0] ^ 1;
            }
            else {
                leadingDigitForFirstRow = board[0][0];
                leadingDigitForFirstCol = board[0][0];
            }
        }
        else {
            if (zeroCount + 1 == oneCount) {
                leadingDigitForFirstRow = 1;

                if (colZeroCount == zeroCount)
                    leadingDigitForFirstCol = 1;
                else if (colZeroCount != oneCount)
                    return -1;
            }
            else if (zeroCount - 1 == oneCount) {
                if (colZeroCount == oneCount)
                    leadingDigitForFirstCol = 1;
                else if (colZeroCount != zeroCount)
                    return -1;
            }
            else {
                return -1;
            }
        }

        // Each row starting with 1 needs to be the same as other rows starting with 1.
        // Each row starting with 0 needs to contain the opposite values of the row starting with 1.
        for (int row = 1; row < boardLength; row++) {
            boolean reverse = (board[0][0] != board[row][0]);

            for (int col = 1; col < boardLength; col++)
                if ((board[0][col] == board[row][col]) == reverse)
                    return -1;
        }

        // Consider first row - how many moves to make the pattern 10101.
        // Consider first column - how many moves to make the pattern 10101
        //
        // We only need to check every second/even position (indexes 1,3,5,etc). If these are ok, then the odd positions will be ok
        // and if these are not ok, then we'll need to swap each incorrect even col with an incorrect odd column.
        int moveCount = 0;

        for (idx = 1; idx < boardLength; idx += 2) {
            if (board[0][idx] == leadingDigitForFirstRow)
                moveCount++;

            if (board[idx][0] == leadingDigitForFirstCol)
                moveCount++;
        }

        return(moveCount);
    }
}
