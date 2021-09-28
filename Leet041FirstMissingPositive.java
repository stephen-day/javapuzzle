package leet;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*
Leet #41: First Missing Positive

Given an unsorted integer array nums, return the smallest missing positive integer.

You must implement an algorithm that runs in O(n) time and uses constant extra space.

Example 1:
Input: nums = [1,2,0]
Output: 3

Example 2:
Input: nums = [3,4,-1,1]
Output: 2

Example 3:
Input: nums = [7,8,9,11,12]
Output: 1

Constraints:

1 <= nums.length <= 5 * 105
-231 <= nums[i] <= 231 - 1

Leet Result:
Runtime: 2 ms, faster than 98.87% of Java online submissions for First Missing Positive.
Memory Usage: 96.2 MB, less than 87.89% of Java online submissions for First Missing Positive.
*/
public class Leet041FirstMissingPositive
{
    @DataProvider
    public Object[][] firstMissingPositiveProvider() {
        return(new Object[][] {
                    { new int[] {1,2,0},        3 },
                    { new int[] {3,4,-1,1},     2 },
                    { new int[] {7,8,9,11,12},  1 },
        });
    }

    @Test(dataProvider = "firstMissingPositiveProvider")
    public void firstMissingPositiveTest(int[] nums, int expectedResult) {
        int result = firstMissingPositive(nums);

        // Verify
        assertEquals(result, expectedResult);
    }

    public int firstMissingPositive(int[] nums) {
        int i, j;

        for (i = 0; i < nums.length; i++) {
            while (nums[i] > 0 && nums[i] <= nums.length && nums[i] != i + 1) {
                j = nums[i] - 1;

                if (nums[i] == nums[j])
                    break;

                swap(i, j, nums);
            }
        }

        for (i = 0; i < nums.length; i++) {
            if (nums[i] != i + 1)
                break;
        }

        return(i + 1);
    }

    private static void swap(int i, int j, int[] nums) {
        int currNum = nums[i];
        nums[i] = nums[j];
        nums[j] = currNum;
    }
}
