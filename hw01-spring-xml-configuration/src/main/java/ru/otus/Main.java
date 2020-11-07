package ru.otus;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.runners.Runner;

public class Main {
	private final static String CONTEXT_PATH = "/spring-context.xml";

	public static void main(String[] args) {
		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_PATH);
		final Runner runner = context.getBean(Runner.class);

		runner.run();
	}
}
