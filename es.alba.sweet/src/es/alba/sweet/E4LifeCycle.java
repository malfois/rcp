package es.alba.sweet;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;

/**
 * This is a stub implementation containing e4 LifeCycle annotated methods.<br />
 * There is a corresponding entry in <em>plugin.xml</em> (under the <em>org.eclipse.core.runtime.products' extension point</em>) that references this class.
 **/
public class E4LifeCycle {

	@PostContextCreate
	void postContextCreate(IEclipseContext workbenchContext) {
		IEclipseContext serviceContext = E4Workbench.getServiceContext();
		IEclipseContext applicationContext = (IEclipseContext) serviceContext.getActiveChild();
		// MApplication application = serviceContext.get(MApplication.class);

		System.out.println(this.getClass() + " postContextCreate" + " " + applicationContext);
		// ContextInjectionFactory.make(SPerspective.class, workbenchContext);
	}

	@PreSave
	void preSave(IEclipseContext workbenchContext) {
	}

	@ProcessAdditions
	void processAdditions(IEclipseContext workbenchContext) {
		IEclipseContext serviceContext = E4Workbench.getServiceContext();
		IEclipseContext applicationContext = (IEclipseContext) serviceContext.getActiveChild();

		System.out.println(this.getClass() + " processAdditions" + " " + applicationContext);
	}

	@ProcessRemovals
	void processRemovals(IEclipseContext workbenchContext) {
	}
}
