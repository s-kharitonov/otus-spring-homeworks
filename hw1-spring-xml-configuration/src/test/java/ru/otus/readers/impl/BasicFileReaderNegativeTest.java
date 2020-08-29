package ru.otus.readers.impl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BasicFileReaderNegativeTest {

	@ParameterizedTest
	@NullSource
	void shouldThrownExceptionForEmptyStream(final InputStream stream) {
		assertThrows(NullPointerException.class, () -> new BasicFileReader(stream).readLines());
	}
}