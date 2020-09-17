package ru.otus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import ru.otus.services.TestService;

@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		final var context = SpringApplication.run(Main.class, args);
		final var testService = context.getBean(TestService.class);

		testService.test();
	}
}
