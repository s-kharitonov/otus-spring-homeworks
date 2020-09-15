package ru.otus;

import org.springframework.context.annotation.*;
import ru.otus.runners.Runner;

@ComponentScan
@PropertySource("classpath:application.properties")
@EnableAspectJAutoProxy
@Configuration
public class Main {

	public static void main(String[] args) {
		final var context = new AnnotationConfigApplicationContext(Main.class);
		final var runner = context.getBean(Runner.class);

		runner.run();
	}
}
