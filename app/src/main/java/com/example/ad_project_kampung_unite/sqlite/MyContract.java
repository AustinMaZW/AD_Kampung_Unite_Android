package com.example.ad_project_kampung_unite.sqlite;

import android.provider.BaseColumns;

public class MyContract {

    public static final String DATABASE_NAME = "sqlite";
    public static final int DATABASE_VERSION = 1;

    public class Employee {
        public static final String TABLE_NAME = "employee";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SALARY = "salary";
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME +
                        " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME + " VARCHAR(255)," +
                        COLUMN_SALARY + " float" +
                        ")";
        public static final String SQL_DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
    }

    public class Department {
        public static final String TABLE_NAME = "department";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME +
                        " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        COLUMN_NAME + " VARCHAR(255)" +
                        ")";
        public static final String SQL_DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
    }
}
