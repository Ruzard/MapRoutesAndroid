package com.ttu.mapoverlaytest;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.ttu.mapoverlaytest.model.abstr.ServiceUpdateListener;
import com.ttu.mapoverlaytest.model.app.City;
import com.ttu.mapoverlaytest.model.async.CityLoadingTask;
import com.ttu.mapoverlaytest.model.database.DatabaseHelperFactory;
import com.ttu.mapoverlaytest.overlays.CityItemizedOverlay;
import com.ttu.mapoverlaytest.service.external.CityService;

public class MyActivity extends MapActivity implements ServiceUpdateListener {

	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;
	private CityService cityService = new CityService();

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DatabaseHelperFactory.constructHelper(getApplicationContext());
		setContentView(R.layout.main);

		//initializing icons for markers
		Drawable selectedIcon = getResources().getDrawable(R.drawable.radiobutton_selected);
		City.setSelectedDrawable(selectedIcon);

		Drawable notSelectedDrawable = getResources().getDrawable(R.drawable
				.radiobutton);
		City.setNotSelectedDrawable(notSelectedDrawable);


		if (hasConnection()) {
			CityLoadingTask task = new CityLoadingTask(this, this);
			task.execute();
		} else {
			updateReceived();
		}
	}


	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onDestroy() {
		DatabaseHelperFactory.destructHelper();
		super.onDestroy();
	}

	public void updateReceived() {
		List<City> cities = null;
		try {
			cities = DatabaseHelperFactory.getHelper().getCityDao().getAllCities();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (cities != null && cities.size() > 0) {
			CityItemizedOverlay cityItemizedOverlay = new CityItemizedOverlay(City.getNotSelectedDrawable(), this,
					mapView);
			for (City city : cities) {
				cityItemizedOverlay.addOverlay(city);
			}

			mapView = (MapView) findViewById(R.id.mapView);
			mapView.setBuiltInZoomControls(true);

			mapView.getOverlays().add(cityItemizedOverlay);
			mapView.postInvalidate();
			cityItemizedOverlay.zoomToEverything(mapView);
		} else {
			Toast.makeText(this, "No data stored in database, please, connect to the internet",
					Toast.LENGTH_LONG).show();
		}
	}

	public boolean hasConnection() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(
				Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected()) {
			return true;
		}

		NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected()) {
			return true;
		}

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}

		return false;
	}
}
