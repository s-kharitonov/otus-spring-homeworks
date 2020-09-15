package ru.otus.services.impl;

import org.springframework.stereotype.Service;
import ru.otus.dao.QuestionsDao;
import ru.otus.domain.QuestionDTO;
import ru.otus.services.QuestionsService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionsServiceImpl implements QuestionsService {

	private final QuestionsDao questionsDao;

	public QuestionsServiceImpl(final QuestionsDao questionsDao) {
		this.questionsDao = questionsDao;
	}

	@Override
	public List<QuestionDTO> getQuestions() {
		return questionsDao
				.findQuestions()
				.stream()
				.map(QuestionDTO::new)
				.collect(Collectors.toList());
	}
}
