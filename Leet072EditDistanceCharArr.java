package leet;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
Leet #72: Edit Distance

Given two strings word1 and word2, return the minimum number of operations required to convert word1 to word2.

You have the following three operations permitted on a word:
- Insert a character
- Delete a character
- Replace a character

Example 1:
Input: word1 = "horse", word2 = "ros"
Output: 3
Explanation:
horse -> rorse (replace 'h' with 'r')
rorse -> rose (remove 'r')
rose -> ros (remove 'e')

Example 2:
Input: word1 = "intention", word2 = "execution"
Output: 5
Explanation:
intention -> inention (remove 't')
inention -> enention (replace 'i' with 'e')
enention -> exention (replace 'n' with 'x')
exention -> exection (replace 'n' with 'c')
exection -> execution (insert 'u')


Constraints:

0 <= word1.length, word2.length <= 500
word1 and word2 consist of lowercase English letters.

Leet Result:
Runtime: 3 ms, faster than 99.51% of Java online submissions for Edit Distance.
Memory Usage: 39 MB, less than 79.27% of Java online submissions for Edit Distance.

This is a variant that uses a char[] rather than String.charAt(). I found this to be faster for large tests, but Leet rated it
as similar.
*/
public class Leet072EditDistanceCharArr
{
    @DataProvider
    public Object[][] minDistanceProvider() {
        return(new Object[][] {
                    { "horse", "ros",           3 },
                    { "intention", "execution", 5 },
                    { "intention", "intent",    3 },
                    { "tent", "intent",         2 },  // leading trim.
                    { "tent", "tenting",        3 },  // trailing trim.
                    { "aabcc", "aacc",          1 },  // lead trim, trail trim, single del.
                    { "aabxcc", "aadcc",        2 },  // lead trim, trail trim, del + replace.
                    { "aaxbxcc", "aabcc",       2 },  // lead trim, trail trim, 2 x del.
                    { "intention", "tension",   3 },  // lead trim plus single replace.
                    { "asdfghjkzzzyyb","qwertyuizyya", 11},  // 8 replaces, keep z, remove 2x z, replace b.
                    { "xxxxxxxtesta","testazzz", 10},  // del 7 plus del 3
                    { "xxxxxxxtesta","testazzzz", 11},  // del 7 plus del 4
                    { "xxxxxxxtesta","testazzzzz", 12},  // replace 10, del 2.
                    { "testaxxxxxx","xtestaz", 7},  // del 1, replace 1, del 5.
                    { "testaxxxxxxtestaaqwer","testxtestazqwer", 7},
                    { "jhlsehkjbvublaoiersnapwoekjdfwupersjbdakeweufgaofgahwusjhkauhuyhvghfskaifailualeflaeflahesfahefaslkagkeksjfluaegfasjdfblkauhwfoqhiohnghbluawhlfkjsdfjauoehfasdfkyhlkusahjkrhturvbjxouahwoerufalfboautoahfahlahlashasuhlasdhfljksadhfiluhslkfjshdfkljabouhpoweqhkjasfpskdfaskjlfhpowhefalkdsfkajsdfluwblcaubkuawefqpouwfbasjbdapuweyhpasdjfapoiewfnaupgofuasfjasfuhalsfhlkjsdbavalouhfaosbasbdlauhefuahsdflashdfohiawshfoadlvaufhaoiufhaofasdaobsoufhapowifhaoashfpioahefoiahfahoweifhafaiewhfaoiwehfpaoiwhefawoihfow",
                      "wueqbwubowebpwopasodhvzxbzduoiawpeyasfauefluasjflkiuaefoquiwajsbdklfuagweuifglksjfdawiulefuweibfsjxzbkjxhgiwueysjbdguasbvyvutyvfgogyugttsbdgodfagaffaghpoaisofasidhfopaiufjabsoudgaoeuhgrfpasdfpouawgefoasbhdfujabgwuftevrthhgkjfxertyytgidyuodusifdosjtfdkgjhtrgwkehloiuyfiuybtcifyvrbnsyutyaeytwdgtuymipoupuyubtuyztextrzvyztreuytcxuiybivouypoiuhiuyisoudytroiuyhoiewhdfkjasheiuoryawiothjgyhtyfifdyhgosfrdkjgfeulhrqughefjsgiuyatyuqrgwjhtgjhgyuyutdfrguyharyugfegwjhflhawgelwhgeroiuw",
                      407},
                    { "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                      "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb",
                      500}
        });
    }

