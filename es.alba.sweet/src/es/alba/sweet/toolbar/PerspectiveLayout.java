package es.alba.sweet.toolbar;

import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
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

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout compositeLayout = new GridLayout();
		compositeLayout.horizontalSpacing = 5;
		compositeLayout.numColumns = 4;
		composite.setLayout(compositeLayout);

		Label layout = new Label(composite, SWT.NONE | SWT.BOTTOM);
		layout.setText("Layout");

		Combo combo = new Combo(composite, SWT.BORDER);
		String[] items = new String[] { "reset" };
		combo.setItems(items);
		combo.setText(items[0]);
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println(combo.getText());
				List<String> items = List.of(combo.getItems());
				if (!items.contains(combo.getText())) {
					combo.add(combo.getText());
					// save perspective layout
				}

				// load perspective layout

			}

			public void widgetSelected(SelectionEvent e) {
				System.out.println(combo.getText());
				// load perspective layout
			}

		});

		Button save = new Button(composite, SWT.PUSH);
		save.setToolTipText("Save the perspective layout into a file");
		save.setImage(resManager.createImage(IconLoader.load("save.png")));

		Button reset = new Button(composite, SWT.PUSH);
		reset.setToolTipText("Reset the perspective layout");
		reset.setImage(resManager.createImage(IconLoader.load("reset.png")));
	}
}