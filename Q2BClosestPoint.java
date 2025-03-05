// This code implements a method to find the two adjacent points with the smallest Manhattan distance.
// It iterates over all combinations of points, computing their distances and updating the closest pair as needed.
// In cases where pairs have identical distances, it chooses the pair with lexicographically smallest indices.
// The method returns the indices of the closest pair, while the main function demonstrates this with sample coordinates and prints the result.


public class Q2BClosestPoint { // Class Qn2b definition

    // Method to determine the closest pair of points based on Manhattan distance,
    // selecting the pair with lexicographically smallest indices in case of a tie.
    public static int[] findClosestPair(int[] x_coords, int[] y_coords) {
        int n = x_coords.length; // Get the total number of points
        int minDist = Integer.MAX_VALUE; // Initialize the minimum Manhattan distance to a very high value
        int[] result = { 0, 0 }; // Array to store indices of the closest point pair, defaulting to [0, 0]

        // Iterate through every distinct pair of points (i, j) where i < j
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Compute the Manhattan distance for the current pair of points
                int dist = Math.abs(x_coords[i] - x_coords[j]) + Math.abs(y_coords[i] - y_coords[j]);

                // If a smaller distance is found, or the same distance with lexicographically earlier indices, update the result
                if (dist < minDist || (dist == minDist && (i < result[0] || (i == result[0] && j < result[1])))) {
                    minDist = dist; // Set the new minimum distance
                    result[0] = i; // Update the first index of the closest pair
                    result[1] = j; // Update the second index of the closest pair
                }
            }
        }

        return result; // Return the indices corresponding to the closest pair of points
    }

    public static void main(String[] args) {
        // Example test scenario
        int[] x_coords = { 1, 2, 3, 2, 4 }; // Define the x-coordinates for the test points
        int[] y_coords = { 2, 3, 1, 2, 3 }; // Define the y-coordinates for the test points

        // Invoke the findClosestPair method to determine the closest pair
        int[] closestPair = findClosestPair(x_coords, y_coords);

        // Output the indices of the nearest point pair
        System.out.println("Closest pair of points: [" + closestPair[0] + ", " + closestPair[1] + "]");
    }
}

// output
// Closest pair of points: [0, 3]
