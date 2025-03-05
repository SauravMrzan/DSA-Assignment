// Question 6 A
/*
 * Approach:
 * This program uses three threads to print numbers in an interleaved sequence: "0102030405...".
 *  - A `NumberPrinter` class provides methods to print zeros, even numbers, and odd numbers.
 *  - A `ThreadController` class synchronizes three threads using a shared lock.
 *    - `printZero()`: Prints 0 before every number.
 *    - `printEven()`: Prints even numbers in order.
 *    - `printOdd()`: Prints odd numbers in order.
 *  - Synchronization is achieved using `wait()` and `notifyAll()` to prevent race conditions.
 */

 class NumberPrinter {
    public void printZero() {
        System.out.print(0);
    }

    public void printEven(int num) {
        System.out.print(num);
    }

    public void printOdd(int num) {
        System.out.print(num);
    }
}

class ThreadController {
    private final int n;
    private final NumberPrinter printer;
    private int counter = 1;
    private final Object lock = new Object();
    private boolean printZero = true; // Flag to alternate between 0 and numbers

    public ThreadController(int n, NumberPrinter printer) {
        this.n = n;
        this.printer = printer;
    }

    public void printZero() {
        for (int i = 0; i < n; i++) {
            synchronized (lock) {
                while (!printZero) { // Wait if not the turn to print zero
                    try { lock.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
                }
                printer.printZero();
                printZero = false; // Next, an actual number should be printed
                lock.notifyAll(); // Wake up number threads
            }
        }
    }

    public void printEven() {
        for (int i = 2; i <= n; i += 2) {
            synchronized (lock) {
                while (printZero || counter % 2 == 1) { // Wait if it's not an even number's turn
                    try { lock.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
                }
                printer.printEven(i);
                counter++;
                printZero = true; // Next, print a zero
                lock.notifyAll();
            }
        }
    }

    public void printOdd() {
        for (int i = 1; i <= n; i += 2) {
            synchronized (lock) {
                while (printZero || counter % 2 == 0) { // Wait if it's not an odd number's turn
                    try { lock.wait(); } catch (InterruptedException e) { e.printStackTrace(); }
                }
                printer.printOdd(i);
                counter++;
                printZero = true; // Next, print a zero
                lock.notifyAll();
            }
        }
    }
}

public class Q6APrintNumber {
    public static void main(String[] args) {
        int n = 5;
        NumberPrinter printer = new NumberPrinter();
        ThreadController controller = new ThreadController(n, printer);

        Thread zeroThread = new Thread(controller::printZero);
        Thread evenThread = new Thread(controller::printEven);
        Thread oddThread = new Thread(controller::printOdd);

        zeroThread.start();
        evenThread.start();
        oddThread.start();
    }
}