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

    public void fizz() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (current.get() > n) {
                    break;
                }
                if (current.get() % 3 == 0 && current.get() % 5 != 0) {
                    queue.put("fizz");
                    current.incrementAndGet();
                }
            }
        }
    }

    public void buzz() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (current.get() > n) {
                    break;
                }
                if (current.get() % 5 == 0 && current.get() % 3 != 0) {
                    queue.put("buzz");
                    current.incrementAndGet();
                }
            }
        }
    }

    public void fizzbuzz() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (current.get() > n) {
                    break;
                }
                if (current.get() % 3 == 0 && current.get() % 5 == 0) {
                    queue.put("fizzbuzz");
                    current.incrementAndGet();
                }
            }
        }
    }

    public void number() throws InterruptedException {
        while (true) {
            synchronized (this) {
                if (current.get() > n) {
                    queue.put("stop"); // Signals the end of processing
                    break;
                }
                if (current.get() % 3 != 0 && current.get() % 5 != 0) {
                    queue.put(String.valueOf(current));
                    current.incrementAndGet();
                }
            }
        }
    }

    public void print() throws InterruptedException {
        while (true) {
            String value = queue.take();
            if (value.equals("stop")) {
                break;
            }
            System.out.print(value + ", ");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        FizzBuzzMultithreaded fizzBuzz = new FizzBuzzMultithreaded(15);

        Thread threadA = new Thread(() -> {
            try {
                fizzBuzz.fizz();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread threadB = new Thread(() -> {
            try {
                fizzBuzz.buzz();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread threadC = new Thread(() -> {
            try {
                fizzBuzz.fizzbuzz();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread threadD = new Thread(() -> {
            try {
                fizzBuzz.number();
                fizzBuzz.print();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        threadA.start();
        threadB.start();
        threadC.start();
        threadD.start();

        threadA.join();
        threadB.join();
        threadC.join();
        threadD.join();
    }
}