
package es.alba.sweet.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.widgets.Composite;

import es.alba.sweet.core.AMessage;
import es.alba.sweet.core.Output;
import es.alba.sweet.core.OutputName;

public class MessageViewer {
	private OutputTextViewer viewer;

	@Inject
	public MessageViewer() {

	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		viewer = new OutputTextViewer(parent, Output.MESSAGE);

		Output.DEBUG.info("es.alba.sweet.parts.MessageViewer.postConstruct", "MessageViewer constructed");
	}

	@Inject
	@Optional
	public void listUpdated(@UIEventTopic(OutputName.MESSAGE) AMessage message) {
		this.viewer.add(message);
	}
}