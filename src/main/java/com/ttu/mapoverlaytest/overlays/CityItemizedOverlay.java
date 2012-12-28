package com.ttu.mapoverlaytest.overlays;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;
import com.ttu.mapoverlaytest.model.app.City;

public class CityItemizedOverlay extends ItemizedOverlay<City> {

	public static final int TITLE_MARGIN = 10;
	public static final int FONT_SIZE = 15;

	private List<City> cities = new ArrayList<City>();
	private Context mContext;
	private MapView mapView;

	public CityItemizedOverlay(Drawable drawable, Context context, MapView mapView) {
		super(boundCenterBottom(drawable));
		this.mContext = context;
		this.mapView = mapView;
	}

	@Override
	protected City createItem(int i) {
		return cities.get(i);
	}

	@Override
	public int size() {
		return cities.size();
	}

	public void addOverlay(City cityObject) {
		cities.add(cityObject);
		populate();
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		if (!shadow) {
			this.mapView = mapView;

			City selectedCity = getSelectedObject();
			if (selectedCity != null) {
				Projection projection = mapView.getProjection();

				Point startPoint = new Point();
				projection.toPixels(selectedCity.getPoint(), startPoint);

				for (City overlay : getConnectedCities(selectedCity)) {

					//set up style
					Paint paint = new Paint() {
						{
							setStyle(Style.STROKE);
							setStrokeCap(Cap.ROUND);
							setStrokeWidth(3.0f);
							setAntiAlias(true);
						}
					};

					//get point and translate them to pixel coordinates
					Point destinationPoint = new Point();
					projection.toPixels(overlay.getPoint(), destinationPoint);

					int x2 = (startPoint.x + destinationPoint.x) / 2 + 50;
					int y2 = (startPoint.y + destinationPoint.y) / 2 + 50;


					Path path = new Path();
					path.moveTo(startPoint.x, startPoint.y);

					path.cubicTo(startPoint.x, startPoint.y, x2, y2,
							destinationPoint.x,
							destinationPoint.y);
					canvas.drawPath(path, paint);

					//text
					GeoPoint point = overlay.getPoint();
					Point markerBottomCenterCoords = new Point();
					mapView.getProjection().toPixels(point, markerBottomCenterCoords);

            /* Find the width and height of the title*/
					TextPaint paintText = new TextPaint();
					Paint paintRect = new Paint();

					Rect rect = new Rect();
					paintText.setTextSize(FONT_SIZE);
					paintText.getTextBounds(overlay.getTitle(), 0, overlay.getTitle().length(), rect);

					rect.inset(-TITLE_MARGIN, -TITLE_MARGIN);
					rect.offsetTo(markerBottomCenterCoords.x - rect.width() / 2, markerBottomCenterCoords.y - overlay
							.getMarker(0).getBounds().height() - rect.height());

					paintText.setTextAlign(Paint.Align.CENTER);
					paintText.setTextSize(FONT_SIZE);
					paintText.setARGB(255, 255, 255, 255);
					paintRect.setARGB(130, 0, 0, 0);

					canvas.drawRoundRect(new RectF(rect), 2, 2, paintRect);
					canvas.drawText(overlay.getTitle(), rect.left + rect.width() / 2,
							rect.bottom - TITLE_MARGIN, paintText);

				}
			}
		}
	}

	@Override
	protected boolean onTap(int i) {
		City cityObject = cities.get(i);
		removeClickedState(cityObject);
		cityObject.onClick();
		if (cityObject.isSelected())
			zoomToEverything(mapView);

		return super.onTap(i);
	}

	private void removeClickedState(City exceptCity) {
		for (City cityObject : cities) {
			if (exceptCity != null && !cityObject.equals(exceptCity))
				cityObject.setSelected(false);
		}
	}


	public void zoomToEverything(MapView mapView) {
		int minLatitude = Integer.MAX_VALUE;
		int maxLatitude = Integer.MIN_VALUE;
		int minLongitude = Integer.MAX_VALUE;
		int maxLongitude = Integer.MIN_VALUE;

		for (City overlay : cities) {
			GeoPoint point = overlay.getPoint();
			maxLongitude = Math.max(point.getLongitudeE6(), maxLongitude);
			maxLatitude = Math.max(point.getLatitudeE6(), maxLatitude);
			minLatitude = Math.min(point.getLatitudeE6(), minLatitude);
			minLongitude = Math.min(point.getLongitudeE6(), minLongitude);
		}

		GeoPoint geoPoint = new GeoPoint(
				(maxLatitude + minLatitude) / 2,
				(maxLongitude + minLongitude) / 2);
		mapView.getController().zoomToSpan(Math.abs(maxLatitude - minLatitude),
				Math.abs(maxLongitude - minLongitude));
		mapView.getController().animateTo(geoPoint);
	}

	private City getSelectedObject() {
		for (City overlay : cities) {
			if (overlay.isSelected()) {
				return overlay;
			}
		}
		return null;
	}

	private List<City> getConnectedCities(City city) {
		ArrayList<City> connectedCities = new ArrayList<City>();

		for (City availableCity : cities) {
			for (String route : city.getAvailableRoutes()) {
				if (route.equals(availableCity.getTitle()))
					connectedCities.add(availableCity);
			}
		}
		return connectedCities;
	}
}
