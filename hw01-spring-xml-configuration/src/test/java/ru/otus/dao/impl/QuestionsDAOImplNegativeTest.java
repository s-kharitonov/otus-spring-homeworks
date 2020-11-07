package ru.otus.dao.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.loaders.FileResourceLoader;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class QuestionsDAOImplNegativeTest {

	private FileResourceLoader resourceLoader;

	@BeforeEach
	public void beforeEach() {
		resourceLoader = mock(FileResourceLoader.class);
	}

	@Test
	public void shouldThrownExceptionWhenResourceIsNull() {
		when(resourceLoader.loadResource()).thenReturn(null);
		assertThrows(IllegalArgumentException.class, () -> new QuestionsDAOImpl(resourceLoader).findQuestions());
	}
}