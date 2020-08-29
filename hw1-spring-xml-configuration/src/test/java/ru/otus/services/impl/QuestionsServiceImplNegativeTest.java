package ru.otus.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mockito;
import ru.otus.loaders.FileResourceLoader;
import ru.otus.services.QuestionsService;

import static org.junit.jupiter.api.Assertions.assertThrows;

class QuestionsServiceImplNegativeTest {

	private FileResourceLoader fileResourceLoader;
	private QuestionsService questionsService;

	@BeforeEach
	void beforeEach() {
		fileResourceLoader = Mockito.mock(FileResourceLoader.class);
		questionsService = new QuestionsServiceImpl(fileResourceLoader);
	}

	@ParameterizedTest
	@NullSource
	void shouldThrownExceptionForEmptyLoader(final FileResourceLoader loader) {
		assertThrows(NullPointerException.class, () -> new QuestionsServiceImpl(loader).getQuestions());
	}

	@Test
	void shouldThrownExceptionWhenResourceIsEmpty() {
		Mockito.when(fileResourceLoader.loadResource()).thenReturn(null);
		assertThrows(IllegalArgumentException.class, () -> questionsService.getQuestions());
	}
}