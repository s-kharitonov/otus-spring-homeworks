package ru.otus.domain;

import java.util.Date;

public class ErrorResponse {
	private final int status;
	private final Date time;
	private final String message;

	public ErrorResponse(final int status, final String message) {
		this.status = status;
		this.time = new Date();
		this.message = message;
	}

	public int getStatus() {
		return status;
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
				"status=" + status +
				", time=" + time +
				", message='" + message + '\'' +
				'}';
	}
}
