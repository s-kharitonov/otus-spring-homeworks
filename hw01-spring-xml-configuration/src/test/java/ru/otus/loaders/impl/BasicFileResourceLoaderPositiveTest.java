package ru.otus.loaders.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BasicFileResourceLoaderPositiveTest {

	private final static String PATH_TO_TEST_FILE = "/questions.csv";

	@Test
	void shouldReturnNotEmptyInputStream() {
		assertNotEquals(null, new BasicFileResourceLoader(PATH_TO_TEST_FILE).loadResource());
	}
}