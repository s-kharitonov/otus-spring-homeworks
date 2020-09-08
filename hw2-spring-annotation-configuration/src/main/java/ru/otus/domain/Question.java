package ru.otus.domain;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import ru.otus.converters.TextToAnswerConverter;

import java.util.ArrayList;
import java.util.List;

public class Question {

	@CsvBindByName(column = "QuestionText", required = true)
	private String text;

	@CsvBindAndSplitByName(column = "Answers",
			required = true,
			splitOn = "\\|",
			converter = TextToAnswerConverter.class,
			collectionType = ArrayList.class,
			elementType = Answer.class)
	private List<Answer> answers;

	@CsvBindByName(column = "CorrectAnswer", required = true)
	private int correctAnswer;

	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(final List<Answer> answers) {
		this.answers = answers;
	}

	public int getCorrectAnswer() {
		return correctAnswer;
	}

	public void setCorrectAnswer(final int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	@Override
	public String toString() {
		return "Question{" +
				"text='" + text + '\'' +
				", answers=" + answers +
				", correctAnswer=" + correctAnswer +
				'}';
	}
}
