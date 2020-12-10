package es.alba.sweet.core;

public enum MessageType {

	INFO("INFO"), WARNING("WARNING"), ERROR("ERROR");

	private String name;

	private MessageType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
