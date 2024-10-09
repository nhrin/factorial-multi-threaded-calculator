package org.example.factorial;

import java.io.*;
import java.math.BigInteger;
import java.util.concurrent.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class FileReaderTask implements Runnable {
    private static final Logger logger = Logger.getLogger(FileReaderTask.class.getName());
    private final String inputFilePath;
    private final ExecutorService executorService;
    private final BlockingQueue<Entry<Integer, BigInteger>> resultQueue;

    public FileReaderTask(String inputFilePath, ExecutorService executorService, BlockingQueue<Entry<Integer, BigInteger>> resultQueue) {
        this.inputFilePath = inputFilePath;
        this.executorService = executorService;
        this.resultQueue = resultQueue;
    }

    @Override
    public void run() {
        logger.info("Reader Thread started.");
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    try {
                        int number = Integer.parseInt(line);
                        executorService.submit(new FactorialTask(number, resultQueue));
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid number format in input: " + line);
                    }
                }
            }
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.DAYS);
        } catch (IOException | InterruptedException e) {
            logger.severe("Error in Reader Thread: " + e.getMessage());
        }
        logger.info("Reader Thread finished.");
    }
}
