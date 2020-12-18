package es.alba.sweet;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;

import es.alba.sweet.core.output.Output;
import es.alba.sweet.perspective.ToolBar;

/**
 * This is a stub implementation containing e4 LifeCycle annotated methods.<br />
 * There is a corresponding entry in <em>plugin.xml</em> (under the <em>org.eclipse.core.runtime.products' extension point</em>) that references this class.
 **/
public class E4LifeCycle {

	@PostContextCreate
	void postContextCreate(IEclipseContext workbenchContext) {
	}

	@PreSave
	void preSave(IEclipseContext workbenchContext) {
		System.out.println(this.getClass() + " presave");
		ToolBar toolBar = workbenchContext.get(ToolBar.class);
		System.out.println(workbenchContext + " " + toolBar.getJsonConfiguration().getConfiguration());
		toolBar.getJsonConfiguration().write();
	}

	@ProcessAdditions
	void processAdditions(IEclipseContext workbenchContext) {
		Output.DEBUG.info("es.alba.sweet.E4LifeCycle.processAdditions", "Injecting " + ToolBar.class.getSimpleName() + " in context " + workbenchContext);
		new EclipseUI(workbenchContext);
		// EModelService modelService = workbenchContext.get(EModelService.class);
		// EPartService partService = workbenchContext.get(EPartService.class);
		// MApplication application = workbenchContext.get(MApplication.class);

		ToolBar toolBar = new ToolBar();
		workbenchContext.set(ToolBar.class, toolBar);
		Output.DEBUG.info("es.alba.sweet.E4LifeCycle.processAdditions", ToolBar.class.getSimpleName() + " injected in context " + workbenchContext);
	}

	@ProcessRemovals
	void processRemovals(IEclipseContext workbenchContext) {
	}
}
