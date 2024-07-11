package Exercise2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class FizzBuzzMultithreaded {
    private final int n;
    private final AtomicInteger current;
    private final BlockingQueue<String> queue;

    public FizzBuzzMultithreaded(int n) {
        this.n = n;
        this.current = new AtomicInteger(1);
        this.queue = new LinkedBlockingQueue<>();
    }

    // Потік A
    public void fizz() {
        while (true) {
            int num = current.get();
            if (num > n) break;
            if (num % 3 == 0 && num % 5 != 0) {
                queue.add("fizz");
                current.incrementAndGet();
            }
        }
    }

    // Потік B
    public void buzz() {
        while (true) {
            int num = current.get();
            if (num > n) break;
            if (num % 5 == 0 && num % 3 != 0) {
                queue.add("buzz");
                current.incrementAndGet();
            }
        }
    }

    // Потік C
    public void fizzbuzz() {
        while (true) {
            int num = current.get();
            if (num > n) break;
            if (num % 3 == 0 && num % 5 == 0) {
                queue.add("fizzbuzz");
                current.incrementAndGet();
            }
        }
    }

    // Потік D
    public void number() {
        while (true) {
            int num = current.get();
            if (num > n) break;
            if (num % 3 != 0 && num % 5 != 0) {
                queue.add(String.valueOf(num));
                current.incrementAndGet();
            }
        }
    }

    // Потік для виведення результатів
    public void printResults() {
        for (int i = 1; i <= n; i++) {
            try {
                System.out.print(queue.take() + " ");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public static void main(String[] args) {
        int n = 15; // або будь-яке інше число

        FizzBuzzMultithreaded fizzBuzz = new FizzBuzzMultithreaded(n);

        Thread threadA = new Thread(fizzBuzz::fizz);
        Thread threadB = new Thread(fizzBuzz::buzz);
        Thread threadC = new Thread(fizzBuzz::fizzbuzz);
        Thread threadD = new Thread(fizzBuzz::number);
        Thread printThread = new Thread(fizzBuzz::printResults);

        threadA.start();
        threadB.start();
        threadC.start();
        threadD.start();
        printThread.start();

        try {
            threadA.join();
            threadB.join();
            threadC.join();
            threadD.join();
            printThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}