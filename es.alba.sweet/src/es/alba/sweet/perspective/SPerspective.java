package es.alba.sweet.perspective;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Display;

import es.alba.sweet.core.output.Output;
import es.alba.sweet.id.Id;

@Creatable
public class SPerspective {

	@Inject
	EModelService	modelService;
	@Inject
	EPartService	partService;
	@Inject
	MApplication	application;

	public void build() {
		MUIElement element = modelService.find(Id.PERSPECTIVE_STACK, application);

		if (!(element instanceof MPerspectiveStack)) return;
		MPerspectiveStack perspectiveStack = (MPerspectiveStack) element;
		if (!perspectiveStack.getChildren().isEmpty()) return;

		MPart message = partService.createPart(Id.MESSAGE);
		message.setLabel("Message");
		MPart debug = partService.createPart(Id.DEBUG);
		debug.setLabel("Debug");

		List<MPerspective> elements = application.getSnippets().stream().filter(p -> (p instanceof MPerspective)).map(m -> (MPerspective) m).collect(Collectors.toList());

		boolean isFirst = true;
		// for (MPerspective e : elements) {
		MPerspective perspectiveClone = (MPerspective) modelService.cloneSnippet(application, elements.get(0).getElementId(), null);

		MPart messageClone = (MPart) modelService.cloneElement(message, application);
		MPart debugClone = (MPart) modelService.cloneElement(debug, application);

		String id = String.join(".", Id.PARTSTACK, perspectiveClone.getLabel().toLowerCase(), Id.OUTPUT);

		MPartStack partStack = (MPartStack) modelService.find(id, perspectiveClone);
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				partStack.getChildren().addAll(List.of(messageClone, debugClone));
			}
		});

		perspectiveStack.getChildren().add(perspectiveClone);
		if (isFirst) {
			perspectiveStack.setSelectedElement(perspectiveClone);
			isFirst = false;
		}

		Output.DEBUG.info("es.alba.sweet.perspective.SPerspective.build", "Perspectives built");

	}

	public List<MPart> getParts() {
		MTrimmedWindow window = (MTrimmedWindow) modelService.find(Id.WINDOW, application);
		MPerspective activePerspective = modelService.getActivePerspective(window);
		return modelService.findElements(activePerspective, null, MPart.class);
	}

	@Override
	public String toString() {
		return "SPerspective []";
	}

}
