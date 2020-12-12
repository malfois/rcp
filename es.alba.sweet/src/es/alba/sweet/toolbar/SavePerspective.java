
package es.alba.sweet.toolbar;

import org.eclipse.e4.core.di.annotations.Execute;

import es.alba.sweet.core.output.Output;

public class SavePerspective {
	@Execute
	public void execute() {
		Output.MESSAGE.info("es.alba.sweet.toolbar.SavePerspective.execute", "Save Perspective in action");
	}

}