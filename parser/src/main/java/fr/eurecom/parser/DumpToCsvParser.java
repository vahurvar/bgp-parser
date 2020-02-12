package fr.eurecom.parser;

import java.io.*;

import static fr.eurecom.parser.Utils.isIpV4Address;

public class DumpToCsvParser {

    private static final String DELIMITER = ",";
    private static final char QUOTE = '\"';

    public void parse(String file) throws Exception {
        long start = System.currentTimeMillis();
        System.out.println("Parsing file: " + file);

        Process proc = Runtime.getRuntime().exec(new String[]{"bgpdump", "-m" , file});
        BufferedWriter out = new BufferedWriter(new FileWriter(file + ".csv", true));
        try (BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split("\\|");
                if (isIpV4Address(split[8])) { // If next hop is IPV4
                    out.write(getLine(split));
                    out.newLine();
                }
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Total time: " + (System.currentTimeMillis() - start)/1000);
    }

    private String getLine(String[] lines) {
        return lines[1] +
                DELIMITER +
                QUOTE + lines[5] + QUOTE +
                DELIMITER +
                QUOTE + lines[6] + QUOTE;
    }

}
