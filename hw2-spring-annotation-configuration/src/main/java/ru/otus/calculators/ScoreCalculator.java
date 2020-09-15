package ru.otus.calculators;

import ru.otus.domain.UserAnswer;

import java.util.List;

public interface ScoreCalculator {
	int calculate(List<UserAnswer> userAnswers);
}
