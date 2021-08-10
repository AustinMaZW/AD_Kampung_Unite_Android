package com.example.ad_project_kampung_unite.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ad_project_kampung_unite.sqlite.MyContract;
import com.example.ad_project_kampung_unite.sqlite.MyDBHelper;

public class EmployeeAdapter {
    MyDBHelper dbHelper;

    public EmployeeAdapter(Context context) {
        dbHelper = new MyDBHelper(context);
    }

    /*
     * Parameterized Query
     */
    public long insert(String name, String salary) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("salary", salary);

        // Insert the new row using Parameterized Query, returning the primary key value of the new row
        long newRowId = db.insert(MyContract.Employee.TABLE_NAME, null, values);
        return newRowId;
    }

    /*
     * Raw Query
     */
    public void insertRaw(String name, String salary) {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String query = "INSERT INTO " + MyContract.Employee.TABLE_NAME +
                "(" + MyContract.Employee.COLUMN_NAME + ", " + MyContract.Employee.COLUMN_SALARY + ")" +
                " VALUES (?,?)";

        // Insert the new row using Raw Query
        // While using raw queries we never come to know the result of operation
        db.execSQL(query, new String[] { name, salary });
    }

    public String getData() {
        // Gets the data repository in read mode
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Define an array that specifies which columns you want to fetch
        // null will give all
        String[] columns = { MyContract.Employee.COLUMN_ID, MyContract.Employee.COLUMN_NAME, MyContract.Employee.COLUMN_SALARY };

        // Filter results WHERE "salary" > 2000
        //String selection = MyContract.Employee.COLUMN_SALARY + " > ?";
        //String[] selectionArgs = { "2000" };

        // Sorting order of the results in the resulting Cursor
        //String sortOrder = MyContract.Employee.COLUMN_SALARY + " DESC";

        // Limits of results
        int limit = 5;

        // Fetch the desired columns based on our selection criteria and results are returned in a Cursor object
        Cursor cursor =db.query(MyContract.Employee.TABLE_NAME, columns,null,null,null,null,null, String.valueOf(limit));

        StringBuffer buffer= new StringBuffer();

        if (cursor != null) {
            // Since the cursor starts at position -1, calling moveToNext() places the "read position" on the first entry in the results
            // and returns whether or not the cursor is already past the last entry in the result set
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(MyContract.Employee.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(MyContract.Employee.COLUMN_NAME));
                float salary = cursor.getFloat(cursor.getColumnIndex(MyContract.Employee.COLUMN_SALARY));
                buffer.append(id + "   " + name + "   " + salary + " \n");
            }
            cursor.close();
        }

        return buffer.toString();
    }

    public int update(int id, String name, String salary) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("salary", salary);

        String whereClause = "id=?";
        String whereArgs[] = { String.valueOf(id) };

        // Return number of affected rows if success, 0 otherwise
        int noOfRow = db.update(MyContract.Employee.TABLE_NAME, values, whereClause, whereArgs);
        return noOfRow;
    }

    public int delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String whereClause = "id=?";
        String whereArgs[] = { String.valueOf(id) };

        // Return number of affected rows if whereClause passed, 0 otherwise
        int noOfRow = db.delete(MyContract.Employee.TABLE_NAME, whereClause, whereArgs);
        return noOfRow;
    }

    public void closeHelper() {
        dbHelper.close();
    }
}
