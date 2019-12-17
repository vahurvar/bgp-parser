package fr.eurecom.parser;

import fr.eurecom.parser.parsers.DumpToCsvParser;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class ParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParserApplication.class, args);
	}

	@Component
	public class AppStartupRunner implements ApplicationRunner {

		@Override
		public void run(ApplicationArguments args) throws Exception {
			DumpToCsvParser parser = new DumpToCsvParser();

			for (String arg : args.getSourceArgs()) {
				parser.parse(arg);
				Runtime.getRuntime().exec("rm " + arg);
			}
		}
	}

}
