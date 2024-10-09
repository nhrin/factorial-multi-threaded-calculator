package org.example.factorial;

import java.math.BigInteger;
import java.util.concurrent.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class FactorialCalculator {
    private static final Logger logger = Logger.getLogger(FactorialCalculator.class.getName());
    private final ExecutorService executorService;
    private final BlockingQueue<Entry<Integer, BigInteger>> resultQueue;
    private final int poolSize;

    public FactorialCalculator(int poolSize) {
        this.poolSize = poolSize;
        this.executorService = Executors.newFixedThreadPool(poolSize);
        this.resultQueue = new LinkedBlockingQueue<>();
    }

    public void start() {
        logger.info("FactorialCalculator started with thread pool size: " + poolSize);
        new Thread(new FileReaderTask("input.txt", executorService, resultQueue)).start();
        new Thread(new FileWriterTask("output.txt", resultQueue, executorService)).start();
    }
}
