package ru.otus.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.dao.QuestionsDAO;
import ru.otus.dao.impl.QuestionsDAOCSV;
import ru.otus.loaders.FileResourceLoader;
import ru.otus.loaders.impl.BasicFileResourceLoader;
import ru.otus.runners.Runner;
import ru.otus.runners.impl.BasicRunner;
import ru.otus.services.QuestionsService;

@ComponentScan("ru.otus")
@PropertySource("classpath:application.properties")
@Configuration
public class AppConfig {

	@Bean
	public Runner runner(final QuestionsService questionsService) {
		return new BasicRunner(questionsService);
	}

	@Bean
	public FileResourceLoader fileResourceLoader(@Value("${questions.file.path}")final String path) {
		return new BasicFileResourceLoader(path);
	}

	@Bean
	public QuestionsDAO questionsDAO(final FileResourceLoader fileResourceLoader) {
		return new QuestionsDAOCSV(fileResourceLoader);
	}
}
