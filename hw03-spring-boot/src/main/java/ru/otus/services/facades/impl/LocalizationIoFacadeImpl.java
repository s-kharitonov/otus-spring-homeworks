package ru.otus.services.facades.impl;

import org.springframework.stereotype.Service;
import ru.otus.services.IoService;
import ru.otus.services.LocalizationService;
import ru.otus.services.facades.LocalizationIoFacade;

@Service
public class LocalizationIoFacadeImpl implements LocalizationIoFacade {

	private final LocalizationService localizationService;
	private final IoService ioService;

	public LocalizationIoFacadeImpl(final LocalizationService localizationService,
									final IoService ioService) {
		this.localizationService = localizationService;
		this.ioService = ioService;
	}

	@Override
	public void writeMessageFromProps(final String key, final Object... args) {
		ioService.writeMessage(localizationService.localizeMessage(key, args));
	}

	@Override
	public void writeMessage(final String message) {
		ioService.writeMessage(message);
	}

	@Override
	public String readLine() {
		return ioService.readLine();
	}
}
