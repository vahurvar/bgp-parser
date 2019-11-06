package fr.eurecom.parser;

import fr.eurecom.parser.parsers.PrefixParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@SpringBootApplication
@EnableJpaRepositories
public class ParserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParserApplication.class, args);
	}

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

	@Component
	public class AppStartupRunner implements ApplicationRunner {

		private final BgpDao bgpDao;

		public AppStartupRunner(BgpDao bgpDao) {
			this.bgpDao = bgpDao;
		}

		@Override
		public void run(ApplicationArguments args) throws Exception {
			PrefixParser parser = new PrefixParser(bgpDao);
			for (String arg : args.getSourceArgs()) {
				parser.parse(arg);
			}

			parser.parse("/Users/vahur.varris/Downloads/semester-project/postgres/bview.20191101.1600.gz");
		}
	}

}
