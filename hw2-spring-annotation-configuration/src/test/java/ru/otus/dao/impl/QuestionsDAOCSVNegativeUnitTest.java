package ru.otus.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.loaders.FileResourceLoader;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class QuestionsDAOCSVNegativeUnitTest {

	private FileResourceLoader resourceLoader;

	@BeforeEach
	public void beforeEach() {
		resourceLoader = mock(FileResourceLoader.class);
	}

	@Test
	@DisplayName("should throw IllegalArgumentException when input stream is null")
	public void shouldThrownExceptionWhenResourceIsNull() {
		given(resourceLoader.loadResource()).willReturn(null);
		assertThrows(IllegalArgumentException.class, () -> new QuestionsDAOCSV(resourceLoader).findQuestions());
	}
}