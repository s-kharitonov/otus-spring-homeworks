package ru.otus.loaders.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.otus.loaders.FileResourceLoader;

import java.io.IOException;
import java.io.InputStream;

@Component
public class BasicFileResourceLoader implements FileResourceLoader {

	private static final Logger logger = LoggerFactory.getLogger(BasicFileResourceLoader.class);

	private final String path;

	public BasicFileResourceLoader(@Value("${questions.file.path}")final String path) {
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
