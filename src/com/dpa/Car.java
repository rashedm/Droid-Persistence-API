package com.dpa;

import com.dpa.datatypes.AutoIncrement;
import com.dpa.datatypes.PrimaryKey;

public class Car {

	@AutoIncrement
	@PrimaryKey
	int id;
	int model;
	String name;
	
	public Car(String name,int model) {
		this.model = model;
		this.name = name;
	}
	
	public Car() {
		
	}
	
	public String toString() {
		return "ID: "+id+", Model: "+model+", Name: "+name;
	}
	
	public int getModel() {
		return model;
	}
	public void setModel(int model) {
		this.model = model;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
