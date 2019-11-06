package fr.eurecom.parser.parsers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LineConsumer implements Runnable {

    private final BlockingQueue<String[]> q;
    private final String[] poison;
    private final int batch_size;
    private final Consumer<List<String[]>> consumerAction;

    public LineConsumer(BlockingQueue<String[]> q, String[] poison, int batch_size, Consumer<List<String[]>> consumerAction) {
        this.q = q;
        this.poison = poison;
        this.batch_size = batch_size;
        this.consumerAction = consumerAction;
    }

    @Override
    public void run() {
        List<String[]> prefixes = new ArrayList<>();

        while (true) {
            try {
                int i = q.drainTo(prefixes, batch_size);

                if (i == 0) {
                    String[] take = q.take();
                    if (take == poison) {
                        break;
                    } else {
                        prefixes.add(take);
                    }
                } else {
                    if (prefixes.contains(poison)) {
                        List<String[]> collect = prefixes.stream()
                                .filter(prefix -> prefix != poison)
                                .collect(Collectors.toList());

                        consumerAction.accept(collect);
                        prefixes.clear();
                        break;
                    } else {
                        consumerAction.accept(prefixes);
                        prefixes.clear();
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!prefixes.isEmpty()) {
            consumerAction.accept(prefixes);
        }

        System.out.println("Consumer out");
    }
}
