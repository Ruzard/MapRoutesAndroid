package com.ttu.mapoverlaytest.model.app;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class City extends OverlayItem {
	private boolean isSelected;

	private List<String> availableRoutes;

	private static Drawable selectedDrawable;
	private static Drawable notSelectedDrawable;

	private City(GeoPoint geoPoint, String name, String s2) {
		super(geoPoint, name, s2);
		setMarker(notSelectedDrawable);
	}

	public City(GeoPoint geoPoint, String name) {
		this(geoPoint, name, null);
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;

		if (selected) {
			setMarker(selectedDrawable);
		} else {
			setMarker(notSelectedDrawable);
		}
	}
//
//	public Drawable getSelectedDrawable() {
//		return selectedDrawable;
//	}

	public static void setSelectedDrawable(Drawable selectedDrawable) {
		City.selectedDrawable = selectedDrawable;
		selectedDrawable.setBounds(
				0 - selectedDrawable.getIntrinsicWidth() / 2, 0 - selectedDrawable.getIntrinsicHeight(),
				selectedDrawable.getIntrinsicWidth() / 2, 0);
	}

	public static Drawable getNotSelectedDrawable() {
		return notSelectedDrawable;
	}

	public static void setNotSelectedDrawable(Drawable notSelectedDrawable) {
		City.notSelectedDrawable = notSelectedDrawable;
		notSelectedDrawable.setBounds(
				0 - notSelectedDrawable.getIntrinsicWidth() / 2, 0 - notSelectedDrawable.getIntrinsicHeight(),
				notSelectedDrawable.getIntrinsicWidth() / 2, 0);
	}

	public void onClick() {
		setSelected(!isSelected());
	}

	public List<String> getAvailableRoutes() {
		if (availableRoutes == null) {
			availableRoutes = new ArrayList<String>();
		}
		return availableRoutes;
	}

	public void setAvailableRoutes(List<String> availableRoutes) {
		this.availableRoutes = availableRoutes;
	}
}
