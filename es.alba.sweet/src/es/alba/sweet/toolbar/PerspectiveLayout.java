package es.alba.sweet.toolbar;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import es.alba.sweet.core.IconLoader;

public class PerspectiveLayout {
	private ResourceManager resManager;

	@PostConstruct
	public void createGui(Composite parent) {
		resManager = new LocalResourceManager(JFaceResources.getResources(), parent);

		Composite comp = new Composite(parent, SWT.NONE);
		// GridLayout with no margins
		comp.setLayout(GridLayoutFactory.fillDefaults().create());

		Label layout = new Label(comp, SWT.NONE);
		layout.setText("Layout");

		Combo combo = new Combo(comp, SWT.BORDER);
		String[] items = new String[] { "reset" };
		combo.setItems(items);
		combo.setText(items[0]);

		Button save = new Button(comp, SWT.PUSH);
		save.setToolTipText("Save the perspective layout into a file");
		save.setImage(resManager.createImage(IconLoader.load("save.png")));

		Button reset = new Button(comp, SWT.PUSH);
		reset.setToolTipText("Reset the perspective layout");
		reset.setImage(resManager.createImage(IconLoader.load("reset.png")));
	}
}