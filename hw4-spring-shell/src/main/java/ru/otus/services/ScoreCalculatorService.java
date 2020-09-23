package ru.otus.services;

import ru.otus.domain.UserAnswer;

import java.util.List;

public interface ScoreCalculatorService {
	int calculate(List<UserAnswer> userAnswers);
}
