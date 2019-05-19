package com.nyderek.dawid.audiometria;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Audiograms";
    private final String table_name = "exam_results";

    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    String formattedDate;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
        Log.d("DBHelper", "Database created successfully");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + table_name + " (id integer primary key, first_name text, last_name text, exam_date text, result blob)");
        Log.d("DBHelper", "Table created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + table_name);
    }

    void insertData(ResultModel resultModel){
        formattedDate = df.format(c.getTime());
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("first_name", resultModel.getFirstName());
        contentValues.put("last_name", resultModel.getLastName());
        contentValues.put("exam_date", formattedDate);
        contentValues.put("result", resultModel.getAudiogram());
        database.insert(table_name, null, contentValues);
        Log.d("DBHelper", "Data inserted succesfully");
    }

    List<ResultModel> selectData(){
        List<ResultModel> allResultList = new ArrayList<>();
        SQLiteDatabase database = this.getReadableDatabase();
        String[] columns = {"id", "first_name", "last_name", "exam_date", "result"};
        Cursor cursorResults = database.query(table_name, columns, null, null, null, null, null);

        if (cursorResults.moveToFirst()) {
            do {
                allResultList.add(new ResultModel(
                        cursorResults.getInt(0),
                        cursorResults.getString(1),
                        cursorResults.getString(2),
                        cursorResults.getString(3),
                        cursorResults.getBlob(4)
                ));
            } while (cursorResults.moveToNext());
        }
        cursorResults.close();
        Log.d("DBHelper", "Data read succesfully");
        return allResultList;
    }
}
