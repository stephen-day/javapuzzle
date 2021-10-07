package leet;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;

/*
Leet #212: Word Search II

Given an m x n board of characters and a list of strings words, return all words on the board.

Each word must be constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or vertically
neighboring. The same letter cell may not be used more than once in a word.

Example 1:
Input: board = [["o","a","a","n"],["e","t","a","e"],["i","h","k","r"],["i","f","l","v"]], words = ["oath","pea","eat","rain"]
Output: ["eat","oath"]

Example 2:
Input: board = [["a","b"],["c","d"]], words = ["abcb"]
Output: []

Constraints:

m == board.length
n == board[i].length
1 <= m, n <= 12
board[i][j] is a lowercase English letter.
1 <= words.length <= 3 * 104
1 <= words[i].length <= 10
words[i] consists of lowercase English letters.
All the strings of words are unique.

Leet Result:
Runtime: 8 ms, faster than 98.46% of Java online submissions for Word Search II.
Memory Usage: 37.6 MB, less than 83.06% of Java online submissions for Word Search II.

Notes: this implementation tries to improve speed by processing based on the rarest char in each word. This should help for larger
boards (especially where the word has a lot of close matches), but doesn't make much difference for the leet result).
*/
public class Leet212WordSearchIIRareChar
{
    @DataProvider
    public Object[][] findWordsProvider() {
        return(new Object[][] {
            { new char[][] {{'o','a','a','n'},{'e','t','a','e'},{'i','h','k','r'},{'i','f','l','v'}},
              new String[] {"oath","pea","eat","rain"},
              ImmutableList.of("eat","oath")},
            { new char[][] {{'a','b'},{'c','d'}},
              new String[] {"abcb"},
              ImmutableList.of()},
            { new char[][] {{'q','g','w','d','s','e','r','t','b'},
                            {'e','f','n','a','i','f','g','n','m'},
                            {'f','a','s','m','i','n','a','i','d'},
                            {'f','d','a','o','d','o','n','z','n'},
                            {'d','f','a','i','x','c','m','a','s'},
                            {'c','v','a','s','d','w','a','q','x'}},
              new String[]  {"amazing", "coding", "dims", "sam", "desert", "safe"},
              ImmutableList.of("amazing", "dims", "sam", "safe")},
        });
    }

    @Test(dataProvider = "findWordsProvider")
    public void findWordsTest(char[][] board, String[] words, List<String> expectedResult) {
        List<String> result = findWords(board, words);

        // Verify
        if (result.size() != expectedResult.size() ||
                !result.containsAll(expectedResult) ||
                !expectedResult.containsAll(result)) {
            throw new AssertionError("List [" + result + "] does not match expected [" + expectedResult + "]");
        }
    }

    private int rowLastIdx;
    private int colLastIdx;
    private int[] startPos;
    private int startIdx;

    public List<String> findWords(char[][] board, String[] words) {
        int maxWordLen = board.length * board[0].length;

        rowLastIdx = board.length - 1;
        colLastIdx = board[0].length - 1;

        int r, c;
        List<String> resultWords = new LinkedList<>();

        // Determine the locations of each character.
        Map<Character, List<int[]>> charToPosMap = new HashMap<Character, List<int[]>>();

        for (r = 0; r <= rowLastIdx; r++) {
            for (c = 0; c <= colLastIdx; c++) {
                Character currChar = board[r][c];
                List<int[]> list = charToPosMap.get(currChar);
                if (list == null) {
                    list = new LinkedList<int[]>();
                    charToPosMap.put(currChar, list);
                }
                list.add(new int[] { r, c });
            }
        }

        for (int i = 0; i < words.length; i++) {
            // prune stage 1: not enough letters in board for passed word
            if (words[i].length() > maxWordLen)
                continue;

            // prune stage 2: check that we have enough of each letter in the word.
            char[] wordArr = words[i].toCharArray();
            short[] countArr = new short[26];
            boolean prune = false;
            int bestIdx = 0;
            int bestCount = wordArr.length;

            for (r = 0; r < wordArr.length; r++)
                countArr[wordArr[r] - 'a']++;

            for (r = 0; r < wordArr.length; r++) {
                List<int[]> list = charToPosMap.get(wordArr[r]);

                if (list == null || countArr[wordArr[r] - 'a'] > list.size()) {
                    prune = true;
                    break;
                }

                // The best position to start from is the character in the word that is rarest within the board.
                if (bestCount > list.size()) {
                    bestCount = list.size();
                    bestIdx = r;
                }
            }

            if (prune) continue;

            // Search for the word.
            for (int[] pos : charToPosMap.get(wordArr[bestIdx])) {
                startPos = pos;
                startIdx = bestIdx;

                if ((bestIdx == 0 || isPrevCharAdjacent(board, wordArr[bestIdx - 1], pos[0], pos[1])) &&
                        matchNext(board, wordArr, pos[0], pos[1], bestIdx)) {
                    resultWords.add(words[i]);
                    break;
                }
            }
        }

        return resultWords;
    }

    // Recursively match all characters:
    // - First from the passed wordIdx to the end of the string.
    // - Second (using matchPrev()) from the startIdx to the start of the string.
    private boolean matchNext(char[][] board, char[] word, int r, int c, int wordIdx) {
        if (board[r][c] != word[wordIdx])
            return false;

        board[r][c] = 0;

        if (++wordIdx == word.length) {
            if (startIdx == 0) {
                board[r][c] = word[wordIdx - 1];
                return true;
            }

            // Temporarily reinstate the starting character (matchPrev() will check/clear this as it's first step).
            board[startPos[0]][startPos[1]] = word[startIdx];
            boolean m = matchPrev(board, word, startPos[0], startPos[1], startIdx);
            board[startPos[0]][startPos[1]] = 0;
            board[r][c] = word[wordIdx - 1];
            return m;
        }

        if (  (r > 0 && matchNext(board, word, r - 1, c, wordIdx)) ||
              (r < rowLastIdx && matchNext(board, word, r + 1, c, wordIdx)) ||
              (c > 0 && matchNext(board, word, r, c - 1, wordIdx)) ||
              (c < colLastIdx && matchNext(board, word, r, c + 1, wordIdx))) {
            board[r][c] = word[wordIdx - 1];
            return true;
        }

        board[r][c] = word[wordIdx - 1];
        return false;
    }

    // Recursively match all characters from the passed wordIdx to the start of the string.
    private boolean matchPrev(char[][] board, char[] word, int r, int c, int wordIdx) {
        if (board[r][c] != word[wordIdx])
            return false;

        if (--wordIdx < 0)
            return true;

        board[r][c] = 0;

        if (  (r > 0 && matchPrev(board, word, r - 1, c, wordIdx)) ||
              (r < rowLastIdx && matchPrev(board, word, r + 1, c, wordIdx)) ||
              (c > 0 && matchPrev(board, word, r, c - 1, wordIdx)) ||
              (c < colLastIdx && matchPrev(board, word, r, c + 1, wordIdx))) {
            board[r][c] = word[wordIdx + 1];
            return true;
        }

        board[r][c] = word[wordIdx + 1];
        return false;
    }

    // For the case where the rarest character is not the first character of the word, ensure that the prior character in the word
    // is adjacent.
    private boolean isPrevCharAdjacent(char[][] board, char prevChar, int r, int c) {
        return((r > 0 && board[r - 1][c] == prevChar) ||
               (r < rowLastIdx && board[r + 1][c] == prevChar) ||
               (c > 0 && board[r][c - 1] == prevChar) ||
               (c < colLastIdx && board[r][c + 1] == prevChar));
    }
}
