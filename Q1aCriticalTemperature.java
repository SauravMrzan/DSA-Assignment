// The program determines the minimum number of measurements needed to pinpoint the highest safe temperature range using the available sample data.
// When there is one sample across two temperature settings, the process requires two measurements.
// For two samples covering six temperature levels, three measurement sets are needed.
// The program concludes that four measurements are necessary to efficiently identify the highest safe temperature when using three samples and fourteen temperature settings.
public class Q1aCriticalTemperature {

    // Function that computes the fewest measurements needed to identify the highest safe temperature.
    // It uses the number of samples (k) and the number of temperature settings (n) to determine the result.
    public static int minMeasurements(int k, int n) {
        // When only one sample is available, each temperature must be checked one by one.
        if (k == 1)
            return n;
        
        // Create a dynamic programming table where dp[i][j] denotes the maximum number of temperature settings
        // that can be tested using i samples and j measurements.
        int[][] dp = new int[k + 1][n + 1];

        int moves = 0; // Initialize the counter for the number of measurements.
        // Increase the measurement count until the DP table indicates we can cover all temperature levels.
        while (dp[k][moves] < n) {
            moves++;
            // Update the DP table for each available sample with the current number of measurements.
            for (int i = 1; i <= k; i++) {
                dp[i][moves] = 1 + dp[i - 1][moves - 1] + dp[i][moves - 1];
            }
        }
        return moves; // Return the minimal count of measurements required.
    }

    public static void main(String[] args) {
        // Execute test cases to validate the function
        System.out.println(minMeasurements(1, 2));  // Expected output: 2
        System.out.println(minMeasurements(2, 6));  // Expected output: 3
        System.out.println(minMeasurements(3, 14)); // Expected output: 4
    }
}
// Expected output:
// 2
// 3
// 4
