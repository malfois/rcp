
package es.alba.sweet.addons;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.osgi.service.event.Event;

import es.alba.sweet.perspective.ToolBar;

public class ClosingApplication {

	@Inject
	@Optional
	public void closing(@EventTopic(UIEvents.UILifeCycle.APP_SHUTDOWN_STARTED) Event event, ToolBar toolBar) {
		System.out.println("Closing app");
	}

}
