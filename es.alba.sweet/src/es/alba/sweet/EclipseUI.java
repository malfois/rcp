package es.alba.sweet;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class EclipseUI {

	private static IEclipseContext eclipseContext;

	public EclipseUI(IEclipseContext eclipseContext) {
		EclipseUI.eclipseContext = eclipseContext;
	}

	public static EModelService modelService() {
		return eclipseContext.get(EModelService.class);
	}

	public static EPartService partService() {
		return eclipseContext.get(EPartService.class);
	}

	public static MApplication application() {
		return eclipseContext.get(MApplication.class);
	}

	public static MTrimmedWindow window() {
		return (MTrimmedWindow) application().getSelectedElement();
	}

	public static MPerspective activePerspective() {
		return modelService().getActivePerspective(window());
	}

}
