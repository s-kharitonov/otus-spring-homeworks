package ru.otus.utils;

import java.util.Arrays;
import java.util.List;

public final class StringUtils {

	public static List<String> splitString(final String toSplit, final String delimiter) {
		return Arrays.asList(toSplit.split(delimiter));
	}
}
