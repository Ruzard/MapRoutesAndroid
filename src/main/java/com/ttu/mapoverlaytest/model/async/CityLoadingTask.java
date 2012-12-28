package com.ttu.mapoverlaytest.model.async;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import com.ttu.mapoverlaytest.model.abstr.ServiceUpdateListener;
import com.ttu.mapoverlaytest.model.database.DatabaseHelperFactory;
import com.ttu.mapoverlaytest.model.rest.CityObject;
import com.ttu.mapoverlaytest.service.external.CityService;

public class CityLoadingTask extends AsyncTask<Void, Void, List<CityObject>> {
	private Context context;
	private ServiceUpdateListener listener;
	private ProgressDialog dialog;

	public CityLoadingTask(Context context, ServiceUpdateListener listener) {
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog = new ProgressDialog(context);
		dialog.setMessage("Loading cities");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				CityLoadingTask.this.cancel(true);
			}
		});
		dialog.show();
	}

	@Override
	protected List<CityObject> doInBackground(Void... params) {
		return CityService.getCities();
	}

	@Override
	protected void onPostExecute(List<CityObject> cities) {
		super.onPostExecute(cities);
		if (!isCancelled()) {
			dialog.dismiss();

			DatabaseHelperFactory.getHelper().getCityDao().createCities(cities);

			listener.updateReceived();
		}
	}
}
