package com.carlosdlr.async;

import com.carlosdlr.async.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AsyncExample {

    public static void main(String args[]) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        ExecutorService executorService2 = Executors.newSingleThreadExecutor();

        Supplier<List<Long>> supplyIDs = () -> {
            sleep(200);
            return Arrays.asList(1L, 2L, 3L);
        };

        Function<List<Long>, CompletableFuture<List<User>>> fetchUsers = ids -> {
          sleep(300);
          Supplier<List<User>> usersSupplier = () -> {
              System.out.println("Currently Running "+ Thread.currentThread().getName());
              return ids.stream().map(User::new).collect(Collectors.toList());
          };
          return CompletableFuture.supplyAsync(usersSupplier, executorService2);
        };

        Consumer<List<User>> displayer = users -> {
            System.out.println("Running in "+ Thread.currentThread().getName());
            users.forEach(System.out::println);
        };

        CompletableFuture<List<Long>> listCompletableFuture = CompletableFuture.supplyAsync(supplyIDs);
        listCompletableFuture.thenCompose(fetchUsers)
                .thenAcceptAsync(displayer, executorService);

        sleep(1_000);
        executorService.shutdown();
    }

    private static void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
