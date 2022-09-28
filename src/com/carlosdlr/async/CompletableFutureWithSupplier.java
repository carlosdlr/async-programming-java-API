package com.carlosdlr.async;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class CompletableFutureWithSupplier {

    public static void main(String args[]) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Supplier<String> supplier = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return Thread.currentThread().getName();
        };
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(supplier, executorService);
        //finish the task immediately
        //completableFuture.complete("Too long");

        String string = completableFuture.join();
        System.out.println("Result = "+ string);
        //force the completion of the completable future with the passed value
        completableFuture.obtrudeValue("Too long");

        string = completableFuture.join();
        System.out.println("Result = "+ string);
        executorService.shutdown();
    }
}
