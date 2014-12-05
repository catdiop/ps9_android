package enseirb.t3.e_health.DAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import enseirb.t3.e_health.entity.Data;
import enseirb.t3.e_health.entity.User;

public class DatabaseHandler extends SQLiteOpenHelper implements
		GenericDatabaseHandler<User> {

	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "E-health";

	// Users table name
	private static final String TABLE_USERS = "Users";
	// Data table name
	private static final String TABLE_DATA = "Data";

	// Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_TYPE = "type";
	private static final String KEY_DATANAME = "dataname";
	private static final String KEY_VALUE = "value";
	private static final String KEY_DATE = "date";
	private static final String CREATE_USER_TABLE = "CREATE TABLE "
			+ TABLE_USERS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_USERNAME + " TEXT," + KEY_PASSWORD + " TEXT," + KEY_TYPE
			+ " TEXT )";

	private static final String CREATE_DATA_TABLE = "CREATE TABLE "
			+ TABLE_DATA + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_DATANAME + " TEXT," + KEY_VALUE + " TEXT," + KEY_DATE
			+ " TEXT )";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_USER_TABLE);
		db.execSQL(CREATE_DATA_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);

		// Create tables again
		onCreate(db);
	}

	@Override
	public void createUser(User object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, object.getUsername());
		values.put(KEY_PASSWORD, object.getPassword());
		values.put(KEY_TYPE, object.getType());
		// Inserting Row
		db.insert(TABLE_USERS, null, values);
		db.close(); // Closing database connection
	}

	@Override
	public User retrieveUser(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_USERNAME,
				KEY_PASSWORD }, KEY_ID + "=?",
				new String[] { Integer.toString(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		User user = new User(cursor.getString(0), cursor.getString(1));
		user.setId(id);

		return user;
	}

	@Override
	public int updateUser(User object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, object.getUsername());
		values.put(KEY_PASSWORD, object.getPassword());

		// updating row
		return db.update(TABLE_USERS, values, KEY_ID + " = ?",
				new String[] { Integer.toString(object.getId()) });
	}

	@Override
	public void deleteUser(int id) {
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

	public boolean isUser(String username, String password) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE "
				+ KEY_USERNAME + " = ?" + " AND " + KEY_PASSWORD + " = ?";

		Cursor cursor = db.rawQuery(selectQuery, new String[] { username,
				password });

		// db.close();

		return cursor.moveToFirst();
	}

	public boolean doesUserExist(String username) {
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE "
				+ KEY_USERNAME + " = ?";

		Cursor cursor = db.rawQuery(selectQuery, new String[] { username });

		db.close();

		return cursor.moveToFirst();
	}

	public boolean isUserADoctor(String username) {

		String type = "";

		boolean isDoctor = false;

		SQLiteDatabase db = this.getReadableDatabase();
		String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE "
				+ KEY_USERNAME + " = '" + username + "' ";
		Cursor cursorOnType = db.rawQuery(selectQuery, null);
		if (cursorOnType != null)
			cursorOnType.moveToFirst();

		type = cursorOnType.getString(3);

		if (type.equals("doctor")) {
			isDoctor = true;
		}
		db.close();
		return isDoctor;
	}

	public void createData(Data object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATANAME, object.getDataname());
		values.put(KEY_VALUE, object.getValue());
		values.put(KEY_DATE, object.getDate().toString());

		// Inserting Row
		db.insert(TABLE_DATA, null, values);
		db.close(); // Closing database connection
	}

	public List<Data> retrieveDataList(String dataname) {
		SQLiteDatabase db = this.getReadableDatabase();
		Data data;
		List<Data> dataList = new LinkedList<Data>();

		String selectQuery = "select " + KEY_DATANAME + "," + KEY_VALUE + ","
				+ KEY_DATE + " from " + TABLE_DATA + " where " + KEY_DATANAME
				+ " = ?";

		Cursor cursor = db.rawQuery(selectQuery, new String[] { dataname });

		while (cursor.moveToNext()) {

			Date date = null;
			try {
				date = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
						.parse(cursor.getString(2));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			data = new Data(cursor.getString(0), cursor.getString(1), date);
			dataList.add(data);

		}
		db.close();
		return dataList;
	}

	public Data retrieveLastData(String dataname) {
		SQLiteDatabase db = this.getReadableDatabase();
		Data data = null;
		Date date;

		String selectQuery = "select " + KEY_DATANAME + "," + KEY_VALUE + ","
				+ KEY_DATE + " from " + TABLE_DATA + " where " + KEY_DATANAME
				+ " = ?";

		Cursor cursor = db.rawQuery(selectQuery, new String[] { dataname });
		cursor.moveToFirst();

		// try {
		// date = new
		// SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(cursor.getString(2));
		date = new Date();
		data = new Data(cursor.getString(0), cursor.getString(1), date);
		// } catch (ParseException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// db.close();
		return data;
	}

	public int updateData(Data object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_DATANAME, object.getDataname());
		values.put(KEY_VALUE, object.getValue());
		values.put(KEY_DATE, object.getDate().toString());

		db.close();

		// updating row
		return db.update(TABLE_DATA, values, KEY_ID + " = ?",
				new String[] { Integer.toString(object.getId()) });
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
}
