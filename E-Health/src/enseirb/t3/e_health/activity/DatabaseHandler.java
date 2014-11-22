package enseirb.t3.e_health.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import enseirb.t3.e_health.entity.Data;
import enseirb.t3.e_health.entity.User;


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
	private static final String DATABASE_NAME = "E-health";

	// Beacons table name
	private static final String TABLE_BEACONS = "Beacons";
	// Users table name
	private static final String TABLE_USERS = "Users";
	// Data table name
	private static final String TABLE_DATA = "Data";

	// Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_MAC = "mac";
	private static final String KEY_UUID = "uuid";
	private static final String KEY_MAJOR = "major";
	private static final String KEY_MINOR = "minor";
	private static final String KEY_LAT = "lat";
	private static final String KEY_LNG = "lng";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_DATANAME = "dataname";
	private static final String KEY_VALUE = "value";
	private static final String KEY_DATE = "date";

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
		
		String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME +  " TEXT," + KEY_PASSWORD +  " TEXT"+ ")";
		db.execSQL(CREATE_USER_TABLE);
		
		String CREATE_DATA_TABLE = "CREATE TABLE " + TABLE_DATA + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATANAME +  " TEXT," + KEY_VALUE +  " TEXT," + KEY_DATE + " TEXT " + ")";
		db.execSQL(CREATE_DATA_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BEACONS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);

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
	public MyBeacon getBeacon(int id) {
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
	
	public void create(User object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, object.getUsername()); 
		values.put(KEY_PASSWORD, object.getPassword());

		// Inserting Row
		db.insert(TABLE_USERS, null, values);
		db.close(); // Closing database connection
	}
	
	public void create(Data object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATANAME, object.getDataname()); 
		values.put(KEY_VALUE, object.getValue());
		values.put(KEY_DATE, object.getDate());

		// Inserting Row
		db.insert(TABLE_DATA, null, values);
		db.close(); // Closing database connection
	}
	
	public User retriveUser(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_USERNAME,
				KEY_PASSWORD}, KEY_ID + "=?",
				new String[] { Integer.toString(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		User user = new User(cursor.getString(0), cursor.getString(1));
		user.setId(id);

		return user;
	}

	public Data retriveData(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_DATA, new String[] { KEY_DATANAME,
				KEY_VALUE, KEY_DATE}, KEY_ID + "=?",
				new String[] { Integer.toString(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Data data = new Data(cursor.getString(0), cursor.getString(1), cursor.getString(2));
		data.setId(id);

		return data;
	}
	
	
	public int update(User object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, object.getUsername());
		values.put(KEY_PASSWORD, object.getPassword());

		// updating row
		return db.update(TABLE_USERS, values, KEY_ID + " = ?",
				new String[] { Integer.toString(object.getId()) });
	}
	
	public int update(Data object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATANAME, object.getDataname());
		values.put(KEY_VALUE, object.getValue());
		values.put(KEY_DATE, object.getDate());

		// updating row
		return db.update(TABLE_DATA, values, KEY_ID + " = ?",
				new String[] { Integer.toString(object.getId()) });
	}

	
	public void deleteUser(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, KEY_ID + " = ?",
				new String[] { Integer.toString(id)});
		db.close();
	}
	
	public void deleteData(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DATA, KEY_ID + " = ?",
				new String[] { Integer.toString(id)});
		db.close();
	}

	public void deleteAllUser() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, null, null);
		db.close();
	}

	public boolean isUser(String username, String password){
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE " + KEY_USERNAME + " = ?" + " AND " + KEY_PASSWORD + " = ?";

		Cursor cursor = db.rawQuery(selectQuery, new String [] {username, password});
//		db.close();
		return cursor.moveToFirst();	
	}
	
	public void deleteAllData() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DATA, null, null);
		db.close();
	}
	
	public List<Data> getDatas(String dataname) {
		
		Data data = new Data();
		List<Data> datas=new LinkedList<Data>();
		
		SQLiteDatabase db = this.getReadableDatabase();

		/*Cursor cursor = db.query(TABLE_DATA, new String[] { KEY_DATANAME,
				KEY_VALUE, KEY_DATE}, KEY_DATANAME + "=?",
				new String[] {dataname}, null, null, null, null);*/
		Cursor cursor=db.rawQuery("select " + KEY_DATANAME +","+KEY_VALUE+","+KEY_DATE + " from "+ TABLE_DATA +" where "+  KEY_DATANAME +"= ?", new String[]{"B"});
		
		while (cursor.moveToNext()) {
			
            data = new Data(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            datas.add(data);

		}
		db.close();
		return datas;
	}

}
