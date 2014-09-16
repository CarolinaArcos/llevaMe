package co.edu.eafit.llevame.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
	 
public class DatabaseHandler extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "llevameDB";
 
    // Routes table name
    private static final String TABLE_ROUTES = "routes";
 
    // Routes Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_HOUR = "hour";
    private static final String KEY_CAPACITY = "capacity";
    private static final String KEY_NUMBERPLATE = "numberPlate";
    private static final String KEY_DRAWROUTE = "drawRoute";
    private static final String KEY_DRAWPOINTS = "drawPoints";
    private static final String KEY_DETAILS = "details";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ROUTES_TABLE = "CREATE TABLE " + TABLE_ROUTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DATE + " TEXT" + KEY_HOUR + " TEXT,"
                + KEY_CAPACITY + " INTEGER" + KEY_NUMBERPLATE + " TEXT,"
                + KEY_DRAWROUTE + " BLOB" + KEY_DRAWPOINTS + " BLOB,"
                + KEY_DETAILS + " TEXT" + ")";
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
}