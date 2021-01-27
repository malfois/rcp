package es.alba.sweet.addons;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.osgi.service.event.Event;

import es.alba.sweet.EclipseUI;
import es.alba.sweet.configuration.Json;
import es.alba.sweet.core.constant.Id;
import es.alba.sweet.core.output.Output;
import es.alba.sweet.handlers.MovingPartListener;
import es.alba.sweet.handlers.VisibilityPartListener;
import es.alba.sweet.perspective.Configuration;
import es.alba.sweet.perspective.Perspective;
import es.alba.sweet.perspective.PerspectiveConfiguration;
import es.alba.sweet.perspective.PerspectiveControl;

public class Initialise {

	@Inject
	EModelService		modelService;
	@Inject
	EPartService		partService;
	@Inject
	MApplication		application;
	@Inject
	IEventBroker		eventBroker;
	@Inject
	Json<Configuration>	json;

	@Inject
	@Optional
	public void applicationStarted(@EventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event) {

		Output.DEBUG.info("es.alba.sweet.addons.Initialise.applicationStarted", "Initializing the application");

		Output.MESSAGE.setIEventBroker(eventBroker);

		eventBroker.subscribe(UIEvents.ElementContainer.TOPIC_CHILDREN, new MovingPartListener(EclipseUI.getPerspectiveToolControl()));

		json.print();

		// add the perspective to the perspective stack
		build();

		MToolControl toolControl = EclipseUI.getPerspectiveToolControl();
		PerspectiveControl control = (PerspectiveControl) toolControl.getObject();
		control.addButtons(new ArrayList<>(partService.getParts()));

		MTrimmedWindow window = (MTrimmedWindow) modelService.find(Id.WINDOW, application);
		VisibilityPartListener partListener = new VisibilityPartListener();
		EPartService service = window.getContext().get(EPartService.class);
		service.addPartListener(partListener);

		Output.MESSAGE.info("es.alba.sweet.addons.Initialise.applicationStarted", "All initialization done!");

	}

	public void build() {
		Output.DEBUG.info("es.alba.sweet.addons.Initialise.build", "Creating the perspectives");

		modelService = EclipseUI.modelService();
		partService = EclipseUI.partService();
		application = EclipseUI.application();

		MUIElement element = modelService.find(Id.PERSPECTIVE_STACK, application);

		if (!(element instanceof MPerspectiveStack)) return;

		MPerspectiveStack perspectiveStack = (MPerspectiveStack) element;

		// If perspective stack is empty, load all default perspectives
		if (perspectiveStack.getChildren().isEmpty()) {
			Output.DEBUG.info("es.alba.sweet.perspective.ToolBar.build", "No perspective found. Loading default perspectives");
			loadAllDefaultPerspectives(perspectiveStack);
			Output.DEBUG.info("es.alba.sweet.perspective.ToolBar.build", "Default perspectives loaded");
		}

		MPerspective activePerspective = EclipseUI.activePerspective();
		// update(jsonConfiguration.getConfiguration().getPerspective(activePerspective.getElementId()));
	}

	public void loadAllDefaultPerspectives(MPerspectiveStack perspectiveStack) {
		List<String> elementsIds = application.getSnippets().stream().filter(p -> (p instanceof MPerspective)).map(m -> (String) m.getElementId()).collect(Collectors.toList());

		boolean isFirst = true;
		for (String elementId : elementsIds) {
			MPerspective perspective = Perspective.loadDefaultPerspective(elementId);
			if (perspective != null) {
				Output.DEBUG.info("es.alba.sweet.perspective.ToolBar.loadAllDefaultPerspectives", "loading default perspective " + elementId);
				perspectiveStack.getChildren().add(perspective);
				PerspectiveConfiguration configuration = json.getConfiguration().getPerspective(perspective.getElementId());
				configuration.setSelectedLayout(PerspectiveConfiguration.DEFAULT);
				if (isFirst) {
					perspectiveStack.setSelectedElement(perspective);
					isFirst = false;
					Output.DEBUG.info("es.alba.sweet.perspective.ToolBar.loadAllDefaultPerspectives", "first perspective set as active");
				}
			}
		}
	}

}
