package cassandra;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.util.concurrent.UncheckedExecutionException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Threads {
    public static <T, R> List<R> submitAndWait(final List<T> items, final Function<T, Future<R>> mapper) {
        return flush(toFutures(items, mapper));
    }

    public static <T, R> List<Future<R>> toFutures(final List<T> items, final Function<T, Future<R>> mapper) {
        return items.stream()
            .map(mapper)
            .collect(Collectors.toList());
    }

    public static <T> List<T> flush(final List<Future<T>> futures) {
        final var results = new ArrayList<T>();
        for (final var future : futures) {
            try {
                results.add(future.get());
            } catch (InterruptedException e) {
                throw new UncheckedInterruptedException(e);
            } catch (ExecutionException e) {
                throw new UncheckedExecutionException(e);
            }
        }
        return results;
    }


    @SuppressWarnings("PMD.DoNotUseThreads")
    public static ExecutorService newPool(int poolSize) {
        return Executors.newFixedThreadPool(poolSize, r -> {
            Thread t = Executors.defaultThreadFactory()
                .newThread(r);
            t.setDaemon(true);
            return t;
        });
    }

    public static <T> void rearrange(List<List<T>> recordList, int batchSize) {
        for (int i = batchSize; i < recordList.size(); i++) {
            final var consumerRecords = recordList.get(i);
            recordList
                .get(i % batchSize)
                .addAll(consumerRecords);
            consumerRecords.clear();
        }
        recordList.removeIf(List::isEmpty);
    }

    public static class UncheckedInterruptedException extends RuntimeException {
        public UncheckedInterruptedException(final InterruptedException cause) {
            super(cause);
        }
    }

}
