package ru.otus.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringUtilsPositiveTest {

	private static final String DELIMITER = ";";
	private static final String TO_SPLIT = "A;B";

	@Test
	void shouldReturnListWithTwoElements() {
		assertEquals(2, StringUtils.splitString(TO_SPLIT, DELIMITER).size());
	}
}