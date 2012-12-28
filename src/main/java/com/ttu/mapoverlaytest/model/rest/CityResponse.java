package com.ttu.mapoverlaytest.model.rest;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class CityResponse implements Serializable {
	@JsonProperty("response")
	private List<CityObject> cities;

	public List<CityObject> getCities() {
		return cities;
	}

	public void setCities(List<CityObject> cities) {
		this.cities = cities;
	}
}
