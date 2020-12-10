package es.alba.sweet.core;

public class DebugMessage extends AMessage {

	public DebugMessage(MessageType type, String method, String message) {
		super(type, method, message);
	}

	@Override
	public String toString() {
		return getDateTime() + " - " + getType() + " - " + getMethod() + " - " + getMessage() + "\n";
	}

}
