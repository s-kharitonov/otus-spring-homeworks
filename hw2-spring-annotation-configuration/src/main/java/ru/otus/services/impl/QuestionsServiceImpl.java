package ru.otus.services.impl;

import org.springframework.stereotype.Service;
import ru.otus.dao.QuestionsDAO;
import ru.otus.domain.Question;
import ru.otus.services.QuestionsService;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionsServiceImpl implements QuestionsService {

	private final QuestionsDAO questionsDAO;

	public QuestionsServiceImpl(final QuestionsDAO questionsDAO) {
		this.questionsDAO = questionsDAO;
	}

	@Override
	public List<Question> getQuestions() {
		return questionsDAO
				.findQuestions()
				.orElse(new ArrayList<>());
	}
}
