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
Memory Usage: 37.4 MB, less than 92.13% of Java online submissions for Word Search II.
*/
public class Leet212WordSearchII
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

    public List<String> findWords(char[][] board, String[] words) {
        int maxWordLen = board.length * board[0].length;
        int r, c;
        List<String> resultWords = new LinkedList<>();

        // Determine the locations of each character.
        Map<Character, List<int[]>> charToPosMap = new HashMap<Character, List<int[]>>();

        for (r = 0; r < board.length; r++) {
            for (c = 0; c < board[0].length; c++) {
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

            for (r = 0; r < wordArr.length; r++)
                countArr[wordArr[r] - 'a']++;

            for (r = 0; r < wordArr.length; r++) {
                List<int[]> list = charToPosMap.get(wordArr[r]);

                if (list == null || countArr[wordArr[r] - 'a'] > list.size()) {
                    prune = true;
                    break;
                }
            }

            if (prune) continue;

            // Search for the word.
            for (int[] pos : charToPosMap.get(wordArr[0])) {
                if (matchNext(board, wordArr, pos[0], pos[1], 0)) {
                    resultWords.add(words[i]);
                    break;
                }
            }
        }

        return resultWords;
    }

    private static boolean matchNext(char[][] board, char[] word, int r, int c, int wordIdx) {
        if (board[r][c] != word[wordIdx])
            return false;

        if (++wordIdx == word.length)
            return true;

        board[r][c] = 0;

        if (  (r > 0 && matchNext(board, word, r - 1, c, wordIdx)) ||
              (r < board.length -1 && matchNext(board, word, r + 1, c, wordIdx)) ||
              (c > 0 && matchNext(board, word, r, c - 1, wordIdx)) ||
              (c < board[0].length -1 && matchNext(board, word, r, c + 1, wordIdx))) {
            board[r][c] = word[wordIdx - 1];
            return true;
        }

        board[r][c] = word[wordIdx - 1];
        return false;
    }
}
