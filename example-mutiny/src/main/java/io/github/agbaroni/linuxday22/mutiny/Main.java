package io.github.agbaroni.linuxday22.mutiny;

import io.smallrye.mutiny.Uni;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Main {
    private static int k = 0;

    private static String mark(String s) { return String.format("%d %s", ++k, s); }

    private static <T, R> Uni<R> genericComputation(T t, long n, Function<T, R> f) {
	return Uni.createFrom().completionStage(CompletableFuture.supplyAsync(() -> {
		    var s = System.currentTimeMillis();
		    var k = n * 1000L;

		    while (System.currentTimeMillis() - s <= k);

		    return f.apply(t);
		}));
    }

    private static Uni<String> getMessage(String initial, long n) {
	return genericComputation(initial, n, s -> s);
    }

    private static Uni<String> handleMessage(String text, long n) {
	return genericComputation(mark(text), n, String::toUpperCase);
    }

    public static void main(String... args) {
	System.console().writer().println(
	    Uni.combine().all().unis(getMessage("ciao", 3)
				         .onItem()
					 .transform(s1 -> handleMessage(s1, 1)
						              .await()
							      .indefinitely()),
				     getMessage("mondo", 1)
				         .onItem()
					 .transform(s1 -> handleMessage(s1, 2)
							     .await()
							     .indefinitely()))
	        .asTuple().await().indefinitely()
        );
    }
}
