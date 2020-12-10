package es.alba.sweet.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AMessage {

	private final static DateTimeFormatter	FORMATTER	= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private LocalDateTime					dateTime;
	private String							method;
	private String							message;
	private MessageType						type		= MessageType.INFO;

	public AMessage(MessageType type, String method, String message) {
		this.dateTime = LocalDateTime.now();
		this.method = method;
		this.message = message;
		this.type = type;
	}

	public String getDateTime() {
		return dateTime.format(FORMATTER);
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageType getType() {
		return type;
	}

}