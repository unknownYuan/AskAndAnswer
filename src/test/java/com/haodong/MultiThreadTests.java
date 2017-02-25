package com.haodong;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class Consumer implements Runnable {
    private BlockingQueue<String> blockingDeque;

    public Consumer(BlockingQueue<String> blockingDeque) {
        this.blockingDeque = blockingDeque;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(Thread.currentThread().getName() + ": " + blockingDeque.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Producer implements Runnable {
    private BlockingQueue<String> blockingDeque;

    public Producer(BlockingQueue<String> blockingDeque) {
        this.blockingDeque = blockingDeque;
    }

    @Override
    public void run() {


        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                blockingDeque.put(String.valueOf(i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class MultiThreadTests {
    public static void test() {
        BlockingQueue<String> bl = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(bl), "producer").start();
        new Thread(new Consumer(bl), "consumer1").start();
        new Thread(new Consumer(bl), "consumer2").start();
    }

    public static void main(String[] args) {
        test();
    }
}
