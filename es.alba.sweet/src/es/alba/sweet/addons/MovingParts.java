
package es.alba.sweet.addons;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.UIEvents.EventTags;
import org.osgi.service.event.Event;

import es.alba.sweet.core.output.Output;

public class MovingParts {

	@Inject
	@Optional
	public void subscribeTopicElementContainerChildren(@EventTopic(UIEvents.ElementContainer.TOPIC_CHILDREN) Event event) {
		Object element = event.getProperty(EventTags.ELEMENT);
		if (!(element instanceof MPartStack)) {
			return;
		}

		if (UIEvents.isADD(event)) {
			// check new value, because we check for addition and old value will be null
			Object newValue = event.getProperty(EventTags.NEW_VALUE);
			if (newValue instanceof MPart) {
				MPart part = (MPart) newValue;
				Output.DEBUG.info("es.alba.sweet.addons.MovingParts.subscribeTopicElementContainerChildren",
						"Added " + part.getLabel() + " at position: " + event.getProperty(EventTags.POSITION));
			}
		} else if (UIEvents.isREMOVE(event)) {
			// check old value, because we check for remove and new value will be null
			Object oldValue = event.getProperty(EventTags.OLD_VALUE);
			if (oldValue instanceof MPart) {
				MPart part = (MPart) oldValue;
				Output.DEBUG.info("es.alba.sweet.addons.MovingParts.subscribeTopicElementContainerChildren",
						"Removed " + part.getLabel() + " from position: " + event.getProperty(EventTags.POSITION));
			}
		}
	}

}
