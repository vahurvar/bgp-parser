package fr.eurecom.parser.parsers;

import fr.eurecom.parser.BgpDao;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PrefixParser {

    private final BgpDao bgpDao;

    private final int N_THREADS = 3;
    private final int BATCH = 20_000;
    private final String[] poison = {"poison"};

    public PrefixParser(BgpDao bgpDao) {
        this.bgpDao = bgpDao;
    }

    public void parse(String file) throws Exception {
        System.out.println("Parsing file: " + file);
        long start = System.currentTimeMillis();
        BlockingQueue<String[]> q = new ArrayBlockingQueue<>(100_000);

        Stream<String[]> stream = IntStream.rangeClosed(0, N_THREADS * (BATCH + 1)).mapToObj(i -> poison);

        ExecutorService executorService = Executors.newFixedThreadPool(N_THREADS);
        executorService.submit(new LineProducer(file, q, stream));

        Consumer<List<String[]>> consumerAction = bgpDao::insert;

        List<Future> consumers = IntStream.range(0, N_THREADS)
                .mapToObj(i -> executorService.submit(new LineConsumer(q, poison, BATCH, consumerAction)))
                .collect(Collectors.toList());

        for (Future f : consumers) {
            f.get();
        }

        executorService.shutdown();
        System.out.println("Total time: " + (System.currentTimeMillis() - start)/1000);
    }

}
