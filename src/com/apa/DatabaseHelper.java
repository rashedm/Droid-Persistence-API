package com.apa;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private Class<?> clazz;
	private static SQLiteDatabase versionDB;
	private Context context;
	
	DatabaseHelper(Context context, Class<?> clazz) {
        super(context, "APA", null, incrementVersion(context,clazz));
        this.clazz = clazz;
        this.context = context;
    }
	
	private static int incrementVersion(Context context,Class<?> clazz) {
		String createMainTableQuery = ReflectQueryBuilder.getCreateQuery(clazz);
		return updateVersion(context,clazz,createMainTableQuery);
	}
	
	public static int updateVersion(Context context, Class<?> clazz,String createMainTableQuery) {
		String[] tableVersion = getTableVersion(context,clazz);
		if(tableVersion == null) {
			ContentValues initialValues = new ContentValues();
	        initialValues.put("table_name", clazz.getSimpleName());
	        initialValues.put("version", 1);
	        initialValues.put("create_statement", createMainTableQuery);
	        versionDB.insert("version", null, initialValues);
	        versionDB.close();
	        return 1;
		}
        if(!tableVersion[2].equals(createMainTableQuery)) {
        	ContentValues initialValues = new ContentValues();
            initialValues.put("create_statement", createMainTableQuery);
            initialValues.put("version", Integer.parseInt(tableVersion[1])+1);
            versionDB.update("version",initialValues,"table_name='"+clazz.getSimpleName()+"'",null);
        	versionDB.close();
        	return Integer.parseInt(tableVersion[1])+1;
        }
        return Integer.parseInt(tableVersion[1]);
	}
	
	private static String[] getTableVersion(Context context,Class<?> clazz) {
		DatabaseHelperVersionControl versionControlHelper = new DatabaseHelperVersionControl(context);
		versionDB = versionControlHelper.getWritableDatabase();
		Cursor mCursor = versionDB.rawQuery("select * from version where table_name = '"+clazz.getSimpleName()+"'", null);
		String[] version = null;
		if(mCursor!= null && mCursor.getCount() > 0) {
    		mCursor.moveToNext();
    		version = new String[3];
    		version[0] = mCursor.getString(mCursor.getColumnIndex("table_name"));
    		version[1] = mCursor.getInt(mCursor.getColumnIndex("version"))+"";
    		version[2] = mCursor.getString(mCursor.getColumnIndex("create_statement"));
    	}
    	mCursor.close();
    	return version;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("onCreate","Yes they called it :)");
		ArrayList<String> createdTables = new ArrayList<String>();
		String dropTableQuery = ReflectQueryBuilder.getDropQuery(this.clazz);
		String createMainTableQuery = ReflectQueryBuilder.getCreateQuery(this.clazz);
		updateVersion(context, clazz, createMainTableQuery);
		db.execSQL(dropTableQuery);
		db.execSQL(createMainTableQuery);
		createdTables.add(this.clazz.getName());
		ArrayList<String> cxf = ReflectQueryBuilder.getComplexFieldsTypeNames(clazz);
		for(String fieldType : cxf) {
			if(createdTables.contains(fieldType)) {
				continue;
			}
			try {
				String dropQuery = ReflectQueryBuilder.getDropQuery(Class.forName(fieldType));
				String createQuery = ReflectQueryBuilder.getCreateQuery(Class.forName(fieldType));
				updateVersion(context, Class.forName(fieldType), createQuery);
				db.execSQL(dropQuery);
				db.execSQL(createQuery);
				createdTables.add(fieldType);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
	}
	
	
}
