package es.alba.sweet.core.output;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.core.services.events.IEventBroker;

import es.alba.sweet.Activator;

public enum Output {

	DEBUG(OutputName.DEBUG), MESSAGE(OutputName.MESSAGE, DEBUG);

	private List<AMessage>	messages	= new ArrayList<>();

	private List<Output>	outputs		= new ArrayList<>();

	private IEventBroker	eventBroker;

	private String			name;

	Output(String name) {
		this.name = name;
	}

	Output(String name, Output... outputs) {
		this.outputs.addAll(List.of(outputs));
		this.name = name;
	}

	public void setIEventBroker(IEventBroker eventBroker) {
		this.eventBroker = eventBroker;
		this.outputs.forEach(a -> a.setIEventBroker(eventBroker));
	}

	public List<AMessage> getMessages() {
		return this.messages;
	}

	public void info(String method, String message) {
		AMessage info = Factory(name, MessageType.INFO, method, message);
		message(info);
	}

	public void warning(String method, String message) {
		AMessage warning = Factory(name, MessageType.WARNING, method, message);
		message(warning);
	}

	public void error(String method, String message) {
		AMessage error = Factory(name, MessageType.ERROR, method, message);
		message(error);
	}

	private void message(AMessage message) {
		messages.add(message);

		outputs.forEach(a -> a.message(Factory(a.name, message.getType(), message.getMethod(), message.getMessage())));

		if (eventBroker != null) {
			eventBroker.send(name, message);
		}

		if (name.equalsIgnoreCase(OutputName.DEBUG)) {
			Activator.LOGGER.info(message.toString());
		}
		System.out.print(message);
	}

	private static AMessage Factory(String name, MessageType type, String method, String message) {
		switch (name) {
		case OutputName.MESSAGE:
			return new Message(type, method, message);
		case OutputName.DEBUG:
			return new DebugMessage(type, method, message);
		default:
			break;
		}

		return null;
	}

}
