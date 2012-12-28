package com.ttu.mapoverlaytest.model.database;

import android.content.Context;
import com.j256.ormlite.android.apptools.OpenHelperManager;

public class DatabaseHelperFactory {
	private static DatabaseHelper databaseHelper;

	public static DatabaseHelper getHelper() {
		return databaseHelper;
	}

	public static void constructHelper(Context context) {
		databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
	}

	public static void destructHelper() {
		OpenHelperManager.releaseHelper();
		databaseHelper = null;
	}
}
