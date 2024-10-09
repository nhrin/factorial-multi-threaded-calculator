package org.example.factorial;

import java.io.*;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.logging.Logger;
import java.math.BigInteger;

public class FileWriterTask implements Runnable {
    private static final Logger logger = Logger.getLogger(FileWriterTask.class.getName());
    private final String outputFilePath;
    private final BlockingQueue<Entry<Integer, BigInteger>> resultQueue;
    private final ExecutorService executorService;

    public FileWriterTask(String outputFilePath, BlockingQueue<Entry<Integer, BigInteger>> resultQueue, ExecutorService executorService) {
        this.outputFilePath = outputFilePath;
        this.resultQueue = resultQueue;
        this.executorService = executorService;
    }

    @Override
    public void run() {
        logger.info("Writer Thread started.");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            while (true) {
                Entry<Integer, BigInteger> entry = resultQueue.poll(1, TimeUnit.SECONDS);
                if (entry != null) {
                    writer.write(entry.getKey() + " = " + entry.getValue());
                    writer.newLine();
                } else if (executorService.isTerminated() && resultQueue.isEmpty()) {
                    break;
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.severe("Error in Writer Thread: " + e.getMessage());
        }
        logger.info("Writer Thread finished.");
    }
}
