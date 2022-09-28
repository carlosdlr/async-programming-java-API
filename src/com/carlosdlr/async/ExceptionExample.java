package com.carlosdlr.async;

import com.carlosdlr.async.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ExceptionExample {

    public static void main(String args []) {
        Supplier<List<Long>> supplyIDs = () -> {
            sleep(200);
            throw new IllegalStateException("No data");
            //return Arrays.asList(1L, 2L, 3L);
        };

        Function<List<Long>, List<User>> fetchUsers = ids -> {
            sleep(300);
            return ids.stream().map(User::new).collect(Collectors.toList());
        };


        Consumer<List<User>> displayer = users -> users.forEach(System.out::println);
        CompletableFuture<List<Long>> supply = CompletableFuture.supplyAsync(supplyIDs);
        CompletableFuture<List<Long>> exceptionally = supply.exceptionally(e -> List.of());

        CompletableFuture<List<User>> fetch = exceptionally.thenApply(fetchUsers);
        CompletableFuture<Void> display = fetch.thenAccept(displayer);

        sleep(1_000);

        System.out.println("Supply : done=" + supply.isDone()+
                "Exception=" + supply.isCompletedExceptionally());

        System.out.println("Fetch : done=" + fetch.isDone()+
                "Exception=" + fetch.isCompletedExceptionally());

        System.out.println("Display : done=" + display.isDone()+
                "Exception=" + display.isCompletedExceptionally());

    }

    private static void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
