package fr.eurecom.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class LineConsumer implements Runnable {

    private final BlockingQueue<String[]> q;
    private final String[] poison;
    private final PrefixDao dao;
    private final int batch_size;
    private final String file;

    public LineConsumer(BlockingQueue<String[]> q, String[] poison, PrefixDao dao, int batch_size, String file) {
        this.q = q;
        this.poison = poison;
        this.dao = dao;
        this.batch_size = batch_size;
        this.file = file;
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

                        dao.insertAll(collect, file);
                        prefixes.clear();
                        break;
                    } else {
                        dao.insertAll(prefixes, file);
                        prefixes.clear();
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (!prefixes.isEmpty()) {
            dao.insertAll(prefixes, file);
        }

        System.out.println("Consumer out");
    }
}
