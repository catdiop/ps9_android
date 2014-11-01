package com.example.e_health.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "BeaconsManager";

	// Beacons table name
	private static final String TABLE_BEACONS = "Beacons";

	// Beacons Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_MAC = "mac";
	private static final String KEY_UUID = "uuid";
	private static final String KEY_MAJOR = "major";
	private static final String KEY_MINOR = "minor";
	private static final String KEY_LAT = "lat";
	private static final String KEY_LNG = "lng";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_BEACONS_TABLE = "CREATE TABLE " + TABLE_BEACONS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME +  " TEXT," + KEY_MAC +  " TEXT," +
				KEY_UUID +  " TEXT," + KEY_MAJOR +  " TEXT," + KEY_MINOR + " TEXT," + KEY_LAT + 
				" DOUBLE," + KEY_LNG +  " DOUBLE" + ")";
		db.execSQL(CREATE_BEACONS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEACONS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new Beacon
	void addBeacon(MyBeacon beacon) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, beacon.getName()); // Beacon Title
		values.put(KEY_MAC, beacon.getMacAddr()); // Beacon Mac Address
		values.put(KEY_UUID, beacon.getUuid()); // Beacon Uuid
		values.put(KEY_MAJOR, beacon.getMajor()); // Beacon Major
		values.put(KEY_MINOR, beacon.getMinor()); // Beacon Minor

		// Inserting Row
		db.insert(TABLE_BEACONS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single Beacon
	MyBeacon getBeacon(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_BEACONS, new String[] { KEY_NAME,
				KEY_MAC, KEY_UUID, KEY_MAJOR, KEY_MINOR, KEY_LAT, KEY_LNG }, KEY_ID + "=?",
				new String[] { Integer.toString(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		MyBeacon beacon = new MyBeacon(cursor.getString(0), cursor.getString(1), 
				cursor.getString(2), cursor.getString(3), cursor.getString(4));
		beacon.setId(id);

		return beacon;
	}
	
	// Getting All Beacons
	public List<MyBeacon> getAllBeacons() {
		List<MyBeacon> beaconList = new ArrayList<MyBeacon>();
		// Select All Query
		String selectQuery = "SELECT * FROM " + TABLE_BEACONS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				// Adding Beacon to list
				MyBeacon beacon = new MyBeacon(cursor.getString(1), cursor.getString(2), 
						cursor.getString(3), cursor.getString(4), cursor.getString(5));
				beacon.setId(cursor.getInt(0));
				beaconList.add(beacon);
			} while (cursor.moveToNext());
		}

		// return Beacon list
		return beaconList;
	}

	// Updating single Beacon
	public int updateNameBeacon(String name, String macAddr) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name);

		// updating row
		return db.update(TABLE_BEACONS, values, KEY_MAC + " = ?",
				new String[] { macAddr });
	}

	// Deleting single Beacon
	public void deleteBeacon(MyBeacon beacon) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_BEACONS, KEY_MAC + " = ?",
				new String[] { beacon.getMacAddr() });
		db.close();
	}

	// Getting Beacons Count
	public int getBeaconsCount() {
		String countQuery = "SELECT * FROM " + TABLE_BEACONS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();

		return count;
	}

}
