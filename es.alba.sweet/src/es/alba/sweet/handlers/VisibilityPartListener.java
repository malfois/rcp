package es.alba.sweet.handlers;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.IPartListener;

import es.alba.sweet.EclipseUI;
import es.alba.sweet.core.constant.Id;
import es.alba.sweet.core.output.Output;
import es.alba.sweet.perspective.Views;

public class VisibilityPartListener implements IPartListener {

	public VisibilityPartListener() {
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
		updateToolBar(part);
	}

	@Override
	public void partVisible(MPart part) {
		Output.DEBUG.info("es.alba.sweet.handlers.VisibilityPartListener.partHidden", part.getElementId() + "visible");
		updateToolBar(part);
	}

	private void updateToolBar(MPart part) {
		EModelService modelService = EclipseUI.modelService();
		MApplication application = EclipseUI.application();

		Output.DEBUG.info("es.alba.sweet.handlers.VisibilityPartListener.updateToolBar", "Updating button for " + part.getElementId());
		MToolControl toolControl = (MToolControl) modelService.find(Id.PERSPECTIVE_VIEWS, application);
		Views perspectiveViews = (Views) toolControl.getObject();
		perspectiveViews.updateButton(part);

	}

}
