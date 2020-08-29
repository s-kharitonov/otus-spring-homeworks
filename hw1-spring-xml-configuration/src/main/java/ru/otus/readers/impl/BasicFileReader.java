package ru.otus.readers.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.readers.FileReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class BasicFileReader implements FileReader {

	private static final Logger logger = LoggerFactory.getLogger(BasicFileReader.class);

	private final InputStream stream;

	public BasicFileReader(final InputStream stream) {
		this.stream = stream;
	}

	@Override
	public List<String> readLines() {
		try(final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
			return reader.lines().collect(Collectors.toList());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}
