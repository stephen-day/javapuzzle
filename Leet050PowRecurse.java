package leet;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
Leet #50: Pow(x, n)

Implement pow(x, n), which calculates x raised to the power n (i.e., xn).

Example 1:
Input: x = 2.00000, n = 10
Output: 1024.00000

Example 2:
Input: x = 2.10000, n = 3
Output: 9.26100

Example 3:
Input: x = 2.00000, n = -2
Output: 0.25000
Explanation: 2-2 = 1/22 = 1/4 = 0.25

Constraints:

-100.0 < x < 100.0
-231 <= n <= 231-1
-104 <= xn <= 104

Leet Result:
Runtime: 0 ms, faster than 100.00% of Java online submissions for Pow(x, n).
Memory Usage: 38.2 MB, less than 74.11% of Java online submissions for Pow(x, n).
*/
public class Leet050PowRecurse
{
    @DataProvider
    public Object[][] myPowProvider() {
        return(new Object[][] {
            { 2.00000,              10,  1024.00000 },
            { 2.10000,               3,     9.26100 },
            { 2.00000,              -2,     0.25000 },
            { 0.999999999, -2147483648,     8.56328 },
            { 1.00000,     -2147483648,     1.00000 },
            { 2.00000,     -2147483648,     0.00000 },
            { 1.00000,      2147483647,     1.00000 },
            { -2.00000,             10,  1024.00000 },
        });
    }

    @Test(dataProvider = "myPowProvider")
    public void myPowTest(double x, int n, double expectedResult) {
        double result = myPow(x, n);

        // Verify
        assertEquals(result, expectedResult, 0.000009);
    }

    public double myPow(double x, int n) {
        if (x == 1.0 || n == 0)
            return 1.0;

        if (n == 1)
            return x;

        if (n == -1)
            return(1.0 / x);

        double p = myPow(x, n / 2);
        double l = myPow(x, n % 2);
        return(p * p * l);
    }
}
