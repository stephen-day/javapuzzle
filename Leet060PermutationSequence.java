package leet;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
Leet #60: Permutation Sequence

The set [1, 2, 3, ..., n] contains a total of n! unique permutations.

By listing and labeling all of the permutations in order, we get the following sequence for n = 3:

"123"
"132"
"213"
"231"
"312"
"321"
Given n and k, return the kth permutation sequence.

Example 1:
Input: n = 3, k = 3
Output: "213"

Example 2:
Input: n = 4, k = 9
Output: "2314"

Example 3:
Input: n = 3, k = 1
Output: "123"

Constraints:

1 <= n <= 9
1 <= k <= n!

Leet Result:
Runtime: 0 ms, faster than 100.00% of Java online submissions for Permutation Sequence.
Memory Usage: 36.2 MB, less than 85.55% of Java online submissions for Permutation Sequence.
*/
public class Leet060PermutationSequence
{
    @DataProvider
    public Object[][] getPermutationProvider() {
        return(new Object[][] {
                    { 3, 3,     "213" },
                    { 4, 9,     "2314" },
                    { 3, 1,     "123" },
                    { 9, 3,     "123456879" },
                    { 9, 40320, "198765432" },
                    { 9, 40321, "213456789" },
                    { 1, 1,     "1" },
        });
    }

    @Test(dataProvider = "getPermutationProvider")
    public void getPermutationTest(int n, int k, String expectedResult) {
        String result = getPermutation(n, k);

        // Verify
        assertEquals(result, expectedResult);
    }

    private static int[] factorialArr = {1, 1, 2, 6, 24, 120, 720, 5040, 40320};

    public String getPermutation(int n, int k) {
        // Convert k from 1-based to 0-based.
        k--;

        // The value of 'n' gives 'n!' unique combinations. We can work out each digit of the 'k'th value by breaking it up into
        // its factorial components.
        //
        // For example:
        // - n=4 will have 24 unique combinations.
        // - For the first result digit, the first 6 (factorial(n-1)) combinations will start with the digit '1' while the second 6
        //   will start with '2', etc.
        // - For the second result digit, there will be 2 (factorial(n-2)) combinations for each unused digit. If the first result
        //   digit was '2', then there will be 2134/2143, 2314/2341, 2413/2431. Dividing by 2 tells us whether the second digit is
        //   a '1', '3', or '4'.
        char[] result = new char[n];
        boolean[] used = new boolean[n];

        for (int i = 1; i <= n; i++) {
            int factor = factorialArr[n - i];
            int val = (k / factor);
            result[i - 1] = (char) (getNthUnused(val + 1, used) + '0');
            k -= (val * factor);
        }

        return String.copyValueOf(result);
    }

    // Obtain the nth unused number (and mark it as used).
    private static int getNthUnused(int n, boolean[] used) {
        for (int i = 0; i < used.length; i++) {
            if (!used[i]) {
                if (--n == 0) {
                    used[i] = true;
                    return(i + 1);
                }
            }
        }

        return -1;
    }

    // Create an array of size n containing factorial values from zero to n-1.
    private static int[] calcFactorialArr(int n) {
        int[] factorialArr = new int[n];
        factorialArr[0] = 1;

        for (int i = 1; i < n; i++)
            factorialArr[i] = factorialArr[i-1] * i;

        return(factorialArr);
    }

    private static int factorial(int n) {
        int result = 1;
        for (int i = 2; i <= n; i++)
            result *= i;

        return(result);
    }
}
