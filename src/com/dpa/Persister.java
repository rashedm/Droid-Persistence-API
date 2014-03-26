package com.dpa;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Persister extends Querable {

	private DatabaseHelper dbHelper;
	private Context context;
	
	public Persister(Context context,Class clazz) {
		this.context = context;
		this.clazz = clazz;
	}
	
	private Persister() {
		// just to ensure it's never called
	}
	
	public void insert(Object object) {
		dbHelper = new DatabaseHelper(context, object.getClass());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ArrayList<InValues> inValues = ReflectQueryBuilder.getInsertValues(object);
		for(InValues inValue : inValues) {
			if(!inValue.isIDAutoIncrement()) {
				if(inValue.getForeginKey().size() > 0) {
					for(String foreginKey : inValue.getForeginKey().keySet()) {
						inValue.getContentValues().put(foreginKey, inValue.getForeginKey().get(foreginKey).getIncrementedValue());
					}
					db.insert(inValue.getTableName(), null, inValue.getContentValues());
				} else {
					db.insert(inValue.getTableName(), null, inValue.getContentValues());
				}
			} else {
				int insertID = 0;
				if(inValue.getForeginKey().size() > 0) {
					for(String foreginKey : inValue.getForeginKey().keySet()) {
						inValue.getContentValues().put(foreginKey, inValue.getForeginKey().get(foreginKey).getIncrementedValue());
					}
					insertID = (int)db.insert(inValue.getTableName(), null, inValue.getContentValues());
				} else {
					insertID = (int)db.insert(inValue.getTableName(), null, inValue.getContentValues());
				}
				inValue.setIncrementedValue(insertID);
			}
		}
		db.close();
	}
	
	@Override
	public ArrayList get() {
		String query = selectQuery();
		dbHelper = new DatabaseHelper(context, clazz);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query, null);
		ArrayList result = get(mCursor);
		db.close();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList get(Cursor mCursor) {
		if(mCursor == null) {
			return null;
		}
		ArrayList result = new ArrayList();
		Field[] fields = ReflectQueryBuilder.getFields(clazz);
		Class<?> thisClazz = clazz;
		try {
			while(mCursor.moveToNext()) {
				Object o = clazz.newInstance();
				for(Field field : fields) {
					if(field.getType().getSimpleName().equals("String")) {
						field.set(o, mCursor.getString(mCursor.getColumnIndex(field.getName())));
					} else if(field.getType().getSimpleName().equals("int")) {
						field.set(o, mCursor.getInt(mCursor.getColumnIndex(field.getName())));
					} else {
						if(!mCursor.isNull(mCursor.getColumnIndex(field.getName()))) {
							Field pField = ReflectQueryBuilder.getPrimaryField(field.getType());
							if(pField != null && pField.getType().getSimpleName().equals("int")) {
								int value = mCursor.getInt(mCursor.getColumnIndex(field.getName()));
								Persister p = new Persister(this.context,field.getType());
								field.set(o, p.select(field.getType()).byID(value).first());
							} else if(pField != null && pField.getType().getSimpleName().equals("String")) {
								String value = mCursor.getString(mCursor.getColumnIndex(field.getName()));
								Persister p = new Persister(this.context,field.getType());
								field.set(o, p.select(field.getType()).byID(value).first());
							}
						}
					}
				}
				result.add(o);
				//clazz = thisClazz; 
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		mCursor.close();
		return result;
	}
	
	public Object first() {
		limit = 1;
		String query = selectQuery();
		dbHelper = new DatabaseHelper(context, clazz);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor mCursor = db.rawQuery(query, null);
		ArrayList result = get(mCursor);
		db.close();
		return result.get(0);
	}
}
