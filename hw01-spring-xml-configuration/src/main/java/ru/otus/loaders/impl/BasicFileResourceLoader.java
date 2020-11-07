package ru.otus.loaders.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import ru.otus.loaders.FileResourceLoader;

import java.io.IOException;
import java.io.InputStream;

public class BasicFileResourceLoader implements FileResourceLoader {

	private static final Logger logger = LoggerFactory.getLogger(BasicFileResourceLoader.class);

	private final String path;

	public BasicFileResourceLoader(final String path) {
		this.path = path;
	}

	@Override
	public InputStream loadResource() {
		try {
			return new ClassPathResource(path).getInputStream();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}
