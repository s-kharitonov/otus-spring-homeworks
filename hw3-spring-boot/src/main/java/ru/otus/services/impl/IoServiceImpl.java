package ru.otus.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.contexts.StreamContext;
import ru.otus.exceptions.IOServiceException;
import ru.otus.services.IoService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

@Service
public class IoServiceImpl implements IoService {

	private static final Logger logger = LoggerFactory.getLogger(IoServiceImpl.class);

	private final BufferedReader reader;
	private final PrintStream writer;

	public IoServiceImpl(final StreamContext streamContext) {
		this.reader = new BufferedReader(
				new InputStreamReader(streamContext.getInputStream(), StandardCharsets.UTF_8)
		);
		this.writer = streamContext.getPrintStream();
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
