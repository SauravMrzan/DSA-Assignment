// The ThreadController class ensures sequential number printing
// starting with 0, followed by odd numbers, then even numbers,
// and terminating at 5. The NumberPrinter class provides methods
// for printing 0, even numbers, and odd numbers separately.
// Synchronization is achieved through wait-notifyAll mechanisms,
// maintaining the correct execution sequence across three threads.

// The program effectively synchronizes three threads to print 
// the sequence 0 1 2 3 4 5 in a structured manner, preventing
// concurrency issues and ensuring ordered execution.

// NumberPrinter class handles number printing
class NumberPrinter {
    // Prints zero before numbers
    public void printZero() {
        System.out.print(0);
    }

    // Prints even numbers when called
    public void printEven(int number) {
        System.out.print(number);
    }

    // Prints odd numbers when called
    public void printOdd(int number) {
        System.out.print(number);
    }
}

// ThreadController synchronizes the number sequence execution
class ThreadController {
    private int n; // Defines the upper limit for number sequence
    private int count = 1; // Controls the order of printing
    private final Object lock = new Object(); // Lock object for thread safety

    // Constructor initializes the sequence limit
    public ThreadController(int n) {
        this.n = n;
    }

    // Prints 0 before each number, ensuring correct sequence
    public void printZero(NumberPrinter printer) throws InterruptedException {
        synchronized (lock) {
            for (int i = 0; i < n; i++) {
                while (count % 2 != 1) { // Ensures it's zero’s turn
                    lock.wait();
                }
                printer.printZero(); // Prints 0
                count++; // Updates counter
                lock.notifyAll(); // Notifies waiting threads
            }
        }
    }

    // Handles printing of even numbers in the correct sequence
    public void printEven(NumberPrinter printer) throws InterruptedException {
        synchronized (lock) {
            for (int i = 2; i <= n; i += 2) { // Iterates through even numbers
                while (count % 4 != 0) { // Ensures odd number prints first
                    lock.wait();
                }
                printer.printEven(i); // Prints even number
                count++; // Updates counter
                lock.notifyAll(); // Notifies waiting threads
            }
        }
    }

    // Handles printing of odd numbers, maintaining proper order
    public void printOdd(NumberPrinter printer) throws InterruptedException {
        synchronized (lock) {
            for (int i = 1; i <= n; i += 2) { // Iterates through odd numbers
                while (count % 4 != 2) { // Ensures 0 prints first
                    lock.wait();
                }
                printer.printOdd(i); // Prints odd number
                count++; // Updates counter
                lock.notifyAll(); // Notifies waiting threads
            }
        }
    }
}

// Main class initializes and starts the threads
public class Q6APrintNumber {
    public static void main(String[] args) {
        int n = 5; // Sets the sequence limit

        NumberPrinter printer = new NumberPrinter();
        ThreadController controller = new ThreadController(n);

        // Thread for printing 0
        Thread zeroThread = new Thread(() -> {
            try {
                controller.printZero(printer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Thread for printing even numbers
        Thread evenThread = new Thread(() -> {
            try {
                controller.printEven(printer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Thread for printing odd numbers
        Thread oddThread = new Thread(() -> {
            try {
                controller.printOdd(printer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // Start the threads
        zeroThread.start();
        oddThread.start();
        evenThread.start();
    }
}

// Expected output: 0102030405
