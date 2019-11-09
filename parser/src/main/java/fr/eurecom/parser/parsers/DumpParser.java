package fr.eurecom.parser.parsers;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DumpParser {

    private final int N_THREADS = 3;
    private final int BATCH = 20_000;
    private final String[] poison = {"poison"};
    private final Consumer<List<String[]>> consumer;

    public DumpParser(Consumer<List<String[]>> consumer) {
        this.consumer = consumer;
    }

    public void parse(String file) throws Exception {
        System.out.println("Parsing file: " + file);
        long start = System.currentTimeMillis();
        BlockingQueue<String[]> q = new ArrayBlockingQueue<>(100_000);

        Stream<String[]> stream = IntStream.rangeClosed(0, N_THREADS * (BATCH + 1)).mapToObj(i -> poison);

        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        executorService.submit(new LineProducer(file, q, stream));

        List<Future> consumers = IntStream.range(0, N_THREADS)
                .mapToObj(i -> executorService.submit(new LineConsumer(q, poison, BATCH, consumer)))
                .collect(Collectors.toList());

        for (Future f : consumers) {
            f.get();
        }

        executorService.shutdown();
        System.out.println("Total time: " + (System.currentTimeMillis() - start)/1000);
    }

}
