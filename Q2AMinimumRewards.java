// minRewards computes the minimum rewards required based on employee ratings. 
// Initially, every employee is assigned one reward. 
// The algorithm then performs two sequential passes over the ratings array: 
// first, a left-to-right pass increases rewards for employees with higher ratings than their left neighbor, 
// and then a right-to-left pass adjusts rewards for those with higher ratings than their right neighbor. 
// Finally, the total rewards are calculated by summing all values in the rewards array and returning the result.


public class Q2AMinimumRewards { //  encapsulates the solution for allocating rewards based on ratings


    // Method to compute the minimum total rewards required based on employee ratings
    public static int minRewards(int[] ratings) {
        int n = ratings.length; // Determine the number of employees from the ratings array
        int[] rewards = new int[n]; // Initialize an array to hold the reward count for each employee

        // Initially, assign one reward to every employee as a starting point
        for (int i = 0; i < n; i++) {
            rewards[i] = 1; // Each employee gets at least 1 reward by default
        }

        // Perform a left-to-right scan:
        // For any employee with a higher rating than the previous one, increase their reward count
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) { // If current rating exceeds the rating of the left neighbor
                rewards[i] = rewards[i - 1] + 1; // Assign one more reward than the left neighbor
            }
        }

        // Perform a right-to-left scan:
        // Adjust rewards so that an employee with a higher rating than the right neighbor receives more rewards
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) { // If current rating is higher than the right neighbor's rating
                rewards[i] = Math.max(rewards[i], rewards[i + 1] + 1); // Update reward count to ensure proper increment
            }
        }

        // Sum all individual rewards to calculate the total rewards needed
        int totalRewards = 0; // Variable to accumulate the overall reward count
        for (int i = 0; i < n; i++) {
            totalRewards += rewards[i]; // Add each employee's reward to the total
        }

        return totalRewards; // Return the final computed total rewards
    }

    public static void main(String[] args) {
        // Execute sample test cases to validate the solution
        System.out.println("Minimum rewards needed: " + minRewards(new int[] { 1, 0, 2 })); // Test case 1: Expected output 5
        System.out.println("Minimum rewards needed: " + minRewards(new int[] { 1, 2, 2 })); // Test case 2: Expected output 4
    }
}

// Expected Output:
// Minimum rewards needed: 5
// Minimum rewards needed: 4