    @Test(dataProvider = "minDistanceProvider")
    public void getPermutationTest(String word1, String word2, int expectedResult) {
        int result = minDistance(word1, word2);

        // Verify
        assertEquals(result, expectedResult);
    }

/*
Optimized algorithm:
- Because we only want a count (and not details of the operations), an insert is the same as a delete on the other string. For
  example, "rate"/"rat" has a single operation (delete 'e' on first word, or insert 'e' on second word). This means that we only
  need to cater for delete and replace operations (not insert).
- Replace (one operation) is better than Del+Add (two operations) unless deleting char(s) from word1 causes word1 to start with the
  same character as word2. Hence, to determine whether delete is better than replace, we find the next occurrence of word2.charAt(0)
  within word1. If deleting all the characters prior to that occurrence could result in a smaller number of operations, then we
  recursively check that possible path.
- Initial code to ignore matching chars at the start/end of the words.
*/
    private char[] lWord;
    private char[] sWord;
    private int lWordEndIdx;
    private int sWordEndIdx;
    private short[][] dpArr;

    public int minDistance(String word1, String word2) {
        if (word1.length() > word2.length()) {
            lWord = word1.toCharArray();
            sWord = word2.toCharArray();
        }
        else {
            lWord = word2.toCharArray();
            sWord = word1.toCharArray();
        }

        // Omit the leading matching part of the word.
        //
        // If the shorter word is an exact match for the leading chars of the longer word, we have a match OR a few chars to remove
        // from the longer word. This also caters for the case where the shorter word has zero length.
        int beginIdx = 0;
        while (beginIdx < lWord.length && beginIdx < sWord.length && lWord[beginIdx] == sWord[beginIdx])
            beginIdx++;

        if (beginIdx == sWord.length)
            return(lWord.length - sWord.length);

        // Omit the trailing matching part of the word.
        lWordEndIdx = lWord.length - 1;
        sWordEndIdx = sWord.length - 1;
        while (lWordEndIdx >= beginIdx && sWordEndIdx >= beginIdx && lWord[lWordEndIdx] == sWord[sWordEndIdx]) {
            lWordEndIdx--;
            sWordEndIdx--;
        }

        // If there are zero or 1 unmatched chars in the shorter string, we can return at this point.
        //
        // Examples:
        // 1. "aabcc", "aacc", beginIdx=2, lWordEndIdx=2, sWordEndIdx=1. Return 1 (add 'b').
        // 2. "aabxcc", "aadcc", beginIdx=2, lWordEndIdx=3, sWordEndIdx=2. Return 2 (replace 'b' with 'd' and add 'x').
        // 3. "aaxbxcc", "aabcc", beginIdx=2, lWordEndIdx=4, sWordEndIdx=2. Return 2 (del 'x' and 'x').
        if (sWordEndIdx <= beginIdx)
            return countChangesForCloseMatch(lWord, beginIdx, lWordEndIdx, sWord, beginIdx, sWordEndIdx);

        // Calculate worstOperCount (worst case scenario where we have to replace every char for whole length and del excess chars).
        dpArr = new short[lWord.length][sWord.length];

        // Char at beginIdx doesn't match. We have at least 2 operations required (beginIdx cannot point to end of string).
        return getOpCount(beginIdx, beginIdx, 0);
    }

    // Recursively obtain the operation count for the sub-sections of the words lBeginIdx-lWordEndIdx and sBeginIdx-sWordEndIdx.
    //
    // Note that lWord may not be longer than sWord at the point where this is called.
    private int getOpCount(int lBeginIdx, int sBeginIdx, int opCountSoFar) {
        if (dpArr[lBeginIdx][sBeginIdx] != 0)
            return opCountSoFar + dpArr[lBeginIdx][sBeginIdx];

        int opCount = getOpCountMain(lBeginIdx, sBeginIdx, opCountSoFar);

        dpArr[lBeginIdx][sBeginIdx] = (short) (opCount - opCountSoFar);

        return(opCount);
    }

