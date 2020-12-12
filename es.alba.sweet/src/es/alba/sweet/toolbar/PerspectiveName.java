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

import es.alba.sweet.core.IconLoader;

public class PerspectiveName {
	private Composite		parent;
	private ResourceManager	resManager;

	private Label			nameLabel;
	private Label			imageLabel;

	@PostConstruct
	public void createGui(Composite parent) {
		this.parent = parent;

		resManager = new LocalResourceManager(JFaceResources.getResources(), parent);

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout compositeLayout = new GridLayout();
		compositeLayout.horizontalSpacing = 5;
		compositeLayout.numColumns = 3;
		composite.setLayout(compositeLayout);

		Label perspective = new Label(composite, SWT.NONE);
		perspective.setText("Current Perspective:");

		imageLabel = new Label(composite, SWT.NONE);
		GridData gridData = new GridData();
		gridData.widthHint = 20;
		imageLabel.setLayoutData(gridData);
		imageLabel.setImage(resManager.createImage(IconLoader.load("scan.png")));

		nameLabel = new Label(composite, SWT.BORDER);
		nameLabel.setText("Scan");
	}
}