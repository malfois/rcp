package es.alba.sweet.perspective;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public abstract class AToolControl {

	public abstract void update(PerspectiveConfiguration configuration);

	protected Composite			composite;
	protected ResourceManager	resourceManager;

	protected void set(Composite parent) {
		this.resourceManager = new LocalResourceManager(JFaceResources.getResources(), parent);
		this.composite = new Composite(parent, SWT.NONE);
	}

	public Composite getParentComposite() {
		return composite.getParent();
	}
}