    // Recursively obtain the operation count for the sub-sections of the words lBeginIdx-lWordEndIdx and sBeginIdx-sWordEndIdx.
    //
    // Note that lWord may not be longer than sWord at the point where this is called.
    private int getOpCountMain(int lBeginIdx, int sBeginIdx, int opCountSoFar) {
        // Omit the leading matching part of the word.
        //
        // If we're left with zero or one unmatched chars in the shorter word, then we're done.
        while (lBeginIdx <= lWordEndIdx && sBeginIdx <= sWordEndIdx && lWord[lBeginIdx] == sWord[sBeginIdx]) {
            lBeginIdx++;
            sBeginIdx++;
        }

        int sWordLen = sWordEndIdx - sBeginIdx + 1;
        int lWordLen = lWordEndIdx - lBeginIdx + 1;

        if (sBeginIdx >= sWordEndIdx || lBeginIdx >= lWordEndIdx) {
            if (sWordLen < lWordLen)
                return(opCountSoFar + countChangesForCloseMatch(lWord, lBeginIdx, lWordEndIdx, sWord, sBeginIdx, sWordEndIdx));

            return(opCountSoFar + countChangesForCloseMatch(sWord, sBeginIdx, sWordEndIdx, lWord, lBeginIdx, lWordEndIdx));
        }

        // Char at beginIdx's doesn't match. We have at least 2 operations required (and beginIdx's cannot point to end of string).
        //
        // Find the next index of the current char (short and long words). This char needs to be close enough to the current
        // position that it is worth deleting all the chars between the current position and that found position.
        //
        // The formula used here is (wordLenA - (wordLenB/2)). For example:
        // - "xxxtestx", "taestz".
        //   lBeginIdx=0, lWordEndIdx=7, lWordLen=8, sBeginIdx=0, sWordEndIdx=5, sWordLen=6, limit=5.
        //
        // - "xxxxxxxtestx", "taestz".
        //   lBeginIdx=0, lWordEndIdx=11, lWordLen=12, sBeginIdx=0, sWordEndIdx=5, sWordLen=6, limit=9.
        //
        // - "xxxxxxxtesta", "testazzz".
        //   lBeginIdx=0, lWordEndIdx=11, lWordLen=12, sBeginIdx=0, sWordEndIdx=7, sWordLen=8, limit=8.
        //   best: del 7 + del 3 (10)
        //
        // - "xxxxxxxtesta", "testazzzz".
        //   lBeginIdx=0, lWordEndIdx=11, lWordLen=12, sBeginIdx=0, sWordEndIdx=8, sWordLen=9, limit=8.
        //   best: del 7 + del 4 (11)
        //
        // - "xxxxxxxtesta", "testazzzzz".
        //   lBeginIdx=0, lWordEndIdx=11, lWordLen=12, sBeginIdx=0, sWordEndIdx=9, sWordLen=10, limit=7.
        //   lNextIdx = -1 (no 't' in first 7 chars). Has a best case of 12 (del 7 + del 5) which can be achieved by replace.
        //
        // - "testaxxxxxx", "xtestaz".
        //   lBeginIdx=0, lWordEndIdx=10, lWordLen=11, sBeginIdx=0, sWordEndIdx=6, sWordLen=7, limit=7.
        //   lNextIdx = -1 (no 't' in first 7 chars). Has a best case of 12 (del 7 + del 5) which can be achieved by replace.
        //
        // The '-1' on the limit is because we '+1' on the startIdx. The '+1' on the startIdx is because we already know the first
        // char mismatches.

        int lNextIdx = nextIdxOf(lWord, sWord[sBeginIdx], lBeginIdx + 1, lWordLen - (sWordLen / 2) - 1);
        int sNextIdx = nextIdxOf(sWord, lWord[lBeginIdx], sBeginIdx + 1, sWordLen - (lWordLen / 2) - 1);

        // If we can find a (close enough) next occurrence, it may be worth deleting chars between the two points.
        int lCountForDel = Integer.MAX_VALUE;
        if (lNextIdx > 0) {
            lCountForDel = getOpCount(lNextIdx + 1, sBeginIdx + 1, opCountSoFar + lNextIdx - lBeginIdx);
        }

        int sCountForDel = Integer.MAX_VALUE;
        if (sNextIdx > 0) {
            sCountForDel = getOpCount(lBeginIdx + 1, sNextIdx + 1, opCountSoFar + sNextIdx - sBeginIdx);
        }

        // A replace is normally of a single character (move one position forward in both words and increment opCountSoFar by 1).
        //
        // However, if we have repeating characters in both words, we know we've already looked forward for this char combination
        // so we can simply replace multiple chars.
        int replaceCount = 1;
        while (lBeginIdx + replaceCount < lWordEndIdx &&
               sBeginIdx + replaceCount < sWordEndIdx &&
               lWord[lBeginIdx] == lWord[lBeginIdx + replaceCount] &&
               sWord[sBeginIdx] == sWord[sBeginIdx + replaceCount]) {
            replaceCount++;
        }

        int countForReplace = getOpCount(lBeginIdx + replaceCount, sBeginIdx + replaceCount, opCountSoFar + replaceCount);

        return(Math.min(countForReplace, Math.min(lCountForDel, sCountForDel)));
    }

    // Determine the next index of the passed char, starting at startIdx and considering at most 'limit' characters.
    private static int nextIdxOf(char[] w, char findChar, int startIdx, int limit) {
        if (limit > 0) {
            // Change limit to an endIdx.
            limit += startIdx;

            do {
                if (w[startIdx] == findChar)
                    return startIdx;

                startIdx++;
            } while (startIdx < limit);
        }

        return -1;
    }

    // Called when we know that the shorter word is either matched or has one mismatched character (sEndIdx <= sBeginIdx).
    //
    // The caller must ensure that lWord.length() >= sWord.length().
    private static int countChangesForCloseMatch(char[] lWord, int lBeginIdx, int lEndIdx,
                                                 char[] sWord, int sBeginIdx, int sEndIdx) {
        if (sEndIdx == sBeginIdx)
            for (int idx = lBeginIdx + 1; idx <= lEndIdx; idx++)
                if (lWord[idx] == sWord[sBeginIdx])
                    return(lEndIdx - lBeginIdx);

        return(lEndIdx - lBeginIdx + 1);
    }
}
