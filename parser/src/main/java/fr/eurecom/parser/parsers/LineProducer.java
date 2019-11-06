package fr.eurecom.parser.parsers;

import fr.eurecom.parser.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

public class LineProducer implements Runnable {

    private final BlockingQueue<String[]> q;
    private final Stream<String[]> poisons;
    private final String file;


    public LineProducer(String file, BlockingQueue<String[]> q, Stream<String[]> poisons) {
        this.q = q;
        this.file = file;
        this.poisons = poisons;
    }

    @Override
    public void run() {
        try {
            Process proc = Runtime.getRuntime().exec(new String[]{"bgpdump", "-m", "-t", "change" , file});
            try (BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] split = line.split("\\|");
                    if (Utils.isIpV4Address(split[8])) { // If next hop is IPV4
                        q.put(split);
                    }
                }
                poisons.forEach(this::sendToQueue);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("Producer out");
    }

    private void sendToQueue(String[] e) {
        try {
            q.put(e);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

}