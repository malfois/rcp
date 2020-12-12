
package es.alba.sweet.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.widgets.Composite;

import es.alba.sweet.core.output.AMessage;
import es.alba.sweet.core.output.Output;
import es.alba.sweet.core.output.OutputName;

public class DebugViewer {

	private OutputTextViewer viewer;

	@Inject
	public DebugViewer() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		viewer = new OutputTextViewer(parent, Output.DEBUG);

		Output.DEBUG.info("es.alba.sweet.parts.DebugViewer.postConstruct", "DebugViewer constructed");

	}

	@Inject
	@Optional
	public void listUpdated(@UIEventTopic(OutputName.DEBUG) AMessage message) {
		this.viewer.add(message);
	}
}