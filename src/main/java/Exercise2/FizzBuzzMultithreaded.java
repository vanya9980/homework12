package Exercise2;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
public class FizzBuzzMultithreaded {
    private final int n;
    private final AtomicInteger current;
    private final BlockingQueue<String> queue;
    private final Object lock = new Object();

    public FizzBuzzMultithreaded(int n) {
        this.n = n;
        this.current = new AtomicInteger(1);
        this.queue = new LinkedBlockingQueue<>();
    }

    public void fizz() {
        synchronized (lock) {
            while (current.get() <= n) {
                while (current.get() % 3 != 0 || current.get() % 5 == 0) {
                    try {
                        lock.wait();
                    }
                    catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                }
                if (current.get() > n) {
                    break;
                }
                queue.add("fizz");
                current.incrementAndGet();
                lock.notifyAll();
            }
        }
    }

    public void buzz() {
        synchronized (lock) {
            while (current.get() <= n) {
                while (current.get() % 5 != 0 || current.get() % 3 == 0) {
                    try {
                        lock.wait();
                    }
                    catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                }
                if (current.get() > n) {
                    break;
                }
                queue.add("buzz");
                current.incrementAndGet();
                lock.notifyAll();
            }
        }
    }

    public void fizzbuzz() {
        synchronized (lock) {
            while (current.get() <= n) {
                while (current.get() % 3 != 0 || current.get() % 5 != 0) {
                    try {
                        lock.wait();
                    }
                    catch (InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                }
                if (current.get() > n) {
                    break;
                }
                queue.add("fizzbuzz");
                current.incrementAndGet();
                lock.notifyAll();
            }
        }
    }

    public void number() {
        synchronized (lock) {
            while (current.get() <= n) {
                while ((current.get() % 3 == 0 || current.get() % 5 == 0) && queue.isEmpty()) {
                    try {
                        lock.wait();
                    }
                    catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                if(current.get() > n && queue.isEmpty()) break;
                String item = queue.poll();
                if(item == null) {
                    System.out.println(current.get() + ", ");
                    current.incrementAndGet();
                } else {
                    System.out.println(item + ", ");
                }
                lock.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        FizzBuzzMultithreaded fizzBuzz = new FizzBuzzMultithreaded(15);

        Thread threadA = new Thread(fizzBuzz::fizz);
        Thread threadB = new Thread(fizzBuzz::buzz);
        Thread threadC = new Thread(fizzBuzz::fizzbuzz);
        Thread threadD = new Thread(fizzBuzz::number);

        threadA.start();
        threadB.start();
        threadC.start();
        threadD.start();

        try {
            threadA.join();
            threadB.join();
            threadC.join();
            threadD.join();
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}