package xcong.eatfish;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.util.ArrayList;

/**
 * Created by 聪 on 2016/10/25.
 */
public class DBbHelper {
    // 数据库名
    public final static String DATABASE_NAME = "MyData.db";
    // 数据库版本
    public static int DATABASE_VERSION = 1;
    // 表名
    public final static String TABLE_NAME = "person";
    // 表中的字段
    public final static String PERSON_NAME = "name"; //名字
    public final static String PERSON_SCORE = "score";//号码

    private SQLiteDatabase db;
    private DBOpenHelper helper;

    private static class DBOpenHelper extends SQLiteOpenHelper {
        private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + PERSON_NAME + " TEXT PRIMARY KEY,"
                + PERSON_SCORE + " TEXT " +");";
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
    public DBbHelper(Context context){
        helper = new DBOpenHelper(context);
        db =helper.getWritableDatabase();
    }
    public void insert(People person) {
		/* ContentValues */
        ContentValues cv = new ContentValues();

        cv.put(PERSON_NAME,person.getName());
        cv.put(PERSON_SCORE,person.getScore());

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
        cv.put(PERSON_SCORE,person.getScore());

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
                String score = cursor.getString(cursor
                        .getColumnIndex(PERSON_SCORE));

                People item = new People(name,score);
                items.add(item);
                cursor.moveToNext();
            }
        }
        return items;
    }
}
