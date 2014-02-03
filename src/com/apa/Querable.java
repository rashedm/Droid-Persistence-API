package com.apa;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Querable {

	private String table;
	protected HashMap<String,Object> where;
	private StringBuilder query;
	protected Class<?> clazz;
	protected Integer limit;
	
	public Querable() {
		query = new StringBuilder();
		where = new HashMap<String, Object>();
	}
	
	public Querable select(Class<?> clazz) {
		table = clazz.getSimpleName();
		this.clazz = clazz;
		return this;
	}
	
	public Querable byID(String s) {
		where.put("ID", s);
		return this;
	}
	
	public Querable byID(Integer i) {
		where.put("ID", i);
		return this;
	}
	
	public abstract ArrayList get();
	
	protected String selectQuery() {
		query = new StringBuilder();
		query.append("select * from ");
		query.append(table);
		boolean first = true;
		for(String key : where.keySet()) {
			if(first) {
				query.append(" where ");
				first = false;
			}
			if(key.equals("ID")) {
				Field field = ReflectQueryBuilder.getPrimaryField(clazz);
				query.append(field.getName());
				query.append(" = ");
			}
			Object o = where.get(key);
			if(o instanceof Integer) {
				Integer i = (Integer) o;
				query.append(i);
			} else if(o instanceof String) {
				String s = (String)o;
				query.append(s);
			}
		}
		if(limit != null) {
			query.append(" ");
			query.append("limit ");
			query.append(limit);
		}
		return query.toString();
	}

	public abstract Object first();
}
