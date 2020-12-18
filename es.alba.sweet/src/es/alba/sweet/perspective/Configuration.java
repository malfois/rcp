package es.alba.sweet.perspective;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.e4.core.di.annotations.Creatable;

import es.alba.sweet.configuration.AFileConfiguration;
import es.alba.sweet.core.output.Output;

@Creatable
public class Configuration extends AFileConfiguration {

	private List<PerspectiveConfiguration> perspectives = new ArrayList<>();

	public Configuration() {
		super("perspectives");
	}

	public List<PerspectiveConfiguration> getPerspectives() {
		return perspectives;
	}

	public void setPerspectives(List<PerspectiveConfiguration> perspectives) {
		this.perspectives = perspectives;
	}

	public PerspectiveConfiguration getPerspective(String perspectiveId) {
		Optional<PerspectiveConfiguration> result = perspectives.stream().filter(p -> p.getId().equals(perspectiveId)).findFirst();

		if (!result.isPresent()) {
			PerspectiveConfiguration configuration = new PerspectiveConfiguration(perspectiveId);
			perspectives.add(configuration);
			return configuration;
		}
		return result.get();
	}

	public void add(PerspectiveConfiguration perspective) {
		Optional<PerspectiveConfiguration> result = perspectives.stream().filter(p -> p.getId().equals(perspective.getId())).findFirst();

		if (!result.isPresent()) perspectives.add(perspective);
		Output.DEBUG.error("es.alba.sweet.perspective.Configuration.add", "Perspective " + perspective.getId() + " already configured. New configuration not added");
	}

	@Override
	public String toString() {
		return "Configuration [perspectives=" + perspectives + "]";
	}

}
