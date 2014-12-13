package enseirb.t3.e_health.DAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import enseirb.t3.e_health.entity.Alert;
import enseirb.t3.e_health.entity.Data;
import enseirb.t3.e_health.entity.Doctor;
import enseirb.t3.e_health.entity.Patient;
import enseirb.t3.e_health.entity.User;

public class DatabaseHandler extends SQLiteOpenHelper {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "E-health";

	// Users table name
	private static final String TABLE_USERS = "Users";
	// Patient table name
	private static final String TABLE_PATIENT = "Patient";
	//Doctor table name
	private static final String TABLE_DOCTOR = "Doctor";
	// Data table name
	private static final String TABLE_DATA = "Data";
	// Alert table name
	private static final String TABLE_ALERT = "Alert";
	// SavedData table name
	private static final String TABLE_SAVEDDATA = "SavedData";

	// Table Columns names
	private static final String KEY_ID_DATA = "idData";
	private static final String KEY_ID_PATIENT = "idPatient";
	private static final String KEY_ID_DOCTOR = "idDoctor";
	private static final String KEY_ID_ALERT = "idAlert";
	private static final String KEY_ID_SAVEDDATA = "idSavedData";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_TYPE = "type";
	private static final String KEY_DATANAME = "dataname";
	private static final String KEY_ALERTNAME = "alertname";
	private static final String KEY_VALUE = "value";
	private static final String KEY_DATE = "date";
	private static final String KEY_LASTNAME = "lastname";
	private static final String KEY_FIRSTNAME = "firstname";

	private static final String CREATE_USER_TABLE = "CREATE TABLE "
			+ TABLE_USERS + "(" + KEY_ID_DATA + " INTEGER PRIMARY KEY,"
			+ KEY_USERNAME + " TEXT," + KEY_PASSWORD + " TEXT," + KEY_TYPE
			+ " TEXT )";

	private static final String CREATE_PATIENT_TABLE = "CREATE TABLE "
			+ TABLE_PATIENT + "(" + KEY_ID_PATIENT + " INTEGER,"
			+ KEY_FIRSTNAME + " TEXT," + KEY_LASTNAME + " TEXT,"
			+ KEY_ID_DOCTOR + " INTEGER" + " )";

	private static final String CREATE_DOCTOR_TABLE = "CREATE TABLE "
			+ TABLE_DOCTOR + "(" + KEY_ID_DOCTOR + " INTEGER" + " )";

	private static final String CREATE_DATA_TABLE = "CREATE TABLE "
			+ TABLE_DATA + "(" + KEY_ID_DATA + " INTEGER PRIMARY KEY,"
			+ KEY_DATANAME + " TEXT," + KEY_VALUE + " TEXT," + KEY_DATE
			+ " TEXT," + "" + KEY_ID_PATIENT + " INTEGER" + " )";
	
	private static final String CREATE_ALERT_TABLE = "CREATE TABLE "
			+ TABLE_ALERT + "(" + KEY_ID_ALERT + " INTEGER PRIMARY KEY,"
			+ KEY_ID_PATIENT + " INTEGER," + KEY_DATE + " TEXT," + KEY_ALERTNAME
			+ " TEXT" + " )";
	
