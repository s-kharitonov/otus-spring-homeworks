package ru.otus.contexts.impl;

import org.springframework.stereotype.Component;
import ru.otus.contexts.StreamContext;

import java.io.InputStream;
import java.io.PrintStream;

@Component
public class BasicStreamContext implements StreamContext {

	private final InputStream inputStream = System.in;
	private final PrintStream printStream = System.out;

	@Override
	public InputStream getInputStream() {
		return inputStream;
	}

	@Override
	public PrintStream getPrintStream() {
		return printStream;
	}
}
