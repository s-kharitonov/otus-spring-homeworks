package ru.otus.domain;

public class UserAnswer {

	private final QuestionDTO question;
	private final Answer answer;

	public UserAnswer(final QuestionDTO question, final Answer answer) {
		this.question = question;
		this.answer = answer;
	}

	public QuestionDTO getQuestion() {
		return question;
	}

	public Answer getAnswer() {
		return answer;
	}

	@Override
	public String toString() {
		return "UserAnswer{" +
				"question=" + question +
				", answer=" + answer +
				'}';
	}
}
