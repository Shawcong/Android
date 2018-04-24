package yjf.baidumap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;

/**
 * Created by 聪 on 2016/10/25.
 */
public class DBcHelper {
    // 数据库名
    public final static String DATABASE_NAME = "MyData2.db";
    // 数据库版本
    public static int DATABASE_VERSION = 1;
    // 表名
    public final static String TABLE_NAME = "person";
    // 表中的字段
    public final static String PERSON_NAME = "name"; //名字
    public final static String PERSON_NUM = "num";//号码
    public final static String PERSON_LAT = "lat";//纬度
    public final static String PERSON_LON = "lon";//经度
    public final static String PERSON_HIG = "hig";//海拔
    public final static String PERSON_NEAR = "near";//所在地点
    public final static String PERSON_UPDATE = "last";//离上一次更新时间

    private SQLiteDatabase db;
    private DBOpenHelper helper;

    private static class DBOpenHelper extends SQLiteOpenHelper {
        private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + PERSON_NAME + " TEXT PRIMARY KEY,"
                + PERSON_NUM + " TEXT ," + PERSON_LAT + " TEXT," + PERSON_LON + " TEXT,"+ PERSON_HIG
                + " TEXT," + PERSON_NEAR + " TEXT," + PERSON_UPDATE+" TEXT"+");";
        public DBOpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
            String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
            db.execSQL(sql);
            onCreate(db);
        }
    }
    public DBcHelper(Context context){
        helper = new DBOpenHelper(context);
        db =helper.getWritableDatabase();
    }
    public void insert(People person) {
		/* ContentValues */
        ContentValues cv = new ContentValues();

        cv.put(PERSON_NAME,person.getName());
        cv.put(PERSON_NUM,person.getNumber());
        cv.put(PERSON_LAT,person.getLatitude());
        cv.put(PERSON_LON,person.getLongitude());
        cv.put(PERSON_HIG,person.getAltitude());
        cv.put(PERSON_NEAR,person.getNear());
        cv.put(PERSON_UPDATE,person.getTime_update());

        db.insert(TABLE_NAME, null, cv);
    }
    // 删除数据操作
    public boolean delete(String name) {
        String where = PERSON_NAME + " = ?";
        String[] whereValue = { name };
        if (db.delete(TABLE_NAME, where, whereValue) > 0) {
            return true;
        }
        return false;

    }

    // 更新数据操作
    public boolean update(People person) {
        ContentValues cv = new ContentValues();
        cv.put(PERSON_NUM,person.getNumber());
        cv.put(PERSON_LAT,person.getLatitude());
        cv.put(PERSON_LON,person.getLongitude());
        cv.put(PERSON_HIG,person.getAltitude());
        cv.put(PERSON_NEAR,person.getNear());
        cv.put(PERSON_UPDATE,person.getTime_update());

        if (db.update(TABLE_NAME, cv, PERSON_NAME + "=?",
                new String[] { person.getName() }) > 0) {
            return true;
        }
        db.close();
        return false;
    }

    // 获取所有数据
    public ArrayList<People> getAllData() {
        ArrayList<People> items = new ArrayList<People>();
        Cursor cursor = db.rawQuery("SELECT * FROM person", null);

        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor
                        .getColumnIndex(PERSON_NAME));
                String number = cursor.getString(cursor
                        .getColumnIndex(PERSON_NUM));
                String lat = cursor.getString(cursor
                        .getColumnIndex(PERSON_LAT));
                String lon = cursor.getString(cursor
                        .getColumnIndex(PERSON_LON));
                String hig = cursor.getString(cursor
                        .getColumnIndex(PERSON_HIG));
                String near = cursor.getString(cursor
                        .getColumnIndex(PERSON_NEAR));
                String update = cursor.getString(cursor
                        .getColumnIndex(PERSON_UPDATE));

                People item = new People(name,number,lat,lon,hig,near,update);
                items.add(item);
                cursor.moveToNext();
            }
        }
        return items;
    }
}
