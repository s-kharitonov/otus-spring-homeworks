package ru.otus.domain;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import ru.otus.converters.TextToAnswerConverter;

import java.util.ArrayList;
import java.util.List;

public class Question {

	@CsvBindByName(column = "Question")
	private String text;

	@CsvBindAndSplitByName(column = "Answers",
			splitOn = "\\|",
			converter = TextToAnswerConverter.class,
			collectionType = ArrayList.class,
			elementType = Answer.class)
	private List<Answer> answers;

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

	@Override
	public String toString() {
		return "Question{" +
				"text='" + text + '\'' +
				", answers=" + answers +
				'}';
	}
}
