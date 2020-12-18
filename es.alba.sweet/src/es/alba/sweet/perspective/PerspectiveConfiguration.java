package es.alba.sweet.perspective;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PerspectiveConfiguration {

	public final static String	DEFAULT	= "default";

	private String				id;

	private List<String>		layout	= new ArrayList<>();

	private String				selectedLayout;

	public PerspectiveConfiguration() {
		layout.add(DEFAULT);
		selectedLayout = DEFAULT;
	}

	public PerspectiveConfiguration(String id) {
		this();
		this.id = id;
	}

	@JsonIgnore
	public String getLabel() {
		int lastIndex = id.lastIndexOf(".");
		return id.substring(lastIndex + 1);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public List<String> getLayout() {
		if (!this.layout.contains(DEFAULT)) {
			this.layout.add(DEFAULT);
		}
		return layout;
	}

	public void setLayout(List<String> layout) {
		this.layout = layout;
	}

	public String getSelectedLayout() {
		if (selectedLayout == null || !this.getLayout().contains(selectedLayout)) {
			selectedLayout = this.layout.get(0);
		}
		return selectedLayout;
	}

	public void setSelectedLayout(String selectedLayout) {
		if (!this.getLayout().contains(selectedLayout)) {
			selectedLayout = this.layout.get(0);
		}
		this.selectedLayout = selectedLayout;
	}

	@JsonIgnore
	public String[] layoutToArray() {
		return this.layout.toArray(String[]::new);
	}

	@Override
	public String toString() {
		return "PerspectiveConfiguration [id=" + id + ", layout=" + layout + ", selectedLayout=" + selectedLayout + "]";
	}

}
