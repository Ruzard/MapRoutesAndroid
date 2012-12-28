package com.ttu.mapoverlaytest.model.database;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import com.ttu.mapoverlaytest.model.app.City;
import com.ttu.mapoverlaytest.model.rest.CityObject;
import com.ttu.mapoverlaytest.util.CityUtils;

public class CityDao extends BaseDaoImpl<CityObject, String> {
	public CityDao(ConnectionSource connectionSource, Class<CityObject> dataClass) throws SQLException {
		super(connectionSource, dataClass);
	}

	public List<City> getAllCities() throws SQLException {
		return CityUtils.convertToCities(this.queryForAll());
	}

	public void createCity(CityObject cityToSave) {
		try {
			create(cityToSave);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createCities(List<CityObject> cities) {
		if (cities != null) {
			for (CityObject city : cities) {
				createCity(city);
			}
		}
	}
}
