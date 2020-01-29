package fr.eurecom.parser;

import java.util.Date;

public class ParserApplication {

	public static void main(String[] args) throws Exception {
		System.out.println("Parser started: " + new Date());
		DumpToCsvParser parser = new DumpToCsvParser();

		for (String arg : args) {
			parser.parse(arg);
			Runtime.getRuntime().exec("rm " + arg);
		}
		System.out.println("Parser done: " + new Date());
	}

}
