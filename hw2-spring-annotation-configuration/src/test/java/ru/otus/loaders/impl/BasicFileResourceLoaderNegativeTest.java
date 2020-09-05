package ru.otus.loaders.impl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BasicFileResourceLoaderNegativeTest {

	@Test
	void shouldThrownExceptionForEmptyPath() {
		assertThrows(IllegalArgumentException.class, () -> new BasicFileResourceLoader(null).loadResource());
	}
}