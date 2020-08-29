package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.services.QuestionsService;

import java.util.List;

public class Main {
	private final static String CONTEXT_PATH = "/spring-context.xml";
	private final static Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(CONTEXT_PATH);
		final QuestionsService questionsService = context.getBean(QuestionsService.class);
		final List<Question> questions = questionsService.getQuestions();

		logger.info(">>>>> ENGLISH LANGUAGE TEST <<<<<");
		questions.forEach((question) -> {
			final List<Answer> answers = question.getAnswers();
			logger.info("QUESTION: {}", question.getText());

			answers.forEach((answer -> logger.info("ANSWER: {}", answer.getText())));
		});
	}
}
