package leet;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;

/*
Leet #4: Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.

The overall run time complexity should be O(log (m+n)).

Example 1:
Input: nums1 = [1,3], nums2 = [2]
Output: 2.00000
Explanation: merged array = [1,2,3] and median is 2.

Example 2:
Input: nums1 = [1,2], nums2 = [3,4]
Output: 2.50000
Explanation: merged array = [1,2,3,4] and median is (2 + 3) / 2 = 2.5.

Example 3:
Input: nums1 = [0,0], nums2 = [0,0]
Output: 0.00000

Example 4:
Input: nums1 = [], nums2 = [1]
Output: 1.00000

Example 5:
Input: nums1 = [2], nums2 = []
Output: 2.00000

Constraints:

nums1.length == m
nums2.length == n
0 <= m <= 1000
0 <= n <= 1000
1 <= m + n <= 2000
-10^6 <= nums1[i], nums2[i] <= 10^6

Leet Result:
Runtime: 2 ms, faster than 99.90% of Java online submissions for Median of Two Sorted Arrays.
Memory Usage: 40 MB, less than 81.50% of Java online submissions for Median of Two Sorted Arrays.
*/
public class Leet004MedianTwoSortedArrays
{
    @DataProvider
    public Object[][] findMedianSortedArraysProvider() {
        return(new Object[][] {
                    { ImmutableList.of(1,3),          ImmutableList.of(2),      2.0 },
                    { ImmutableList.of(1,2),          ImmutableList.of(3,4),    2.5 },
                    { ImmutableList.of(0,0),          ImmutableList.of(0,0),    0.0 },
                    { ImmutableList.of(),             ImmutableList.of(1),      1.0 },
                    { ImmutableList.of(2),            ImmutableList.of(),       2.0 },
                    { ImmutableList.of(20,21,28),     ImmutableList.of(23),     22.0 }
        });
    }

    @Test(dataProvider = "findMedianSortedArraysProvider")
    public void findMedianSortedArraysTest(List<Integer> list1, List<Integer> list2, double expectedResult) {
        int[] arr1 = Ints.toArray(list1);
        int[] arr2 = Ints.toArray(list2);

        double result = findMedianSortedArrays(arr1, arr2);

        // Verify
        assertEquals(result, expectedResult);
    }

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int midPoint = ((nums1.length + nums2.length) / 2);
        int value = 0, prevValue = 0;
        int num1Idx = 0, num2Idx = 0;

        for (int position = 0; position <= midPoint; position++) {
            prevValue = value;

            if (num1Idx == nums1.length) {
                value = nums2[num2Idx++];
            }
            else if (num2Idx == nums2.length) {
                value = nums1[num1Idx++];
            }
            else if (nums1[num1Idx] < nums2[num2Idx]) {
                value = nums1[num1Idx++];
            }
            else {
                value = nums2[num2Idx++];
            }
        }

        if ((nums1.length + nums2.length) % 2 == 1)
            return value;

        return((value + prevValue) / 2.0);
    }
}
