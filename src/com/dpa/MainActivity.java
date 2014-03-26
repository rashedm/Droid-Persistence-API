package com.dpa;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Persister persister = new Persister(getApplicationContext(),Person.class);
        Person per = new Person();
        per.name = "Test";
        per.age = 10;
        per.wife1 = new Person();
        per.wife1.name = "Testah";
        per.car = new Car("Accord",2007);
        //persister.insert(per);
        ArrayList<Person> result = persister.select(Person.class).get();
        for(Person p : result) {
        	Log.i("Person",p.toString());
        }
    }
}
