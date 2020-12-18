package es.alba.sweet.perspective;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import es.alba.sweet.EclipseUI;
import es.alba.sweet.core.IconLoader;

public class Name extends AToolControl {

	private Label	nameLabel;
	private Label	imageLabel;

	@PostConstruct
	public void createGui(Composite parent) {
		super.set(parent);

		GridLayout compositeLayout = new GridLayout();
		compositeLayout.horizontalSpacing = 5;
		compositeLayout.numColumns = 3;
		composite.setLayout(compositeLayout);

		Label perspective = new Label(composite, SWT.NONE);
		perspective.setText("Current Perspective:");

		nameLabel = new Label(composite, SWT.NONE);

		imageLabel = new Label(composite, SWT.NONE);
		GridData imageGridData = new GridData();
		imageGridData.widthHint = 20;
		imageLabel.setLayoutData(imageGridData);
	}

	@Override
	public void update(PerspectiveConfiguration configuration) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				nameLabel.setText(configuration.getLabel());
				nameLabel.pack();

				MPerspective activePerspective = EclipseUI.activePerspective();
				String iconURI = activePerspective.getIconURI();
				if (iconURI == null) {
					MUIElement element = EclipseUI.modelService().findSnippet(EclipseUI.application(), configuration.getId());
					MPerspective perspective = (MPerspective) element;
					iconURI = perspective.getIconURI();
				}
				imageLabel.setImage(resourceManager.createImage(IconLoader.loadFromURI(iconURI)));
				composite.pack();
			}
		});
	}
}