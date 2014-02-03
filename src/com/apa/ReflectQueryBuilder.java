package com.apa;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.ContentValues;
import android.util.Log;

public class ReflectQueryBuilder {

	public static String getCreateQuery(Class<?> clazz) {
		StringBuilder query = new StringBuilder("create table ");
		query.append(clazz.getSimpleName());
		query.append("(");
		Field[] fields = clazz.getDeclaredFields();
		boolean first = true;
		for(Field field : fields) {
			String name = field.getName();
			String type = getType(field);
			String constraints = getConstraints(field);
			if(type != null) {
				if(!first) {
					query.append(", ");
				} else {
					first = false;
				}
				query.append(name);
				query.append(" ");
				query.append(type);
				query.append(" ");
				query.append(constraints);
			}
		}
		ArrayList<Field> rr = recursiveReferrings(clazz);
		for(Field loopBack : rr) {
			if(!first) {
				query.append(", ");
			} else {
				first = false;
			}
			String name = loopBack.getName();
			Field primaryField = getPrimaryField(loopBack.getType());
			String type = getType(primaryField);
			query.append(name);
			query.append(" ");
			query.append(type);
		}
		query.append(")");
		return query.toString();
	}
	
	public static String getDropQuery(Class<?> clazz) {
		StringBuilder query = new StringBuilder("DROP TABLE IF EXISTS ");
		query.append(clazz.getSimpleName());
		query.append("; ");
		return query.toString();
	}
	
	public static ArrayList<InValues> getInsertValues(Object object) {
		ContentValues cv = new ContentValues();
		Field[] fields = object.getClass().getDeclaredFields();
		ArrayList<InValues> inValues = new ArrayList<InValues>();
		InValues currentRow = new InValues();
		for(Field field : fields) {
			try {
				if(field.getType().getSimpleName().equals("int")) {
					if(field.isAnnotationPresent(PrimaryKey.class) || field.isAnnotationPresent(AutoIncrement.class)) {
						if(field.getInt(object) != 0) {
							cv.put(field.getName(), field.getInt(object));
						}
					} else {
						cv.put(field.getName(), field.getInt(object));
					}
				} else if(field.getType().getSimpleName().equals("String")) {
					Object value = field.get(object);
					if(value != null) {
						cv.put(field.getName(), value.toString());
					}
				} else {
					Object o = field.get(object);
					if(o != null) {
						ArrayList<InValues> rList = getInsertValues(field.get(object));
						inValues.addAll(rList);
						InValues rValue = rList.get(rList.size()-1);
						Field primaryField = getPrimaryField(rValue.getClazz());
						if(primaryField.getType().getSimpleName().equals("int")) {
							try{
								int value = rValue.getContentValues().getAsInteger(primaryField.getName());
								cv.put(field.getName(), value);
							} catch(NullPointerException npe) {
								rValue.setIDAutoIncrement(true);
								currentRow.addForeginKey(field.getName(),rValue);
							}
						} else if(primaryField.getType().getSimpleName().equals("String")) {
							String value = rValue.getContentValues().getAsString(primaryField.getName());
							cv.put(field.getName(), value);
						}
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		currentRow.setClazz(object.getClass());
		currentRow.setTableName(object.getClass().getSimpleName());
		currentRow.setContentValues(cv);
		inValues.add(currentRow);
		return inValues;
	}
	
	private static String getType(Field field) {
		Class<?> type = field.getType();
		if(type.getName() != null && type.getName().equals("int")) {
			return "INTEGER";
		} else if(type.getName() != null && type.getName().equals("java.lang.String")) {
			return "text";
		}
		return null;
	}
	
	private static String getConstraints(Field field) {
		StringBuilder constraints = new StringBuilder();
		boolean nullable = field.isAnnotationPresent(Nullable.class);
		boolean primaryKey = field.isAnnotationPresent(PrimaryKey.class) || field.isAnnotationPresent(AutoIncrement.class);
		boolean autoIncrement = field.isAnnotationPresent(AutoIncrement.class);
		if(!nullable && !autoIncrement) {
			constraints.append("not null");
		}
		if(primaryKey) {
			if(!nullable) {
				constraints.append(" ");
				constraints.append("primary key");
			}
			// TODO: Throw an exception
		}
		if(autoIncrement) {
			constraints.append(" ");
			constraints.append("AUTOINCREMENT");
		}
		return constraints.toString();
	}
	
	static ArrayList<String> getComplexFieldsTypeNames(Class<?> c) {
		ArrayList<String> complexFields = new ArrayList<String>();
		Field[] fields = c.getDeclaredFields();
		for(Field field : fields) {
			String fieldType = field.getType().getSimpleName();
			if(!fieldType.equals("int") && !fieldType.equals("String")) {
				complexFields.add(field.getType().getName());
			}
		}
		return complexFields;
	}
	
	static ArrayList<Field> getComplexFields(Class<?> c) {
		ArrayList<Field> complexFields = new ArrayList<Field>();
		Field[] fields = c.getDeclaredFields();
		for(Field field : fields) {
			String fieldType = field.getType().getSimpleName();
			if(!fieldType.equals("int") && !fieldType.equals("String")) {
				complexFields.add(field);
			}
		}
		return complexFields;
	}
	
	private static ArrayList<Field> recursiveReferrings(Class<?> clazz) {
		ArrayList<Field> complexFields = getComplexFields(clazz);
		ArrayList<Field> rr = new ArrayList<Field>();
		for(Field cf : complexFields) {
			rr.add(cf);
		}
		return rr;
	}
	
	public static Field getPrimaryField(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			if(field.isAnnotationPresent(PrimaryKey.class)||field.isAnnotationPresent(AutoIncrement.class)) {
				return field;
			}
		}
		return null;
	}
	
	static Field[] getFields(Class<?> clazz) {
		return clazz.getDeclaredFields();
	}
}
