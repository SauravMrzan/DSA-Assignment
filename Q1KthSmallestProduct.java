// Question 1 A
import java.util.*;

/**
 * This program finds the kth smallest product that can be obtained 
 * by selecting one element from each of two sorted arrays.
 * It uses a binary search approach to efficiently locate the kth smallest product.
 */
public class Q1KthSmallestProduct {
    
    // Function to find the kth smallest product of two sorted arrays
    public static int findKthSmallestProduct(int[] returns1, int[] returns2, int k) {
        
        // Step 1: Define the search range for binary search
        int left = returns1[0] * returns2[0]; // Minimum possible product
        int right = returns1[returns1.length - 1] * returns2[returns2.length - 1]; // Maximum possible product
        
        // Step 2: Perform binary search on the product values
        while (left < right) {
            int mid = left + (right - left) / 2; // Calculate the mid-point product
            
            // Count how many products are <= mid
            int count = countLessOrEqual(returns1, returns2, mid);
            
            if (count < k) {
                left = mid + 1; // Increase the lower bound if count is too small
            } else {
                right = mid; // Decrease the upper bound if count is enough or more
            }
        }
        
        // Step 3: The kth smallest product is found at 'left'
        return left;
    }
    
    // Helper function to count the number of product pairs <= target value
    private static int countLessOrEqual(int[] returns1, int[] returns2, int target) {
        int count = 0; // Initialize count of valid products
        int j = returns2.length - 1; // Start from the last element of returns2
        
        // Iterate through each element in returns1
        for (int num1 : returns1) {
            // Find how many elements in returns2 produce a product <= target
            while (j >= 0 && num1 * returns2[j] > target) {
                j--; // Move left if product is too large
            }
            
            // Add valid count from returns2
            count += (j + 1); // Count all valid elements from index 0 to j
        }
        
        return count; // Return the total count of valid products
    }
    
    // Main function to test the implementation
    public static void main(String[] args) {
        // Test case 1: Small arrays
        int[] returns1 = {3, 7};
        int[] returns2 = {5, 8};
        int k = 2;
        System.out.println(findKthSmallestProduct(returns1, returns2, k)); // Output: 24

        // Test case 2: Includes negative and zero values
        int[] returns3 = {-4, -2, 0, 3};
        int[] returns4 = {2, 4};
        int k2 = 6;
        System.out.println(findKthSmallestProduct(returns3, returns4, k2)); // Output: 0
    }
}