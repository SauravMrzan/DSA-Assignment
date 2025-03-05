// Question 1 A
public class Q1aCriticalTemperature {
    /**
     * This method calculates the minimum number of measurements required to reach a certain number of total measurements.
     * @param k The initial number of measurements.
     * @param n The target number of total measurements.
     * @return The minimum number of measurements required to reach the target.
     */
    public static int minMeasurements(int k, int n) {
        // Initialize an array to store the number of measurements at each step, with a size of k + 1.
        int[] dp = new int[k + 1];
        
        // Initialize a variable to store the minimum number of measurements required, starting at 0.
        int m = 0;
        
        // Continue the loop until the number of measurements at the k-th step is greater than or equal to the target number of measurements.
        while (dp[k] < n) {
            // Increment the minimum number of measurements required by 1.
            m++;
            
            // Iterate over the array in reverse order, starting from the k-th step.
            for (int i = k; i >= 1; i--) {
                // Update the number of measurements at the current step by adding the number of measurements at the previous step and the step before that, plus 1.
                dp[i] = dp[i] + dp[i - 1] + 1;
            }
        }
        
        // Return the minimum number of measurements required to reach the target.
        return m;
    }

    /**
     * The main method is used to test the minMeasurements method with example inputs.
     * @param args Command-line arguments (not used in this program).
     */
    public static void main(String[] args) {
        // Test the minMeasurements method with k = 1 and n = 2, and print the result.
        System.out.println(minMeasurements(1, 2));  // Output: 2
        System.out.println(minMeasurements(4,10)); //Output: 4
        System.out.println(minMeasurements(3,14)); //Output: 4
    }
}