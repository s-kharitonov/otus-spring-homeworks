package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.otus.configs.AppProperties;
import ru.otus.services.TestService;

@EnableAspectJAutoProxy
@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		final var context = SpringApplication.run(Main.class, args);
		final var testService = context.getBean(TestService.class);

		testService.test();
	}
}
