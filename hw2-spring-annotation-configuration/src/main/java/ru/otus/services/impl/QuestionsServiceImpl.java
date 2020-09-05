package ru.otus.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.dao.QuestionsDAO;
import ru.otus.domain.Question;
import ru.otus.services.QuestionsService;

import java.util.List;

public class QuestionsServiceImpl implements QuestionsService {

	private static final Logger logger = LoggerFactory.getLogger(QuestionsServiceImpl.class);

	private final QuestionsDAO questionsDAO;

	public QuestionsServiceImpl(final QuestionsDAO questionsDAO) {
		this.questionsDAO = questionsDAO;
	}

	@Override
	public List<Question> getQuestions() {
		final List<Question> questions = questionsDAO.findQuestions();

		if (questions.isEmpty()) {
			logger.error("questions not found!");
		}

		return questions;
	}
}
