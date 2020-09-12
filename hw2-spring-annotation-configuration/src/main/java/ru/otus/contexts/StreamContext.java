package ru.otus.contexts;

import java.io.InputStream;
import java.io.PrintStream;

public interface StreamContext {
	InputStream getInputStream();

	PrintStream getPrintStream();
}
