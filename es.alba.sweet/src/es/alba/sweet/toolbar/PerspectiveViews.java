package es.alba.sweet.toolbar;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import es.alba.sweet.core.IconLoader;
import es.alba.sweet.core.output.Output;

public class PerspectiveViews {

	private Composite		composite;
	private GridLayout		gridLayout	= new GridLayout();

	private ResourceManager	resManager;

	@Inject
	EPartService			partService;

	@PostConstruct
	public void createGui(EModelService modelService, MWindow window, Composite parent) {
		resManager = new LocalResourceManager(JFaceResources.getResources(), parent);

		composite = new Composite(parent, SWT.NONE);
		gridLayout.horizontalSpacing = 0;
		gridLayout.numColumns = 1;
		composite.setLayout(gridLayout);

		Label layout = new Label(composite, SWT.NONE);
		layout.setText("Views");
	}

	public void updateButton(MPart part) {
		Output.DEBUG.info("es.alba.sweet.toolbar.PerspectiveViews.updateButton", "updating button image from part visibility");
		if (composite.getChildren().length == 1) {
			Output.DEBUG.info("es.alba.sweet.toolbar.PerspectiveViews.updateButton", "No button yet in the toolbar - No update possible");
			return;
		}

		Button button = List.of(composite.getChildren()).stream().filter(p -> (p instanceof Button) && p.getToolTipText().contains(part.getLabel())).map(m -> (Button) m)
				.findFirst().orElse(null);
		if (button == null) {
			Output.DEBUG.error("es.alba.sweet.toolbar.PerspectiveViews.updateButton", "No button with label " + part.getLabel() + " found");
			return;
		}

		Image image = getImage(part);
		button.setImage(image);
		button.setToolTipText(updateToolTipText(part));
		Output.DEBUG.info("es.alba.sweet.toolbar.PerspectiveViews.updateButton", "Button image updated from part visibility");
	}

	public void initialiseViewsButtons(List<MPart> parts) {
		Output.DEBUG.info("es.alba.sweet.toolbar.PerspectiveViews.initialiseViewsButtons", "Creating perspective views in the toolbar");
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				gridLayout.numColumns = 1 + parts.size();
				deleteButtons();
				parts.forEach(a -> addButton(a));
				composite.pack();
			}
		});
		Output.DEBUG.info("es.alba.sweet.toolbar.PerspectiveViews.initialiseViewsButtons", "perspective views in the toolbar created");
	}

	private void addButton(MPart part) {
		Output.DEBUG.info("es.alba.sweet.toolbar.PerspectiveViews.addButton", "Creating a button for the part " + part.getElementId());
		Button button = new Button(composite, SWT.PUSH);
		button.setToolTipText(updateToolTipText(part));
		Image image = getImage(part);
		button.setImage(image);
		button.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
			boolean visible = partService.isPartVisible(part);
			if (!visible) {
				partService.activate(part);
				Output.DEBUG.info("es.alba.sweet.toolbar.PerspectiveViews.addButton", "View " + part.getLabel() + " visible");
			} else {
				partService.hidePart(part);
				Output.DEBUG.info("es.alba.sweet.toolbar.PerspectiveViews.addButton", "View " + part.getLabel() + " non visible");
			}
		}));

		Output.DEBUG.info("es.alba.sweet.toolbar.PerspectiveViews.addButton", "Button for the part " + part.getElementId() + " created");
	}

	private Image getImage(MPart part) {
		ImageDescriptor descriptor = IconLoader.loadFromURI(part.getIconURI());
		Image image = resManager.createImage(descriptor);
		if (partService.isPartVisible(part)) return image;
		return new Image(Display.getCurrent(), image, SWT.IMAGE_GRAY);
	}

	private void deleteButtons() {
		Output.DEBUG.info("es.alba.sweet.toolbar.PerspectiveViews.deleteButtons", "Deleting views buttons");
		List.of(composite.getChildren()).stream().filter(p -> (p instanceof Button)).forEach(a -> a.dispose());
	}

	private String updateToolTipText(MPart part) {
		String text = part.getLabel();
		if (partService.isPartVisible(part)) {
			text = text + " part visible\nClick for closing the part";
			return text;
		}

		text = text + " part non visible\nClick for activating the part";
		return text;

	}
}