package com.carlosdlr.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FirstCompletableFutures {
    public static void main(String args []) throws InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Runnable task = () -> System.out.println("running asynchronously in the thread " + Thread.currentThread().getName());
        CompletableFuture.runAsync(task, executorService);
        executorService.shutdown();
    }
}
