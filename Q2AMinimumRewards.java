// Question 2 A
import java.util.*;

/**
 * This program calculates the minimum number of rewards needed
 * to distribute among employees based on their performance ratings.
 * Each employee must receive at least one reward, and employees
 * with higher ratings than their adjacent colleagues must receive more rewards.
 */
public class Q2AMinimumRewards {
    
    // Function to compute the minimum rewards required
    public static int minRewards(int[] ratings) {
        int n = ratings.length;
        int[] rewards = new int[n];
        Arrays.fill(rewards, 1); //  Assign 1 reward to every employee
        
        // Traverse from left to right, increasing rewards for increasing ratings
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                rewards[i] = rewards[i - 1] + 1;
            }
        }
        
        //Traverse from right to left, ensuring decreasing ratings are considered
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1);
            }
        }
        
        //Sum up the total rewards needed
        int totalRewards = 0;
        for (int reward : rewards) {
            totalRewards += reward;
        }
        
        return totalRewards;
    }
    
    // Main function to test the implementation
    public static void main(String[] args) {
        int[] ratings1 = {2, 0, 2};
        System.out.println(minRewards(ratings1)); // Output: 5
        
        int[] ratings2 = {0, 2, 2};
        System.out.println(minRewards(ratings2)); // Output: 4
    }
}