package com.ttu.mapoverlaytest.model.rest;

import java.util.ArrayList;
import java.util.Collection;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;


@DatabaseTable(tableName = "cities")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CityObject {

	@DatabaseField(id = true)
	private String name;
	@DatabaseField(canBeNull = true, dataType = DataType.SERIALIZABLE)
	private ArrayList<String> availableRoutes;
	@DatabaseField(canBeNull = false, dataType = DataType.DOUBLE)
	private double latitude;
	@DatabaseField(canBeNull = false, dataType = DataType.DOUBLE)
	private double longitude;

	public Collection<String> getAvailableRoutes() {
		return availableRoutes;
	}

	public void setAvailableRoutes(ArrayList<String> availableRoutes) {
		this.availableRoutes = availableRoutes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
