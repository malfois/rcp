package es.alba.sweet.parts;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import es.alba.sweet.core.output.Output;

public class OutputListViewer {

	private ListViewer viewer;

	public OutputListViewer(Composite parent, Output output) {
		// define the Viewer
		viewer = new ListViewer(parent, SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(output.getMessages());

	}

	public void refresh() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				viewer.refresh();
			}
		});
	}

}
