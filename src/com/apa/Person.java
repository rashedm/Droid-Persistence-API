package com.apa;

public class Person {

	@PrimaryKey
	@AutoIncrement
	int id;
	@Nullable
	int age;
	String name;
	Person wife1;
	Car car;
	
	@Override
	public String toString() {
		String w = "";
		if(wife1 != null) {
			w = wife1.toString();
		}
		String c = "";
		if(car != null) {
			c = car.toString();
		}
		return "ID: "+id+", Age: "+age+", Name: "+name+", Wife:("+w+"), Car: ("+c+")";
	}
}
