package ru.otus;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.configs.AppConfig;
import ru.otus.runners.Runner;

public class Main {

	public static void main(String[] args) {
		final var context = new AnnotationConfigApplicationContext(AppConfig.class);
		final var runner = context.getBean(Runner.class);

		runner.run();
	}
}
