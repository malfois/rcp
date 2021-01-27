package es.alba.sweet.parts.output;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.swt.widgets.Display;

import es.alba.sweet.core.constant.Id;

public class OutputPartStack {

	public final static List<String> OUTPUT_PART_IDS = List.of(Id.MESSAGE, Id.DEBUG);

	public static void loadOutputParts(EModelService modelService, EPartService partService, MApplication application, MPerspective activePerspective) {
		// Find the output part stack
		String outputId = String.join(".", Id.PARTSTACK, activePerspective.getLabel().toLowerCase(), Id.OUTPUT);
		MPartStack outputPartStack = (MPartStack) modelService.find(outputId, activePerspective);

		// Get the elements id with the id in the output list
		List<String> stackElementsId = outputPartStack.getChildren().stream().filter(p -> OUTPUT_PART_IDS.contains(p.getElementId())).map(m -> m.getElementId())
				.collect(Collectors.toList());

		// Create the elements that not in the list
		List<String> elementsToCreate = OUTPUT_PART_IDS.stream().filter(p -> !stackElementsId.contains(p)).collect(Collectors.toList());

		List<MPart> clones = new ArrayList<>();
		for (String element : elementsToCreate) {
			MPart part = partService.createPart(element);
			clones.add((MPart) modelService.cloneElement(part, application));
		}

		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				outputPartStack.getChildren().addAll(clones);
			}
		});

	}

	private static Set<String> findDuplicates(List<String> listContainingDuplicates) {
		final Set<String> setToReturn = new HashSet<>();
		final Set<String> set1 = new HashSet<>();

		for (String text : listContainingDuplicates) {
			if (!set1.add(text)) {
				setToReturn.add(text);
			}
		}
		return setToReturn;
	}
}
