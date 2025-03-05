// Question 4 A
/*
 * This Java program defines a class `TrendingHashtags` that processes a list of tweets to find the top trending hashtags
 * from tweets posted in February 2024. It counts the occurrences of each hashtag and sorts them by frequency and 
 * lexicographic order. The program then prints the top three hashtags along with their counts.
 */

 import java.util.*;

 public class Q4ATrendingHashtags {
     
     // Method to find the top trending hashtags from a list of tweets
     public static List<Map.Entry<String, Integer>> findTopTrendingHashtags(List<Tweet> tweets) {
         Map<String, Integer> hashtagCount = new HashMap<>(); // Map to store hashtag counts
         
         // Process each tweet
         for (Tweet tweet : tweets) {
             // Only consider tweets from February 2024
             if (tweet.date.startsWith("2024-02")) {
                 // Extract hashtags from the tweet text
                 String[] words = tweet.text.split("\\s+"); // Split tweet text into words
                 for (String word : words) {
                     // Check if the word is a hashtag
                     if (word.startsWith("#")) {
                         // Update the count of the hashtag in the map
                         hashtagCount.put(word, hashtagCount.getOrDefault(word, 0) + 1);
                     }
                 }
             }
         }
 
         // Sort hashtags by count (descending) and then by lexicographic order
         List<Map.Entry<String, Integer>> sortedHashtags = new ArrayList<>(hashtagCount.entrySet()); // Create a list from the map entries
         sortedHashtags.sort((a, b) -> {
             if (!b.getValue().equals(a.getValue())) {
                 return b.getValue() - a.getValue(); // Sort by count descending
             }
             return a.getKey().compareTo(b.getKey()); // If same count, sort alphabetically
         });
 
         // Return the top 3 hashtags with counts
         return sortedHashtags.subList(0, Math.min(3, sortedHashtags.size())); // Get the top 3 or fewer if not enough hashtags
     }
 
     public static void main(String[] args) {
         // Sample tweets
         List<Tweet> tweets = Arrays.asList(
             new Tweet(135, "2024-02-01", "Enjoying a great start to the day. #HappyDay #MorningVibes"),
             new Tweet(136, "2024-02-03", "Another #HappyDay with good vibes! #FeelGood"),
             new Tweet(137, "2024-02-04", "Productivity peaks! #WorkLife #ProductiveDay"),
             new Tweet(138, "2024-02-04", "Exploring new tech frontiers. #TechLife #Innovation"),
             new Tweet(139, "2024-02-05", "Gratitude for today’s moments. #HappyDay #Thankful"),
             new Tweet(140, "2024-02-07", "Innovation drives us. #TechLife #FutureTech"),
             new Tweet(141, "2024-02-09", "Connecting with nature’s serenity. #Nature #Peaceful")
         );
 
         // Find top trending hashtags
         List<Map.Entry<String, Integer>> topHashtags = findTopTrendingHashtags(tweets);
 
         // Print result
         System.out.println("Hashtag\tCount"); // Print header
         for (Map.Entry<String, Integer> entry : topHashtags) {
             System.out.println(entry.getKey() + "\t" + entry.getValue()); // Print each hashtag and its count
         }
     }
 }
 
 // Tweet class to store tweet data
 class Tweet {
     int id; // Unique identifier for the tweet
     String date; // Date of the tweet
     String text; // Text content of the tweet
 
     // Constructor to initialize a Tweet object
     Tweet(int id, String date, String text) {
         this.id = id; // Set tweet ID
         this.date = date; // Set tweet date
         this.text = text; // Set tweet text
     }
 }