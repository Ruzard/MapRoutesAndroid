package com.ttu.mapoverlaytest.model.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.ttu.mapoverlaytest.model.rest.CityObject;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String TAG = DatabaseHelper.class.getSimpleName();

	private static final String DATABASE_NAME ="vorgumap.db";

	private static final int DATABASE_VERSION = 1;

	private static CityDao cityDao = null;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, CityObject.class);
		} catch (SQLException e){
			Log.e(TAG, "error creating DB " + DATABASE_NAME);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			TableUtils.dropTable(connectionSource, CityObject.class, true);
		} catch (SQLException e){
			Log.e(TAG, "error dropping DB " + DATABASE_NAME);
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close(){
		super.close();
		cityDao = null;
	}

	public CityDao getCityDao(){
		if(cityDao == null){
			try {
				cityDao = new CityDao(getConnectionSource(), CityObject.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return cityDao;
	}

}
