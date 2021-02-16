package ru.otus.domain;

import java.util.Date;

public class ErrorResponse {
	private final int code;
	private final Date time;
	private final String message;

	public ErrorResponse(final int code, final String message) {
		this.code = code;
		this.time = new Date();
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public Date getTime() {
		return time;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "ErrorResponse{" +
				"code=" + code +
				", time=" + time +
				", message='" + message + '\'' +
				'}';
	}
}
