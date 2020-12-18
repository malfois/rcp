package es.alba.sweet.core.constant;

import java.util.List;

public class Id {

	public final static String			SWEET						= "es.alba.sweet";
	public final static String			WINDOW						= String.join(".", SWEET, "window", "main");
	public final static String			PERSPECTIVE_STACK			= String.join(".", SWEET, "perspectivestack");
	public final static String			PARTSTACK					= String.join(".", SWEET, "partstack");

	private final static String			PART_DESCRIPTOR				= "partdescriptor";

	public final static String			OUTPUT						= "output";
	public final static String			MESSAGE						= String.join(".", SWEET, PART_DESCRIPTOR, OUTPUT, "message");
	public final static String			DEBUG						= String.join(".", SWEET, PART_DESCRIPTOR, OUTPUT, "debug");
	public final static List<String>	OUTPUT_PART_IDS				= List.of(Id.MESSAGE, Id.DEBUG);

	public final static String			MAIN_TRIM_BAR				= "es.alba.sweet.trimbar.top";
	public final static String			PERSPECTIVE_NAME			= "es.alba.sweet.toolcontrol.perspective.name";
	public final static String			PERSPECTIVE_LAYOUT			= "es.alba.sweet.toolcontrol.perspective.layout";
	public final static String			PERSPECTIVE_VIEWS			= "es.alba.sweet.toolcontrol.perspective.views";

	public final static String			SHOW_VIEWS_COMMAND			= "es.alba.sweet.command.showview";
	public final static String			SHOW_PERSPECTIVE_COMMAND	= "es.alba.sweet.command.showperspective";
}
