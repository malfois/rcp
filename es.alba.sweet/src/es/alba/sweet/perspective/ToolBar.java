package es.alba.sweet.perspective;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import es.alba.sweet.EclipseUI;
import es.alba.sweet.configuration.Json;
import es.alba.sweet.core.constant.Id;
import es.alba.sweet.core.output.Output;

//@Creatable
public class ToolBar {

	private EModelService		modelService		= EclipseUI.modelService();
	private EPartService		partService			= EclipseUI.partService();
	private MApplication		application			= EclipseUI.application();

	private Json<Configuration>	jsonConfiguration	= new Json<>(new Configuration());

	private Name				name;
	private Layout				layout;
	private Views				views;

	public ToolBar() {
		jsonConfiguration.read();
		jsonConfiguration.print();
	}

	public void build() {
		Output.DEBUG.info("es.alba.sweet.perspective.SPerspective.build", "Creating the perspectives");

		modelService = EclipseUI.modelService();
		partService = EclipseUI.partService();
		application = EclipseUI.application();

		MToolControl toolControl = (MToolControl) modelService.find(Id.PERSPECTIVE_NAME, application);
		this.name = (Name) toolControl.getObject();

		toolControl = (MToolControl) modelService.find(Id.PERSPECTIVE_LAYOUT, application);
		this.layout = (Layout) toolControl.getObject();

		toolControl = (MToolControl) modelService.find(Id.PERSPECTIVE_VIEWS, application);
		this.views = (Views) toolControl.getObject();

		MUIElement element = modelService.find(Id.PERSPECTIVE_STACK, application);

		if (!(element instanceof MPerspectiveStack)) return;

		MPerspectiveStack perspectiveStack = (MPerspectiveStack) element;

		// If perspective stack is empty, load all default perspectives
		if (perspectiveStack.getChildren().isEmpty()) {
			Output.DEBUG.info("es.alba.sweet.perspective.ToolBar.build", "No perspective found. Loading default perspectives");
			loadAllDefaultPerspectives(perspectiveStack);
			Output.DEBUG.info("es.alba.sweet.perspective.ToolBar.build", "Default perspectives loaded");
			// return;
		}

		MPerspective activePerspective = EclipseUI.activePerspective();
		update(jsonConfiguration.getConfiguration().getPerspective(activePerspective.getElementId()));
	}

	public void loadAllDefaultPerspectives(MPerspectiveStack perspectiveStack) {
		List<String> elementsIds = application.getSnippets().stream().filter(p -> (p instanceof MPerspective)).map(m -> (String) m.getElementId()).collect(Collectors.toList());

		boolean isFirst = true;
		for (String elementId : elementsIds) {
			Output.DEBUG.info("es.alba.sweet.perspective.ToolBar.loadAllDefaultPerspectives", "loading default perspective " + elementId);
			MPerspective perspective = layout.loadDefaultPerspective(elementId);
			if (perspective != null) {
				perspectiveStack.getChildren().add(perspective);
				PerspectiveConfiguration configuration = new PerspectiveConfiguration(perspective.getElementId());
				configuration.setSelectedLayout(PerspectiveConfiguration.DEFAULT);
				boolean added = jsonConfiguration.getConfiguration().add(configuration);
				if (!added) jsonConfiguration.getConfiguration().getPerspective(perspective.getElementId()).setSelectedLayout(PerspectiveConfiguration.DEFAULT);
				if (isFirst) {
					perspectiveStack.setSelectedElement(perspective);
					isFirst = false;
					Output.DEBUG.info("es.alba.sweet.perspective.ToolBar.loadAllDefaultPerspectives", "first perspective set as active");
				}
			}
		}
	}

	public void update(PerspectiveConfiguration configuration) {
		this.name.update(configuration);
		this.layout.update(configuration);
		this.views.update(configuration);
	}

	public Json<Configuration> getJsonConfiguration() {
		return jsonConfiguration;
	}

	public Layout getLayoutComponent() {
		return this.layout;
	}
}
