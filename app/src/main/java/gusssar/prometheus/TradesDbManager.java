package gusssar.prometheus;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;

class TradesDbManager extends SQLiteOpenHelper {
    //private static TradesDbManager sInstance;

    //если изменить версию, то выполняем то, что в onUpgrade()
    private static final int DATABASE_VERSION = 1;
    public static Context myContext;
    public static String
            LOG_TAG = "TradesDbManager",
            DATABASE_NAME = "prometheus.db";
    String[] TABLE_DATA;
    Integer TABLE_TRADE_ID;
    String  TABLE_TYPE;
    Double  TABLE_PRICE;
    Double  TABLE_QUANTITY;
    Double  TABLE_AMOUNT;
    Integer TABLE_DATE;

    //private static final String
    //        LOG_TAG = "TradesDbManager",
    //        DATABASE_NAME = "mydata.db",
    //        TABLE_DATA = "data",
    //        TABLE_DATA_ID = "id",
    //        TABLE_DATA_KEY = "key",
    //        TABLE_DATA_VALUE= "value";
    //public static final String
    //        PRICE = "price",
    //        PHOTO = "photo",
    //        TRUE = "true",
    //        FALSE = "false";

    //public static synchronized TradesDbManager getInstance(Context context) {
    //    if (sInstance == null) sInstance = new TradesDbManager(context);
    //    return sInstance;
    //}

    // конструктор суперкласса
    //private TradesDbManager(Context context) {
    public TradesDbManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        myContext = context;
    }

    //@Override
    //public void onConfigure(SQLiteDatabase db) {
    //    super.onConfigure(db);
    //    db.setForeignKeyConstraintsEnabled(true);
    //}

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
            TABLE_DATA = myContext.getResources().getStringArray(R.array.pairListForLink);
            for (int i = 0; i <= 52; i++) {
                String CREATE_TABLE_DATA = "CREATE TABLE " + TABLE_DATA[i] +
                        "(" +
                        "TABLE_TRADE_ID         INTEGER PRIMARY KEY," +
                        "TABLE_TYPE             TEXT," +
                        "TABLE_PRICE            REAL," +
                        "TABLE_QUANTITY         REAL," +
                        "TABLE_AMOUNT           REAL," +
                        "TABLE_DATE             INTEGER" +
                        ")";
                db.execSQL(CREATE_TABLE_DATA);
            }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     //  if (oldVersion != newVersion) {
     //      //удалим текущую версию базы со всеми данными
     //      db.execSQL("DROP TABLE IF EXISTS " + TABLE_DATA);
     //      //создадим новую базу
     //      onCreate(db);
     //  }
    }

   // public static long set(String key, String value) {
   //     SQLiteDatabase db = sInstance.getWritableDatabase();
   //     db.beginTransaction();
   //     long result = -1;
       // try {
       //     ContentValues values = new ContentValues();
       //     values.put(TABLE_DATA_KEY, key);
       //     values.put(TABLE_DATA_VALUE, value);
       //     int rows = db.update(TABLE_DATA, values, TABLE_DATA_KEY + "= ?", new String[]{key});
       //     if (rows == 1) {
       //         String settingSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?", TABLE_DATA_ID, TABLE_DATA, TABLE_DATA_KEY);
       //         Cursor cursor = db.rawQuery(settingSelectQuery, new String[]{String.valueOf(key)});
       //         try {
       //             if (cursor.moveToFirst()) {
       //                 result = cursor.getInt(0);
       //                 db.setTransactionSuccessful();
       //             }
       //         } finally {
       //             if (cursor != null && !cursor.isClosed()) {
       //                 cursor.close();
       //             }
       //         }
       //     } else {
       //         result = db.insertOrThrow(TABLE_DATA, null, values);
       //         db.setTransactionSuccessful();
       //     }
       // } catch (Exception e) {
       //     Log.d(LOG_TAG, e.getMessage());
       // } finally {
       //     db.endTransaction();
       // }
    //   return result;
    //}

    //public static String get(String key) {
    //    String result = "";
    //    final SQLiteDatabase db = sInstance.getReadableDatabase();
    //    final Cursor cursor = db.rawQuery(String.format("SELECT %s FROM %s WHERE %s = ?", TABLE_DATA_VALUE, TABLE_DATA, TABLE_DATA_KEY), new String[]{String.valueOf(key)});
    //    try {
    //        if (cursor.moveToFirst()) {
    //            do {
    //                result = cursor.getString(cursor.getColumnIndex(TABLE_DATA_VALUE));
    //            } while (cursor.moveToNext());
    //        }
    //    } catch (Exception e) {
    //        Log.e(LOG_TAG, e.getMessage());
    //    } finally {
    //        if (cursor != null && !cursor.isClosed()) {
    //            cursor.close();
    //        }
    //    }
    //    return result == null || result.equals("") || result.equals("null") ? null : result;
    //}

}
