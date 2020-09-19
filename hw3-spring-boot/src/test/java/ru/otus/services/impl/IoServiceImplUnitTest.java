package ru.otus.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.contexts.StreamContext;
import ru.otus.services.IoService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class IoServiceImplUnitTest {

	private static final String LINE_SEPARATOR = System.lineSeparator();
	private static final String TEST_MESSAGE = "hello world!";

	private ByteArrayOutputStream arrayOutputStream;
	private IoService ioService;

	@BeforeEach
	void setUp() {
		final StreamContext context = mock(StreamContext.class);

		arrayOutputStream = new ByteArrayOutputStream();

		given(context.getPrintStream()).willReturn(new PrintStream(arrayOutputStream));
		given(context.getInputStream()).willReturn(System.in);

		ioService = new IoServiceImpl(context);
	}

	@Test
	@DisplayName("should print message: " + TEST_MESSAGE)
	public void shouldPrintMessage() throws InterruptedException {
		ioService.writeMessage(TEST_MESSAGE);
		Thread.sleep(1000);
		assertThat(arrayOutputStream.toString()).isEqualTo(TEST_MESSAGE + LINE_SEPARATOR);
	}
	
	@Test
	@DisplayName("should print message \"null\" with null param")
	public void shouldPrintMessageWithNullParam() throws InterruptedException {
		ioService.writeMessage(null);
		Thread.sleep(1000);
		assertThat(arrayOutputStream.toString()).isEqualTo("null" + LINE_SEPARATOR);
	}
}