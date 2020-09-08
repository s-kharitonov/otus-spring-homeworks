package ru.otus.dao;

import ru.otus.domain.Question;

import java.util.List;
import java.util.Optional;

public interface QuestionsDAO {
	Optional<List<Question>> findQuestions();
}
