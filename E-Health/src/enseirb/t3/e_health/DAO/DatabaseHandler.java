package enseirb.t3.e_health.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

	// Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_ID_PATIENT = "idPatient";
	private static final String KEY_ID_DOCTOR = "idDoctor";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_TYPE = "type";
	private static final String KEY_DATANAME = "dataname";
	private static final String KEY_VALUE = "value";
	private static final String KEY_DATE = "date";
	private static final String KEY_LASTNAME = "lastname";
	private static final String KEY_FIRSTNAME = "firstname";

	private static final String CREATE_USER_TABLE = "CREATE TABLE "
			+ TABLE_USERS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_USERNAME + " TEXT," + KEY_PASSWORD + " TEXT," + KEY_TYPE
			+ " TEXT )";

	private static final String CREATE_PATIENT_TABLE = "CREATE TABLE "
			+ TABLE_PATIENT + "(" + KEY_ID_PATIENT + " INTEGER,"
			+ KEY_FIRSTNAME + " TEXT," + KEY_LASTNAME + " TEXT,"
			+ KEY_ID_DOCTOR + " INTEGER" + " )";

	private static final String CREATE_DOCTOR_TABLE = "CREATE TABLE "
			+ TABLE_DOCTOR + "(" + KEY_ID_DOCTOR + " INTEGER" + " )";

	private static final String CREATE_DATA_TABLE = "CREATE TABLE "
			+ TABLE_DATA + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_DATANAME + " TEXT," + KEY_VALUE + " TEXT," + KEY_DATE
			+ " TEXT," + "" + KEY_ID_PATIENT + " INTEGER" + " )";

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
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTOR);

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

		Cursor cursor = db.query(TABLE_USERS, null, KEY_ID + "=?",
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
		return db.update(TABLE_USERS, values, KEY_ID + " = ?",
				new String[] { Integer.toString(object.getIDUser()) });
	}

	private void deleteUser(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, KEY_ID + " = ?",
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

	public int updateData(Data object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATANAME, object.getDataname());
		values.put(KEY_VALUE, object.getValue());
		values.put(KEY_DATE, object.getDate().toString());

		// updating row
		return db.update(TABLE_DATA, values, KEY_ID + " = ?",
				new String[] { Integer.toString(object.getIDData()) });
	}

	public void deleteData(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DATA, KEY_ID + " = ?",
				new String[] { Integer.toString(id) });
		db.close();
	}

	public void deleteAllData() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DATA, null, null);
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
}
