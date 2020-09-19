package ru.otus.services.facades.impl;

import org.springframework.stereotype.Service;
import ru.otus.services.IoService;
import ru.otus.services.LocalizationService;
import ru.otus.services.facades.IoFacade;

@Service
public class IoFacadeImpl implements IoFacade {

	private final LocalizationService localizationService;
	private final IoService ioService;

	public IoFacadeImpl(final LocalizationService localizationService, final IoService ioService) {
		this.localizationService = localizationService;
		this.ioService = ioService;
	}

	@Override
	public void writeMessage(final String key, final Object... args) {
		ioService.writeMessage(localizationService.localizeMessage(key, args));
	}
}
