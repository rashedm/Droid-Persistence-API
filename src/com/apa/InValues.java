package com.apa;

import java.util.HashMap;

import android.content.ContentValues;

public class InValues {
	private ContentValues contentValues;
	private String tableName;
	private Class<?> clazz;
	private boolean isIDAutoIncrement;
	private int incrementedValue;
	private HashMap<String,InValues> foreginKey;
	
	public InValues() {
		foreginKey = new HashMap<String, InValues>();
	}
	
	public int getIncrementedValue() {
		return incrementedValue;
	}

	public void setIncrementedValue(int incrementedValue) {
		this.incrementedValue = incrementedValue;
	}

	public HashMap<String,InValues> getForeginKey() {
		return foreginKey;
	}
	
	public void addForeginKey(String name, InValues value) {
		this.foreginKey.put(name, value);
	}

	public void setForeginKey(HashMap<String,InValues> foreginKey) {
		this.foreginKey = foreginKey;
	}

	public boolean isIDAutoIncrement() {
		return isIDAutoIncrement;
	}

	public void setIDAutoIncrement(boolean isIDAutoIncrement) {
		this.isIDAutoIncrement = isIDAutoIncrement;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	InValues(String tableName,ContentValues contentValues, Class<?> clazz) {
		this.contentValues = contentValues;
		this.tableName = tableName;
		this.clazz = clazz;
	}
	
	public ContentValues getContentValues() {
		return contentValues;
	}
	public void setContentValues(ContentValues contentValues) {
		this.contentValues = contentValues;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	
}
