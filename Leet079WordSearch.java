package leet;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
Leet #79: Word Search

Given an m x n grid of characters board and a string word, return true if word exists in the grid.

The word can be constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or vertically
neighboring. The same letter cell may not be used more than once.

Example 1:
Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"
Output: true

Example 2:
Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "SEE"
Output: true

Example 3:
Input: board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCB"
Output: false

Constraints:

m == board.length
n = board[i].length
1 <= m, n <= 6
1 <= word.length <= 15
board and word consists of only lowercase and uppercase English letters.

Leet Result:
Runtime: 14 ms, faster than 99.44% of Java online submissions for Word Search.
Memory Usage: 37.3 MB, less than 61.89% of Java online submissions for Word Search.
*/
public class Leet079WordSearch
{
    @DataProvider
    public Object[][] existProvider() {
        return(new Object[][] {
            { new char[][] {{'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}},   "ABCCED",  true  },
            { new char[][] {{'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}},   "SEE",     true  },
            { new char[][] {{'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}},   "ABCB",    false },
        });
    }

    @Test(dataProvider = "existProvider")
    public void existTest(char[][] board, String word, boolean expectedResult) {
        boolean result = exist(board, word);

        // Verify
        assertEquals(result, expectedResult);
    }

    public boolean exist(char[][] board, String word) {
        char[] wordArr = word.toCharArray();
        int r, c;

        // prune stage 1: not enough letters in board for passed word
        if (wordArr.length > board.length * board[0].length)
            return false;

        // prune stage 2: check that we have enough of each letter in the word.
        byte[] countArr = new byte['z' - 'A' + 1];

        for (r = 0; r < board.length; r++)
            for (c = 0; c < board[0].length; c++)
                countArr[board[r][c] - 'A']++;

        for (r = 0; r < wordArr.length; r++)
            if (--countArr[wordArr[r] - 'A'] < 0)
                return false;

        // Search the board.
        for (r = 0; r < board.length; r++)
            for (c = 0; c < board[0].length; c++)
                if (matchNext(board, wordArr, r, c, 0))
                    return true;

        return false;
    }

    private static boolean matchNext(char[][] board, char[] word, int r, int c, int wordIdx) {
        if (board[r][c] != word[wordIdx])
            return false;

        if (++wordIdx == word.length)
            return true;

        board[r][c] = 0;

        if (r > 0 && matchNext(board, word, r - 1, c, wordIdx))
            return true;

        if (r < board.length -1 && matchNext(board, word, r + 1, c, wordIdx))
            return true;

        if (c > 0 && matchNext(board, word, r, c - 1, wordIdx))
            return true;

        if (c < board[0].length -1 && matchNext(board, word, r, c + 1, wordIdx))
            return true;

        board[r][c] = word[wordIdx - 1];

        return false;
    }
}
