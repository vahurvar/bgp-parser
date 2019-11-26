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

	/*
		@Bean
	public DataSource dataSource(
			@Value("${database.user}") String user,
			@Value("${database.password}") String password,
			@Value("${database.host}") String host,
			@Value("${database.port}") String port
	) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://" + host + ":" + port + "/bgp");
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		return dataSource;
	}
	 */

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
