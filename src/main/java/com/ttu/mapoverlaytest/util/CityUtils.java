package com.ttu.mapoverlaytest.util;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.ttu.mapoverlaytest.model.app.City;
import com.ttu.mapoverlaytest.model.rest.CityObject;

public class CityUtils {

	public static List<City> convertToCities(List<CityObject> cityObjects) {
		List<City> cities = new ArrayList<City>(cityObjects.size());
		for (CityObject cityObject : cityObjects) {
			cities.add(convertToCity(cityObject));
		}
		return cities;
	}

	private static City convertToCity(CityObject cityObject) {
		int latitude = (int) (cityObject.getLatitude() * 1E6);
		int longitude = (int) (cityObject.getLongitude() * 1E6);
		GeoPoint geoPoint = new GeoPoint(latitude, longitude);

		City city = new City(geoPoint, cityObject.getName());
		city.setAvailableRoutes(new ArrayList<String>(cityObject.getAvailableRoutes()));
		return city;
	}
}
