package es.alba.sweet.core;

public class Message extends AMessage {

	public Message(MessageType type, String method, String message) {
		super(type, method, message);
	}

	@Override
	public String toString() {
		return getDateTime() + " - " + getType() + " - " + getMessage() + "\n";
	}
}
