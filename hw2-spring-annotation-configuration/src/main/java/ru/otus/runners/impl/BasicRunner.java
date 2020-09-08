package ru.otus.runners.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.runners.Runner;
import ru.otus.services.QuestionsService;

import java.util.List;

@Component
public class BasicRunner implements Runner {

	private final static Logger logger = LoggerFactory.getLogger(BasicRunner.class);

	private final QuestionsService questionsService;

	public BasicRunner(final QuestionsService questionsService) {
		this.questionsService = questionsService;
	}

	@Override
	public void run() {
		final List<Question> questions = questionsService.getQuestions();

		logger.info(">>>>> ENGLISH LANGUAGE TEST <<<<<");
		questions.forEach((question) -> {
			final List<Answer> answers = question.getAnswers();
			logger.info("QUESTION: {}", question.getText());

			answers.forEach((answer -> logger.info("ANSWER: {}", answer.getText())));
		});
	}
}
