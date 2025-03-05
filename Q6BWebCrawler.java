// This Java program implements a multithreaded web crawler
// that begins from a given URL and stops once it reaches a
// specified page limit. It extracts links from the webpage
// content and ensures that only unique HTTP/HTTPS URLs are
// stored and processed. The program utilizes an ExecutorService
// for parallel execution and includes a retry mechanism for
// handling failed page fetches. The extracted webpage content
// is saved in a file while also displaying newly discovered links.

import java.util.*; // Importing Java utility classes for collections and data structures
import java.util.concurrent.*; // Importing Java classes for concurrency and threading
import java.net.*; // Importing networking classes for URL handling
import java.io.*; // Importing input/output classes for file operations and data handling

class WebCrawler {
    // A synchronized set to track visited URLs and prevent duplicate visits
    private final Set<String> visitedUrls = Collections.synchronizedSet(new HashSet<>());

    // A thread-safe queue to manage the URLs to be crawled
    private final Queue<String> urlQueue = new ConcurrentLinkedQueue<>();

    // A thread pool to handle parallel crawling tasks efficiently
    private final ExecutorService threadPool;

    // The maximum number of retries allowed for a failed URL request
    private final int maxRetries = 2;

    // Constructor to initialize the web crawler with a set number of threads
    public WebCrawler(int threadCount) {
        this.threadPool = Executors.newFixedThreadPool(threadCount);
    }

    // Method to initiate crawling from a given URL with a limit on the number of pages
    public void startCrawling(String startUrl, int maxPages) {
        urlQueue.add(startUrl);
        
        // Process URLs from the queue until the limit is reached
        while (!urlQueue.isEmpty() && visitedUrls.size() < maxPages) {
            String url = urlQueue.poll();

            if (url != null && !visitedUrls.contains(url)) {
                visitedUrls.add(url);
                threadPool.execute(() -> fetchPage(url, 0));
            }
        }
        threadPool.shutdown();
    }

    // Method to retrieve webpage content and retry if the request fails
    private void fetchPage(String url, int retryCount) {
        try {
            System.out.println("Crawling: " + url);
            URL website = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(website.openStream()));
            
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
                extractUrls(line);
            }
            reader.close();
            savePageContent(url, content.toString());
        } catch (Exception e) {
            System.out.println("Failed to access: " + url);
            if (retryCount < maxRetries) {
                System.out.println("Retrying: " + url);
                fetchPage(url, retryCount + 1);
            }
        }
    }

    // Extract and store unique URLs found within the webpage content
    private void extractUrls(String content) {
        String regex = "https?://[\\w\\.-]+";
        Scanner scanner = new Scanner(content);
        while (scanner.findInLine(regex) != null) {
            String foundUrl = scanner.match().group();
            if (!visitedUrls.contains(foundUrl)) {
                urlQueue.add(foundUrl);
                System.out.println("Discovered URL: " + foundUrl);
            }
        }
        scanner.close();
    }

    // Save the fetched webpage content into a local file
    private void savePageContent(String url, String content) {
        try {
            String fileName = "crawled_data.txt";
            FileWriter writer = new FileWriter(fileName, true);
            writer.write("Source URL: " + url + "\n" + content + "\n\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error while saving content for: " + url);
        }
    }

    // Main function to initiate the web crawling process with specific parameters
    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler(5);
        crawler.startCrawling("https://www.wikipedia.org", 10);
    }
}

// Output:
// Crawling: https://www.wikipedia.org
// Discovered URL: http://www.w3.org
// Discovered URL: http://www.w3.org
// Discovered URL: http://www.w3.org
// Discovered URL: http://www.w3.org
// Discovered URL: https://wikis.world
// Discovered URL: https://upload.wikimedia.org
// Discovered URL: https://meta.wikimedia.org
// Discovered URL: https://donate.wikimedia.org
// Discovered URL: https://en.wikipedia.org
// Discovered URL: https://play.google.com
// Discovered URL: https://itunes.apple.com
// Discovered URL: https://creativecommons.org
// Discovered URL: https://foundation.wikimedia.org
// Discovered URL: https://foundation.wikimedia.org
