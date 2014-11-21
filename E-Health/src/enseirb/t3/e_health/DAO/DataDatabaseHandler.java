package enseirb.t3.e_health.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import enseirb.t3.e_health.entity.Data;

public class DataDatabaseHandler extends SQLiteOpenHelper implements GenericDatabaseHandler<Data>{

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "E-health";

	// Data table name
	private static final String TABLE_DATA = "Data";

	// Data Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_DATANAME = "dataname";
	private static final String KEY_VALUE = "value";
	private static final String KEY_DATE = "date";

	public DataDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_DATA_TABLE = "CREATE TABLE " + TABLE_DATA + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_DATANAME +  " TEXT," + KEY_VALUE +  " TEXT," + KEY_DATE + " TEXT " + ")";
		db.execSQL(CREATE_DATA_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);

		// Create tables again
		onCreate(db);
	}

	@Override
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

	@Override
	public Data retrive(int id) {
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

	@Override
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

	@Override
	public void delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DATA, KEY_ID + " = ?",
				new String[] { Integer.toString(id)});
		db.close();
	}
	
	public void deleteAllData() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DATA, null, null);
		db.close();
	}
}
