
package es.alba.sweet.toolbar;

import org.eclipse.e4.core.di.annotations.Execute;

import es.alba.sweet.core.output.Output;

public class ResetPerspective {
	@Execute
	public void execute() {
		Output.MESSAGE.info("es.alba.sweet.toolbar.ResetPerspective.execute", "Perspective reset in action");
	}

}