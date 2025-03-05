// Question 6 B
/*
 * Multi-threaded Web Crawler using ExecutorService
 *
 * Approach:
 * - Uses a queue (`ConcurrentLinkedQueue<String>`) to store URLs to be crawled.
 * - Prevents duplicate crawling with a thread-safe `ConcurrentHashMap.newKeySet()`.
 * - Uses `ExecutorService` to run multiple threads (5 threads in this case).
 * - Each thread fetches a web page using HTTP GET and extracts data.
 * - Extracted URLs (simulated) are added back to the queue for further crawling.
 * - Gracefully shuts down once max pages are crawled.
 * - Stores crawled page data in a `ConcurrentHashMap<String, String>`.
 */

 import java.io.BufferedReader;
 import java.io.InputStreamReader;
 import java.net.HttpURLConnection;
 import java.net.URL;
 import java.util.Queue;
 import java.util.Set;
 import java.util.concurrent.*;
 
 public class Q6BWebCrawler {
     // Queue to store URLs that need to be crawled (Thread-safe)
     private final Queue<String> urlQueue = new ConcurrentLinkedQueue<>();
 
     // Set to keep track of visited URLs and avoid duplicates (Thread-safe)
     private final Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
 
     // Thread pool for managing multiple crawling threads
     private final ExecutorService executor = Executors.newFixedThreadPool(5);
 
     // Stores the fetched page content (Thread-safe)
     private final ConcurrentHashMap<String, String> crawledData = new ConcurrentHashMap<>();
 
     // Constructor initializes the queue with the starting URL
     public Q6BWebCrawler(String startUrl) {
         urlQueue.add(startUrl);
     }
 
     // Starts crawling up to maxPages
     public void startCrawling(int maxPages) {
         // Continue crawling while there are URLs left and max pages are not reached
         while (!urlQueue.isEmpty() && visitedUrls.size() < maxPages) {
             String url = urlQueue.poll(); // Get next URL
             if (url != null && visitedUrls.add(url)) { // Avoid duplicate crawling
                 executor.submit(() -> crawl(url)); // Assign task to thread pool
             }
         }
         shutdownExecutor(); // Shut down gracefully after crawling
     }
 
     // Fetches and processes a web page
     private void crawl(String url) {
         try {
             System.out.println("Crawling: " + url);
             String content = fetchContent(url); // Fetch page content
             crawledData.put(url, content); // Store the fetched data
             extractLinks(content); // Extract and add new URLs to queue
         } catch (Exception e) {
             System.err.println("Failed to crawl " + url + ": " + e.getMessage());
         }
     }
 
     // Fetches content of a web page using HTTP GET
     private String fetchContent(String urlString) throws Exception {
         StringBuilder content = new StringBuilder();
         URL url = new URL(urlString); // Convert string to URL object
         HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Open HTTP connection
         connection.setRequestMethod("GET"); // Set HTTP method to GET
 
         // Read response using BufferedReader
         try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
             String line;
             while ((line = reader.readLine()) != null) { // Read page content line by line
                 content.append(line);
             }
         }
         return content.toString(); // Return fetched content as a string
     }
 
     // Simulates extracting links from the fetched content
     private void extractLinks(String content) {
         // Normally, this would use regex or an HTML parser (e.g., Jsoup)
         if (visitedUrls.size() < 10) { // Limit added links for demo
             urlQueue.add("http://example.com/page" + (visitedUrls.size() + 1)); // Simulate new links
         }
     }
 
     // Graceful shutdown of thread pool
     private void shutdownExecutor() {
         executor.shutdown(); // Stop accepting new tasks
         try {
             if (!executor.awaitTermination(5, TimeUnit.SECONDS)) { // Wait for tasks to finish
                 executor.shutdownNow(); // Force shutdown if not completed in 5s
             }
         } catch (InterruptedException e) {
             executor.shutdownNow();
         }
     }
 
     // Prints the crawled page data
     public void printCrawledData() {
         System.out.println("Crawled Pages:");
         for (String url : crawledData.keySet()) {
             // Print first 100 characters of the page content for readability
             System.out.println(url + " -> " + crawledData.get(url).substring(0, Math.min(100, crawledData.get(url).length())) + "...");
         }
     }
 
     // Main method to start crawling
     public static void main(String[] args) {
         Q6BWebCrawler crawler = new Q6BWebCrawler("http://example.com"); // Start from example.com
         crawler.startCrawling(5); // Crawl up to 5 pages
         crawler.printCrawledData(); // Print crawled content
     }
 }