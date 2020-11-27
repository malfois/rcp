package es.alba.sweet.addons;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.osgi.service.event.Event;

import es.alba.sweet.perspective.SPerspective;

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
	@Optional
	public void applicationStarted(@EventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event) {

		// add the SPerscpective class to the application contect
		IEclipseContext serviceContext = E4Workbench.getServiceContext();
		IEclipseContext applicationContext = (IEclipseContext) serviceContext.getActiveChild();
		SPerspective perspective = ContextInjectionFactory.make(SPerspective.class, applicationContext);

		// add the perspective to the perspective stack
		perspective.build(modelService, partService, application);

	}

}
