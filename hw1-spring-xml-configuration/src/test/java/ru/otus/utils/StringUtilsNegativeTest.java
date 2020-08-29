package ru.otus.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StringUtilsNegativeTest {

	private static final String DELIMITER = ";";
	private static final String TO_SPLIT = "A;B";
	private static final int MIN_SIZE_LIST = 1;

	@Test
	void shouldThrownExceptionForEmptyParameters() {
		assertThrows(NullPointerException.class, () -> StringUtils.splitString(null, null));
	}

	@ParameterizedTest
	@NullSource
	void shouldThrownExceptionForEmptyToSplit(final String toSplit) {
		assertThrows(NullPointerException.class, () -> StringUtils.splitString(toSplit, DELIMITER));
	}

	@ParameterizedTest
	@NullSource
	void shouldThrownExceptionForEmptyDelimiter(final String delimiter) {
		assertThrows(NullPointerException.class, () -> StringUtils.splitString(TO_SPLIT, delimiter));
	}

	@ParameterizedTest
	@EmptySource
	void shouldReturnLengthEqualToSplitParameter(final String delimiter) {
		assertEquals(TO_SPLIT.length(), StringUtils.splitString(TO_SPLIT, delimiter).size());
	}

	@ParameterizedTest
	@EmptySource
	void shouldReturnListWithOneElement(final String toSplit) {
		assertEquals(MIN_SIZE_LIST, StringUtils.splitString(toSplit, DELIMITER).size());
	}
}