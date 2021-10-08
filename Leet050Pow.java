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

Note: Leet testing cannot tell the difference between this approach (iterative) and the recursive approach as both are 0ms.
      But my testing shows that this approach is around 30% faster than the recursive approach.
*/
public class Leet050Pow
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

        return((n > 0) ? posPow(x, n) : negPow(x, n));
    }

    public double posPow(double x, int n) {
        double result = x;
        double multiplier = 1.0;
        n--;

        for (;;) {
            if (n == 0)
                return(result * multiplier);

            if (n == 1)
                return(result * x * multiplier);

            if (n % 2 == 1)
                multiplier *= result;

            result *= result;

            n /= 2;
        }
    }

    public double negPow(double x, int n) {
        double result = x;
        double multiplier = 1.0;
        n++;

        for (;;) {
            if (n == 0)
                return(1 / (result * multiplier));

            if (n == -1)
                return(1 / (result * multiplier * x));

            if (n % 2 == -1)
                multiplier *= result;

            result *= result;

            n /= 2;
        }
    }
}
