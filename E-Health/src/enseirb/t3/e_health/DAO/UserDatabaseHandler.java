package enseirb.t3.e_health.DAO;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import enseirb.t3.e_health.entity.User;

public class UserDatabaseHandler extends SQLiteOpenHelper implements GenericDatabaseHandler<User>{

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "E-health";

	// Beacons table name
	private static final String TABLE_USERS = "Users";

	// Beacons Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";

	public UserDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_BEACONS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME +  " TEXT," + KEY_PASSWORD +  " TEXT"+ ")";
		db.execSQL(CREATE_BEACONS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

		// Create tables again
		onCreate(db);
	}

	@Override
	public void create(User object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, object.getUsername()); 
		values.put(KEY_PASSWORD, object.getPassword()); // Beacon Mac Address

		// Inserting Row
		db.insert(TABLE_USERS, null, values);
		db.close(); // Closing database connection
	}

	@Override
	public User retrive(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_USERNAME,
				KEY_PASSWORD}, KEY_ID + "=?",
				new String[] { Integer.toString(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		User user = new User();
		user.setUsername(cursor.getString(0));
		user.setPassword(cursor.getString(1));
		user.setId(id);

		return user;
	}

	@Override
	public int update(User object) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_USERNAME, object.getUsername());
		values.put(KEY_PASSWORD, object.getPassword());

		// updating row
		return db.update(TABLE_USERS, values, KEY_ID + " = ?",
				new String[] { Integer.toString(object.getId()) });
	}

	@Override
	public void delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_USERS, KEY_ID + " = ?",
				new String[] { Integer.toString(id)});
		db.close();
	}

	public boolean isUser(String username, String password){
		SQLiteDatabase db = this.getReadableDatabase();

		String selectQuery = "SELECT * FROM " + TABLE_USERS + " LIMIT 1";

		Cursor cursor = db.rawQuery(selectQuery, null);

		return cursor.moveToFirst();	
	}
}
