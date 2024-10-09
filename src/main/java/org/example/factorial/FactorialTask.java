package org.example.factorial;

import java.math.BigInteger;
import java.util.concurrent.*;
import java.util.Map;
import java.util.AbstractMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class FactorialTask implements Runnable {
    private static final Logger logger = Logger.getLogger(FactorialTask.class.getName());
    private static final int MAX_NUMBERS_PER_SECOND = 100;
    private static final AtomicInteger count = new AtomicInteger(0);

    private final int number;
    private final BlockingQueue<Map.Entry<Integer, BigInteger>> resultQueue;

    public FactorialTask(int number, BlockingQueue<Map.Entry<Integer, BigInteger>> resultQueue) {
        this.number = number;
        this.resultQueue = resultQueue;
    }

    @Override
    public void run() {
        try {
            throttle();
            BigInteger factorialResult = factorial(number);
            resultQueue.put(new AbstractMap.SimpleEntry<>(number, factorialResult));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.severe("Thread interrupted for number: " + number);
        }
    }

    private BigInteger factorial(int number) {
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= number; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    private static void throttle() throws InterruptedException {
        if (count.incrementAndGet() >= MAX_NUMBERS_PER_SECOND) {
            Thread.sleep(1000);
            count.set(0);
        }
    }
}
