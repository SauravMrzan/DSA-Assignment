// The getTrendingHashtags method utilizes the Tweet class to manage  
// individual tweet information such as user IDs, tweet IDs, and dates,  
// while analyzing tweet content to identify the top three trending  
// hashtags along with their frequency of occurrence, presented in a table.
public class Q4ATrendingHashtags {

    // Define the Tweet class to hold tweet data such as user ID, tweet ID, date, and tweet content
    static class Tweet {
        int userId; // User ID of the individual who posted the tweet
        int tweetId; // Unique ID for each tweet
        int year; // Year the tweet was posted
        int month; // Month the tweet was posted
        int day; // Day the tweet was posted
        String tweet; // Content of the tweet

        // Constructor to initialize a Tweet object with necessary fields
        public Tweet(int userId, int tweetId, int year, int month, int day, String tweet) {
            this.userId = userId;
            this.tweetId = tweetId;
            this.year = year;
            this.month = month;
            this.day = day;
            this.tweet = tweet;
        }
    }

    // Method to identify and return trending hashtags from an array of tweets
    public static String[][] getTrendingHashtags(Tweet[] tweets) {
        // Array to store tweets from February 2024
        Tweet[] februaryTweets = new Tweet[tweets.length];
        int februaryCount = 0;

        // Filter tweets from February 2024
        for (Tweet tweet : tweets) {
            if (tweet.month == 2 && tweet.year == 2024) {
                februaryTweets[februaryCount++] = tweet;
            }
        }

        // Arrays to store unique hashtags and their frequencies
        String[] hashtags = new String[100]; // Array to store unique hashtags
        int[] counts = new int[100]; // Array to store the count of each hashtag
        int hashtagCount = 0; // Counter for the number of unique hashtags

        // Extract and count hashtags from February tweets
        for (int i = 0; i < februaryCount; i++) {
            // Extract hashtags from the tweet's content
            String[] tweetHashtags = extractHashtags(februaryTweets[i].tweet);
            for (String hashtag : tweetHashtags) {
                if (hashtag != null) {
                    // If the hashtag is new, add it; otherwise, increment the count
                    int index = findHashtag(hashtags, hashtag, hashtagCount);
                    if (index == -1) {
                        hashtags[hashtagCount] = hashtag;
                        counts[hashtagCount] = 1;
                        hashtagCount++;
                    } else {
                        counts[index]++; // Increment the count for an existing hashtag
                    }
                }
            }
        }

        // Sort the hashtags by count in descending order
        sortHashtags(hashtags, counts, hashtagCount);

        // Create and return the final result with the top 3 hashtags and their counts
        String[][] result = new String[3][2];
        for (int i = 0; i < 3 && i < hashtagCount; i++) {
            result[i][0] = hashtags[i]; // Store the hashtag
            result[i][1] = String.valueOf(counts[i]); // Store the count of the hashtag
        }
        return result; // Return the top trending hashtags
    }

    // Helper method to extract hashtags from a tweet's content
    private static String[] extractHashtags(String text) {
        String[] words = text.split(" "); // Split tweet into words by spaces
        String[] hashtags = new String[words.length]; // Array to store hashtags
        int count = 0; // Counter for number of hashtags

        // Loop through the words and collect hashtags
        for (String word : words) {
            if (word.startsWith("#")) { // Check if the word starts with a hashtag
                hashtags[count++] = word; // Add hashtag to the array
            }
        }
        return hashtags; // Return the array of hashtags
    }

    // Helper method to find the index of a hashtag in the array
    private static int findHashtag(String[] hashtags, String hashtag, int count) {
        for (int i = 0; i < count; i++) {
            if (hashtags[i].equals(hashtag)) {
                return i; // Return index if the hashtag already exists
            }
        }
        return -1; // Return -1 if the hashtag does not exist
    }

    // Helper method to sort hashtags by count (in descending order)
    private static void sortHashtags(String[] hashtags, int[] counts, int count) {
        // Perform a bubble sort to arrange hashtags based on their count and alphabetically if counts are the same
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                // Swap if the current count is smaller than the next count or if counts are equal, compare alphabetically
                if (counts[j] < counts[j + 1]
                        || (counts[j] == counts[j + 1] && hashtags[j].compareTo(hashtags[j + 1]) > 0)) {
                    int tempCount = counts[j];
                    counts[j] = counts[j + 1];
                    counts[j + 1] = tempCount;

                    String tempHashtag = hashtags[j];
                    hashtags[j] = hashtags[j + 1];
                    hashtags[j + 1] = tempHashtag;
                }
            }
        }
    }

    public static void main(String[] args) {
        // Create a sample array of tweets for testing
        Tweet[] tweets = new Tweet[] {
                new Tweet(135, 13, 2024, 2, 1, "Enjoying a great start to the day. #HappyDay #morningvibes"),
                new Tweet(136, 14, 2024, 2, 2, "Another #HappyDay with good vibes! #FeelGood"),
                new Tweet(137, 15, 2024, 2, 3, "Productivity hacks! #workLife #ProductiveDay"),
                new Tweet(138, 16, 2024, 2, 4, "Exploring new tech frontiers. #TechLife #Innovation"),
                new Tweet(139, 17, 2024, 2, 5, "Gratitude for today's moments. #HappyDay #Thankful"),
                new Tweet(140, 18, 2024, 2, 7, "Innovation drives us. #TechLife #FutureTech"),
                new Tweet(141, 19, 2024, 2, 9, "Connecting with nature's serenity. #Nature #Peaceful")
        };

        // Get the top trending hashtags for February 2024
        String[][] trendingHashtags = getTrendingHashtags(tweets);

        // Print the trending hashtags in a tabular format
        System.out.println("Output:");
        System.out.println("+---------------+-------+");
        System.out.println("| hashtag       | count |");
        System.out.println("+---------------+-------+");
        for (int i = 0; i < trendingHashtags.length && trendingHashtags[i][0] != null; i++) {
            System.out.printf("| %-13s | %-5s |\n", trendingHashtags[i][0], trendingHashtags[i][1]);
        }
        System.out.println("+---------------+-------+");
    }
}

// Output:
// +---------------+-------+
// | hashtag       | count |
// +---------------+-------+
// | #HappyDay     | 3     |
// | #TechLife     | 2     |
// | #FeelGood     | 1     |
// +---------------+-------+
