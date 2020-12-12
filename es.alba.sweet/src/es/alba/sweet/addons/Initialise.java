package es.alba.sweet.addons;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.osgi.service.event.Event;

import es.alba.sweet.core.output.Output;
import es.alba.sweet.handlers.VisibilityPartListener;
import es.alba.sweet.id.Id;
import es.alba.sweet.perspective.SPerspective;
import es.alba.sweet.toolbar.PerspectiveViews;

@SuppressWarnings("restriction")
public class Initialise {

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

		Output.DEBUG.info("es.alba.sweet.addons.Initialise.applicationStarted", "Initializing the application");

		Output.MESSAGE.setIEventBroker(eventBroker);

		// add the SPerscpective class to the application contect
		IEclipseContext serviceContext = E4Workbench.getServiceContext();
		IEclipseContext applicationContext = (IEclipseContext) serviceContext.getActiveChild();
		SPerspective perspective = ContextInjectionFactory.make(SPerspective.class, applicationContext);

		MTrimmedWindow window = (MTrimmedWindow) modelService.find(Id.WINDOW, application);
		VisibilityPartListener partListener = new VisibilityPartListener(modelService, application);
		EPartService service = window.getContext().get(EPartService.class);
		service.addPartListener(partListener);

		// add the perspective to the perspective stack
		perspective.build();
		List<MPart> parts = perspective.getParts();

		MToolControl toolControl = (MToolControl) modelService.find(Id.PERSPECTIVE_VIEWS, application);
		PerspectiveViews perspectiveViews = (PerspectiveViews) toolControl.getObject();
		perspectiveViews.initialiseViewsButtons(parts);

		Output.MESSAGE.info("es.alba.sweet.addons.Initialise.applicationStarted", "All initialization done!");

	}

}
