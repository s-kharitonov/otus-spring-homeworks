package ru.otus.readers.impl;

import org.junit.jupiter.api.Test;
import ru.otus.loaders.impl.BasicFileResourceLoader;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BasicFileReaderPositiveTest {

	private final static String PATH_TO_TEST_FILE = "/questions.csv";

	@Test
	void shouldReturnNotEmptyList() {
		final InputStream stream = new BasicFileResourceLoader(PATH_TO_TEST_FILE).loadResource();
		assertNotEquals(null, new BasicFileReader(stream).readLines());
	}
}