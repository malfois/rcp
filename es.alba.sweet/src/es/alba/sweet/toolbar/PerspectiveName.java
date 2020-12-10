package es.alba.sweet.toolbar;

import javax.annotation.PostConstruct;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class PerspectiveName {
	private Composite		parent;
	private ResourceManager	resManager;

	private Label			nameLabel;
	private Label			imageLabel;

	@PostConstruct
	public void createGui(Composite parent) {
		this.parent = parent;

		resManager = new LocalResourceManager(JFaceResources.getResources(), parent);

		GridData gridLayout = new GridData();
		gridLayout.grabExcessHorizontalSpace = true;
		parent.setLayoutData(gridLayout);

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout compositeLayout = new GridLayout();
		compositeLayout.horizontalSpacing = 5;
		compositeLayout.numColumns = 7;
		composite.setLayout(compositeLayout);

		Label perspective = new Label(composite, SWT.NONE);
		perspective.setText("Current Perspective:");

		imageLabel = new Label(composite, SWT.NONE);
		GridData gridData = new GridData();
		gridData.widthHint = 20;
		imageLabel.setLayoutData(gridData);

		nameLabel = new Label(composite, SWT.BORDER);
		nameLabel.setText("Unknown");
	}
}