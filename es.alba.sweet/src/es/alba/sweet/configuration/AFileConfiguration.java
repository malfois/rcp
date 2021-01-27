package es.alba.sweet.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.alba.sweet.AModelObject;

public abstract class AFileConfiguration extends AModelObject {

	@JsonIgnore
	public String filePrefix;

	protected AFileConfiguration(String filePrefix) {
		this.filePrefix = filePrefix;
	}

	public String getFilePrefix() {
		return filePrefix;
	}

	public void setFilePrefix(String filePrefix) {
		this.filePrefix = filePrefix;
	}

}
