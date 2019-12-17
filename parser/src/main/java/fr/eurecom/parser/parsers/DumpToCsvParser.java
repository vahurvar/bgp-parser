package fr.eurecom.parser.parsers;

import fr.eurecom.parser.Utils;

import java.io.*;

public class DumpToCsvParser {

    public void parse(String file) throws Exception {
        long start = System.currentTimeMillis();
        System.out.println("Parsing file: " + file);

        Process proc = Runtime.getRuntime().exec(new String[]{"bgpdump", "-m" , file});
        BufferedWriter out = new BufferedWriter(new FileWriter(file + ".csv", true));
        try (BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split("\\|");
                if (Utils.isIpV4Address(split[8])) { // If next hop is IPV4
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
        String delimiter = ",";
        char quote = '\"';
        return lines[1] +
                delimiter +
                quote + lines[5] + quote +
                delimiter +
                quote + lines[6] + quote;
    }

}
