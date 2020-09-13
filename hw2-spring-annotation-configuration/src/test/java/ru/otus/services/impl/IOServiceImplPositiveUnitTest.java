package ru.otus.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.contexts.StreamContext;
import ru.otus.services.IOService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class IOServiceImplPositiveUnitTest {

	private static final String LINE_SEPARATOR = System.lineSeparator();
	private static final String TEST_MESSAGE = "hello world!";

	private ByteArrayOutputStream arrayOutputStream;
	private IOService ioService;

	@BeforeEach
	void setUp() {
		final StreamContext context = mock(StreamContext.class);

		arrayOutputStream = new ByteArrayOutputStream();

		given(context.getPrintStream()).willReturn(new PrintStream(arrayOutputStream));
		given(context.getInputStream()).willReturn(System.in);

		ioService = new IOServiceImpl(context);
	}

	@Test
	@DisplayName("should print message: " + TEST_MESSAGE)
	public void shouldPrintMessage() throws InterruptedException {
		ioService.writeMessage(TEST_MESSAGE);
		Thread.sleep(1000);
		assertThat(arrayOutputStream.toString()).isEqualTo(TEST_MESSAGE + LINE_SEPARATOR);
	}
}