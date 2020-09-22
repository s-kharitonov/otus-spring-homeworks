package ru.otus.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.exceptions.IOServiceException;
import ru.otus.services.IoService;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Service
public class IoServiceImpl implements IoService {

	private static final Logger logger = LoggerFactory.getLogger(IoServiceImpl.class);

	private final BufferedReader reader;
	private final PrintStream writer;

	public IoServiceImpl(@Value("#{T(java.lang.System).in}") final InputStream inputStream,
						 @Value("#{T(java.lang.System).out}") final PrintStream printStream) {
		this.reader = new BufferedReader(
				new InputStreamReader(inputStream, StandardCharsets.UTF_8)
		);
		this.writer = printStream;
	}

	@Override
	public void writeMessage(final String message) {
		writer.println(message);
	}

	@Override
	public String readLine() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new IOServiceException("stream read error!", e);
		}
	}
}
