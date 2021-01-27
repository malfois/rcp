
package es.alba.sweet.scan.graph;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import es.alba.sweet.configuration.Json;
import es.alba.sweet.perspective.Configuration;

public class ScanLegend {

	@Inject
	Json<Configuration>	json;

	private int			n	= 0;

	@PostConstruct
	public void postConstruct(Composite parent) {
		Composite testComposite = new Composite(parent, SWT.NONE);
		GridLayout testLayout = new GridLayout();
		testLayout.horizontalSpacing = 10;
		testLayout.numColumns = 1;
		testComposite.setLayout(testLayout);

		Button test = new Button(testComposite, SWT.PUSH);
		test.setText("Test");
		test.addSelectionListener(new TestAdapter());
	}

	private class TestAdapter extends SelectionAdapter {

		public void widgetSelected(SelectionEvent e) {
			json.getConfiguration().setSelectedPerspectiveId("test " + String.valueOf(n));
			n++;
		}
	}
}