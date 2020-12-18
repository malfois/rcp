package es.alba.sweet.addons;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.osgi.service.event.Event;

import es.alba.sweet.core.constant.Id;
import es.alba.sweet.core.output.Output;
import es.alba.sweet.handlers.MovingPartListener;
import es.alba.sweet.handlers.VisibilityPartListener;
import es.alba.sweet.perspective.ToolBar;

public class Initialise {

	@Inject
	EModelService	modelService;
	@Inject
	EPartService	partService;
	@Inject
	MApplication	application;
	@Inject
	IEventBroker	eventBroker;
	@Inject
	ToolBar			toolBar;

	@Inject
	@Optional
	public void applicationStarted(@EventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event) {

		Output.DEBUG.info("es.alba.sweet.addons.Initialise.applicationStarted", "Initializing the application");

		Output.MESSAGE.setIEventBroker(eventBroker);

		eventBroker.subscribe(UIEvents.ElementContainer.TOPIC_CHILDREN, new MovingPartListener(toolBar));

		MTrimmedWindow window = (MTrimmedWindow) modelService.find(Id.WINDOW, application);
		VisibilityPartListener partListener = new VisibilityPartListener();
		EPartService service = window.getContext().get(EPartService.class);
		service.addPartListener(partListener);

		// add the perspective to the perspective stack
		toolBar.build();

		Output.MESSAGE.info("es.alba.sweet.addons.Initialise.applicationStarted", "All initialization done!");

	}

}
