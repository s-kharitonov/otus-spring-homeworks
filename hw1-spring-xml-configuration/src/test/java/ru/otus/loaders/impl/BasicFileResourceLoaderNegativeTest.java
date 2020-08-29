package ru.otus.loaders.impl;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BasicFileResourceLoaderNegativeTest {

	@ParameterizedTest
	@NullSource
	void shouldThrownExceptionForEmptyPath(final String path) {
		assertThrows(IllegalArgumentException.class, () -> new BasicFileResourceLoader(path).loadResource());
	}
}