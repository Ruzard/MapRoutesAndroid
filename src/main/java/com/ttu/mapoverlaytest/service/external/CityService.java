package com.ttu.mapoverlaytest.service.external;

import java.util.List;

import com.ttu.mapoverlaytest.model.rest.CityObject;
import com.ttu.mapoverlaytest.model.rest.CityResponse;
import com.ttu.mapoverlaytest.model.rest.Constant;

public class CityService {
	private static final RestService service = new RestService();

	public static List<CityObject> getCities() {
		CityResponse response = (CityResponse) service.processRequest(Constant.REST_LOAD_CITIES, CityResponse.class);
		return response.getCities();
	}
}
