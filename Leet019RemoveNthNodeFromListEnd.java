package leet;

import static org.testng.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;

/*
Leet #19: Given the head of a linked list, remove the nth node from the end of the list and return its head.

Example 1:

Input: head = [1,2,3,4,5], n = 2
Output: [1,2,3,5]
Example 2:

Input: head = [1], n = 1
Output: []
Example 3:

Input: head = [1,2], n = 1
Output: [1]

Constraints:

The number of nodes in the list is sz.
1 <= sz <= 30
0 <= Node.val <= 100
1 <= n <= sz

Leet Result:
Runtime: 0 ms, faster than 100.00% of Java online submissions for Remove Nth Node From End of List.
Memory Usage: 36.6 MB, less than 95.01% of Java online submissions for Remove Nth Node From End of List.
*/
public class Leet019RemoveNthNodeFromListEnd
{
    @DataProvider
    public Object[][] removeNthFromEndProvider() {
        return(new Object[][] {
                    { ImmutableList.of(1, 2, 3, 4, 5),   2,   ImmutableList.of(1, 2, 3, 5) },
                    { ImmutableList.of(1),               1,   ImmutableList.of() },
                    { ImmutableList.of(1, 2),            1,   ImmutableList.of(1) },
        });
    }

    @Test(dataProvider = "removeNthFromEndProvider")
    public void removeNthFromEndTest(List<Integer> inputList, int n, List<Integer> expectedList) {
        ListNode inputHead = convertListToNodes(inputList);
        ListNode resultHead = removeNthFromEnd(inputHead, n);
        List<Integer> resultList = convertNodesToList(resultHead);

        // Verify
        assertEquals(resultList, expectedList);
    }

    private static ListNode convertListToNodes(List<Integer> inputList) {
        ListNode head = null;
        ListIterator<Integer> reverseIterator = inputList.listIterator(inputList.size());

        while (reverseIterator.hasPrevious())
            head = new ListNode(reverseIterator.previous(), head);

        return head;
    }

    private static List<Integer> convertNodesToList(ListNode head) {
        List<Integer> resultList = new LinkedList<Integer>();

        for (ListNode node = head; node != null; node = node.next)
            resultList.add(node.val);

        return resultList;
    }

    public ListNode removeNthFromEnd(ListNode head, int n) {
        int gap = 0;
        ListNode node = head;

        // When gap==n, nthPrevNode will be the node immediately prior to the node we need to remove.
        ListNode nthPrevNode = head;

        for (;;) {
            node = node.next;
            if (node == null) {
                if (nthPrevNode == head && gap < n)
                    return nthPrevNode.next;

                if (nthPrevNode.next != null)
                    nthPrevNode.next = nthPrevNode.next.next;

                return head;
            }

            if (gap == n)
                nthPrevNode = nthPrevNode.next;
            else
                gap++;
        }
    }

    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int val) { this.val = val; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }
    }
}
