package ru.otus.loaders.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BasicFileResourceLoaderNegativeUnitTest {

	@Test
	@DisplayName("should thrown IllegalArgumentException for empty path")
	void shouldThrownExceptionForEmptyPath() {
		assertThrows(IllegalArgumentException.class, () -> new BasicFileResourceLoader(null).loadResource());
	}
}