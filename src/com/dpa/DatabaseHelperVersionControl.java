package com.dpa;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelperVersionControl extends SQLiteOpenHelper {

	private static String mainCreateStatement;
	
	DatabaseHelperVersionControl(Context context) {
        super(context, "VersionControl", null, 1);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table version ( table_name text not null primary key, version int not null,create_statement text not null)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
	
	
}
