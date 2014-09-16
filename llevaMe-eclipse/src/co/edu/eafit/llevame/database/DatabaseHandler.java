package co.edu.eafit.llevame.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
	 
public class DatabaseHandler extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
 
    // Database Name
    public static final String DATABASE_NAME = "llevame.db";
 
    // Routes table name
    public static final String TABLE_ROUTES = "routes";
 
    // Routes Table Columns names
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_DATE = "date";
    public static final String KEY_HOUR = "hour";
    public static final String KEY_CAPACITY = "capacity";
    public static final String KEY_NUMBERPLATE = "numberPlate";
    public static final String KEY_DRAWROUTE = "drawRoute";
    public static final String KEY_DRAWPOINTS = "drawPoints";
    public static final String KEY_DETAILS = "details";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ROUTES_TABLE = "CREATE TABLE " + TABLE_ROUTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, "
                + KEY_DATE + " TEXT, " + KEY_HOUR + " TEXT, "
                + KEY_CAPACITY + " INTEGER, " + KEY_NUMBERPLATE + " TEXT, "
                + KEY_DRAWROUTE + " BLOB, " + KEY_DRAWPOINTS + " BLOB, "
                + KEY_DETAILS + " TEXT " + ")";
        db.execSQL(CREATE_ROUTES_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);
 
        // Create tables again
        onCreate(db);
    }
    
    public void fillWithTestingData(SQLiteDatabase db){
    	ContentValues values = new ContentValues();
    	values.put(KEY_NAME, "jhon doe");
    	values.put(KEY_DATE, "sep 20");
    	values.put(KEY_HOUR, "10:30");
    	values.put(KEY_CAPACITY, 3);
    	values.put(KEY_NUMBERPLATE, "XXX123");
    	
    	db.insert(TABLE_ROUTES, null, values);
    	
    	
    	values = new ContentValues();
    	values.put(KEY_NAME, "jane doe");
    	values.put(KEY_DATE, "sep 20");
    	values.put(KEY_HOUR, "10:30");
    	values.put(KEY_CAPACITY, 3);
    	values.put(KEY_NUMBERPLATE, "XXX123");
    	
    	db.insert(TABLE_ROUTES, null, values);
    }
}