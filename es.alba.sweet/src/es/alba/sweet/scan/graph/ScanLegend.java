
package es.alba.sweet.scan.graph;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.widgets.Composite;

import es.alba.sweet.perspective.SPerspective;

public class ScanLegend {
	@Inject
	public ScanLegend() {

	}

	@PostConstruct
	public void postConstruct(Composite parent, SPerspective perspective) {
		System.out.println(perspective);
	}

}