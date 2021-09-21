package leet;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
Leet #5: Longest Palindromic Substring

Given a string s, return the longest palindromic substring in s.



Example 1:
Input: s = "babad"
Output: "bab"
Note: "aba" is also a valid answer.

Example 2:
Input: s = "cbbd"
Output: "bb"

Example 3:
Input: s = "a"
Output: "a"

Example 4:
Input: s = "ac"
Output: "a"

Constraints:
1 <= s.length <= 1000
s consist of only digits and English letters.

Leet Result:
Runtime: 13 ms, faster than 96.49% of Java online submissions for Longest Palindromic Substring.
Memory Usage: 38.9 MB, less than 90.28% of Java online submissions for Longest Palindromic Substring.
*/
public class Leet005LongestPalindromeSubstring
{
    @DataProvider
    public Object[][] longestPalindromeProvider() {
        return(new Object[][] {
                    { "babad",      "bab" },  // or "aba" also valid
                    { "cbbd",       "bb" },
                    { "a",          "a" },
                    { "ac",         "a" },
                    { "xradar",     "radar" },
                    { "aaaa",       "aaaa" }
        });
    }

    @Test(dataProvider = "longestPalindromeProvider")
    public void longestPalindromeTest(String s, String expectedResult) {
        String result = longestPalindrome(s);

        // Verify
        assertEquals(result, expectedResult);
    }

    int mBestStartIdx;
    int mBestLength;
    int mMaxIdx;

    public String longestPalindrome(String s) {
        mBestStartIdx = 0;
        mBestLength = 1;
        mMaxIdx = s.length() - 1;

        // There are two ways of making a palindrome.
        // - Double center letter ("abba") - even number of chars.
        // - Single center letter ("radar") - odd number of chars.
        for (int idx = 0; idx < mMaxIdx; idx++) {
            if (idx + 2 < s.length() && s.charAt(idx) == s.charAt(idx + 2))
                checkLongerPalindrome(s, idx, idx + 2);

            if (s.charAt(idx) == s.charAt(idx + 1))
                checkLongerPalindrome(s, idx, idx + 1);
        }

        return(s.substring(mBestStartIdx, mBestStartIdx + mBestLength));
    }

    // This method is called when we know we have a palindrome, but need to check if we actually have a longer one.
    private void checkLongerPalindrome(String s, int firstIdx, int lastIdx)
    {
        do {
            firstIdx--;
            lastIdx++;
        } while (firstIdx >= 0 && lastIdx < s.length() && s.charAt(firstIdx) == s.charAt(lastIdx));

        // Note: the indexes at this point are both one position past the edge of the actual range.
        int length = lastIdx - firstIdx - 1;
        if (length > mBestLength) {
            mBestLength = length;
            mBestStartIdx = firstIdx + 1;
            mMaxIdx = (s.length() - (mBestLength / 2));
        }
    }
}
