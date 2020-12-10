package es.alba.sweet.addons;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.osgi.service.event.Event;

import es.alba.sweet.core.Output;
import es.alba.sweet.perspective.SPerspective;

@SuppressWarnings("restriction")
public class Initialise {

	// public final static Logger LOGGER = Logger.getLogger(Initialise.class.getName());

	@Inject
	EModelService		modelService;
	@Inject
	EPartService		partService;
	@Inject
	ESelectionService	selectionService;
	@Inject
	MApplication		application;
	@Inject
	IEventBroker		eventBroker;

	@Inject
	@Optional
	public void applicationStarted(@EventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event) {

		// logger();

		Output.DEBUG.info("es.alba.sweet.addons.Initialise.applicationStarted", "Initializing the application");

		Output.MESSAGE.setIEventBroker(eventBroker);

		// add the SPerscpective class to the application contect
		IEclipseContext serviceContext = E4Workbench.getServiceContext();
		IEclipseContext applicationContext = (IEclipseContext) serviceContext.getActiveChild();
		SPerspective perspective = ContextInjectionFactory.make(SPerspective.class, applicationContext);

		// add the perspective to the perspective stack
		perspective.build(modelService, partService, application);

		Output.MESSAGE.info("es.alba.sweet.addons.Initialise.applicationStarted", "All initialization done!");
	}

	// private void logger() {
	//
	// // suppress the logging output to the console
	// Logger rootLogger = Logger.getLogger("");
	// Handler[] handlers = rootLogger.getHandlers();
	// if (handlers[0] instanceof ConsoleHandler) {
	// rootLogger.removeHandler(handlers[0]);
	// }
	//
	// LOGGER.setLevel(Level.INFO);
	// FileHandler fileTxt;
	// try {
	// fileTxt = new FileHandler("C:\\temp\\Logging.txt");
	// // create a TXT formatter
	// MessageFormatter formatterTxt = new MessageFormatter();
	// fileTxt.setFormatter(formatterTxt);
	// LOGGER.addHandler(fileTxt);
	//
	// } catch (SecurityException | IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	//
	// }
	// LOGGER.setLevel(Level.INFO);
	// }
	//
	// public class MessageFormatter extends Formatter {
	//
	// @Override
	// public String format(LogRecord record) {
	// return record.getMessage();
	// }
	//
	// }
}