	private static final String CREATE_SAVEDDATA_TABLE = "CREATE TABLE "
			+ TABLE_SAVEDDATA + "(" + KEY_ID_SAVEDDATA + " INTEGER PRIMARY KEY,"
			+ KEY_ID_ALERT + " INTEGER PRIMARY KEY" + KEY_DATANAME + " TEXT," + KEY_VALUE + " TEXT," + KEY_DATE
			+ " TEXT" + "" + " )";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_USER_TABLE);
		db.execSQL(CREATE_DATA_TABLE);
		db.execSQL(CREATE_PATIENT_TABLE);
		db.execSQL(CREATE_DOCTOR_TABLE);
		db.execSQL(CREATE_ALERT_TABLE);
		db.execSQL(CREATE_SAVEDDATA_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTOR);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALERT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVEDDATA);

		// Create tables again
		onCreate(db);
	}

	private int createUser(User object) {
		SQLiteDatabase db = this.getWritableDatabase();
		int userID;

		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, object.getUsername());
		values.put(KEY_PASSWORD, object.getPassword());
		values.put(KEY_TYPE, object.getType());
		// Inserting Row
		db.insert(TABLE_USERS, null, values);

		userID = retrieveUser(object.getUsername(), object.getPassword())
				.getIDUser();

		return userID;
	}

	public User retrieveUser(String username, String password) {
		SQLiteDatabase db = this.getReadableDatabase();
		User user = null;

		String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE "
				+ KEY_USERNAME + " = ?" + " AND " + KEY_PASSWORD + " = ?";

		Cursor cursor = db.rawQuery(selectQuery, new String[] { username,
				password });

		if (cursor.moveToFirst())
			user = new User(Integer.parseInt(cursor.getString(0)),
					cursor.getString(1), cursor.getString(2),
					cursor.getString(3));
		return user;
	}

	public User retrieveUser(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		User user = null;

		Cursor cursor = db.query(TABLE_USERS, null, KEY_ID_DATA + "=?",
				new String[] { Integer.toString(id) }, null, null, null, null);

		if (cursor.moveToFirst())
			user = new User(Integer.parseInt(cursor.getString(0)),
					cursor.getString(1), cursor.getString(2),
					cursor.getString(3));
		return user;
	}

	private int updateUser(User object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, object.getUsername());
		values.put(KEY_PASSWORD, object.getPassword());

		// updating row
		return db.update(TABLE_USERS, values, KEY_ID_DATA + " = ?",
				new String[] { Integer.toString(object.getIDUser()) });
	}

	private void deleteUser(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, KEY_ID_DATA + " = ?",
				new String[] { Integer.toString(id) });
		db.close();
	}

	public void deleteAllUser() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, null, null);
		db.close();
	}

	public boolean doesUserExist(String username) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE "
				+ KEY_USERNAME + " = ?";

		Cursor cursor = db.rawQuery(selectQuery, new String[] { username });

		return cursor.moveToFirst();
	}

	public void createData(Data object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATANAME, object.getDataname());
		values.put(KEY_VALUE, object.getValue());
		values.put(KEY_DATE, object.getDate().toString());
		values.put(KEY_ID_PATIENT, object.getIdPatient());

		// Inserting Row
		db.insert(TABLE_DATA, null, values);
	}
	
	public ArrayList<Data> retrieveDataList(String dataname) {
		SQLiteDatabase db = this.getReadableDatabase();
		ArrayList<Data> arraySavedData = new ArrayList<Data>();
		Data data = null;
		Date date = null;

		Cursor cursor = db.query(TABLE_DATA, null, KEY_DATANAME + "=?",
				new String[] { dataname }, null, null, null, null);

		if (cursor.moveToNext()) {
			try {
				date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(cursor.getString(2));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			data = new Data(cursor.getString(0),
					cursor.getString(1), date,
					Integer.parseInt(cursor.getString(3)));
			arraySavedData.add(data);
		}
		return arraySavedData;
	}

	public int updateData(Data object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATANAME, object.getDataname());
		values.put(KEY_VALUE, object.getValue());
		values.put(KEY_DATE, object.getDate().toString());

		// updating row
		return db.update(TABLE_DATA, values, KEY_ID_DATA + " = ?",
				new String[] { Integer.toString(object.getIDData()) });
	}

	public void deleteData(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DATA, KEY_ID_DATA + " = ?",
				new String[] { Integer.toString(id) });
		db.close();
	}

	public void deleteAllData() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DATA, null, null);
		db.close();
	}
	
	public int getNumberData() {
		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_DATA;
		int number = 0;
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		while (cursor.moveToNext())
			number++;
		
		return number;	
	}

	public void deleteLastData() {
		SQLiteDatabase db = this.getWritableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_DATA;
		
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst())
			db.delete(TABLE_DATA, KEY_ID_DATA + "=?",  new String[]{cursor.getString(cursor.getColumnIndex(KEY_ID_DATA))});
		db.close();
	}
	
	public void createPatient(Patient object) {
		SQLiteDatabase db = this.getWritableDatabase();
		int userID;

		userID = createUser((User) object);

		ContentValues values = new ContentValues();
		values.put(KEY_ID_PATIENT, userID);
		values.put(KEY_LASTNAME, object.getLastname());
		values.put(KEY_FIRSTNAME, object.getFirstname());
		values.put(KEY_ID_DOCTOR, object.getIDDoctor());

		// Inserting Row
		db.insert(TABLE_PATIENT, null, values);
	}

	public Patient retrievePatient(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Patient patient = null;
		User user = retrieveUser(id);

		Cursor cursor = db.query(TABLE_PATIENT, null, KEY_ID_PATIENT + "=?",
				new String[] { Integer.toString(id) }, null, null, null, null);

		if (cursor.moveToFirst())
			patient = new Patient(cursor.getString(1), cursor.getString(2),
					Integer.parseInt(cursor.getString(3)), user.getUsername(),
					user.getPassword());
		return patient;
	}

	public void createDoctor(Doctor object) {
		SQLiteDatabase db = this.getWritableDatabase();
		int userID;

		userID = createUser((User) object);

		ContentValues values = new ContentValues();
		values.put(KEY_ID_DOCTOR, userID);

		// Inserting Row
		db.insert(TABLE_DOCTOR, null, values);
	}

	public Doctor retrieveDoctor(int id) {
		SQLiteDatabase db = this.getReadableDatabase();
		Doctor doctor = null;
		User user = retrieveUser(id);

		Cursor cursor = db.query(TABLE_DOCTOR, null, KEY_ID_DOCTOR + "=?",
				new String[] { Integer.toString(id) }, null, null, null, null);

		if (cursor.moveToFirst())
			doctor = new Doctor(user.getUsername(), user.getPassword());
		return doctor;
	}
	
	public int createAlert (Alert object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID_ALERT, object.getIDAlert());
		values.put(KEY_ID_PATIENT, object.getIDPatient());
		values.put(KEY_DATE, object.getDate().toString());
		values.put(KEY_ALERTNAME, object.getAlertName());

		// Inserting Row
		db.insert(TABLE_ALERT, null, values);
		
		return retrieveLastIdAlert();
	}
	
	public Alert retrieveLastAlert () {
		SQLiteDatabase db = this.getReadableDatabase();
		Alert alert = null;
		Date date = null;
		
		Cursor cursor = db.query(TABLE_ALERT, null, KEY_ID_ALERT + "=?",
				new String[] { Integer.toString(retrieveLastIdAlert()) }, null, null, null, null);
		
		if (cursor.moveToFirst())
			try {
				date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(cursor.getString(2));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			alert = new Alert(Integer.parseInt(cursor.getString(1)), date, cursor.getString(3));
		
		return alert;
	}
	
	public int retrieveLastIdAlert() {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT " + KEY_ID_ALERT + " FROM " + TABLE_ALERT;

		Cursor cursor = db.rawQuery(selectQuery, null);

		return Integer.parseInt(cursor.getString(0));
	}
	
	public void createSavedData(Data object, int idAlert) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID_ALERT, Integer.toString(idAlert));
		values.put(KEY_DATANAME, object.getDataname());
		values.put(KEY_VALUE, object.getValue());
		values.put(KEY_DATE, object.getDate().toString());

		// Inserting Row
		db.insert(TABLE_SAVEDDATA, null, values);
	}
	
}
