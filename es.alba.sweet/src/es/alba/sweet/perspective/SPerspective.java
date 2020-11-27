package es.alba.sweet.perspective;

import java.util.List;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

import es.alba.sweet.id.Id;

@Creatable
public class SPerspective {

	public void build(EModelService modelService, EPartService partService, MApplication application) {
		MUIElement element = modelService.find(Id.PERSPECTIVE_STACK, application);

		if (!(element instanceof MPerspectiveStack)) return;
		MPerspectiveStack perspectiveStack = (MPerspectiveStack) element;
		if (!perspectiveStack.getChildren().isEmpty()) return;

		MPart message = partService.createPart("es.alba.sweet.partdescriptor.output.message");
		message.setLabel("Message");
		MPart debug = partService.createPart("es.alba.sweet.partdescriptor.output.message");
		debug.setLabel("Debug");

		List<MUIElement> elements = application.getSnippets();
		boolean isFirst = true;
		for (MUIElement e : elements) {
			if (e instanceof MPerspective) {
				MPerspective perspectiveClone = (MPerspective) modelService.cloneSnippet(application, e.getElementId(), null);

				MPart messageClone = (MPart) modelService.cloneElement(message, application);
				MPart debugClone = (MPart) modelService.cloneElement(debug, application);

				MPartStack partStack = (MPartStack) modelService.find("es.alba.sweet.partstack.output", perspectiveClone);
				System.out.println(partStack);
				partStack.getChildren().addAll(List.of(messageClone, debugClone));

				perspectiveStack.getChildren().add(perspectiveClone);
				if (isFirst) {
					perspectiveStack.setSelectedElement(perspectiveClone);
					isFirst = false;
				}
			}
		}
		return;
	}

	@Override
	public String toString() {
		return "SPerspective []";
	}

}
