package es.alba.sweet.handlers;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.IPartListener;

import es.alba.sweet.core.output.Output;
import es.alba.sweet.id.Id;
import es.alba.sweet.toolbar.PerspectiveViews;

public class VisibilityPartListener implements IPartListener {

	EModelService	modelService;
	MApplication	application;

	public VisibilityPartListener(EModelService modelService, MApplication application) {
		this.modelService = modelService;
		this.application = application;
	}

	@Override
	public void partActivated(MPart part) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partBroughtToTop(MPart part) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partDeactivated(MPart part) {
		// TODO Auto-generated method stub

	}

	@Override
	public void partHidden(MPart part) {
		Output.DEBUG.info("es.alba.sweet.handlers.VisibilityPartListener.partHidden", part.getElementId() + "hidden");
		updateViewButton(part);
	}

	@Override
	public void partVisible(MPart part) {
		Output.DEBUG.info("es.alba.sweet.handlers.VisibilityPartListener.partHidden", part.getElementId() + "visible");
		updateViewButton(part);
	}

	private void updateViewButton(MPart part) {
		Output.DEBUG.info("es.alba.sweet.handlers.VisibilityPartListener.updateViewButton", "Updating button for " + part.getElementId());
		MToolControl toolControl = (MToolControl) modelService.find(Id.PERSPECTIVE_VIEWS, application);
		PerspectiveViews perspectiveViews = (PerspectiveViews) toolControl.getObject();
		perspectiveViews.updateButton(part);
	}

}
