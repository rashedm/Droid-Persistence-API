Droid Persistence API
=====================

Introduction
------------
It's an Android library that will help you to persists your beans into an SQLite database.

Installation
-----------
Download the project and import it using Eclipse as an existing Android project then mark it down as a library in your project.

Example
-------
Create a bean which you will use in your project for example "Person"

    public class Person
    {
        @PrimaryKey
        @AutoIncrement
	    int id;
        
        @Nullable
        String name;
    }
Then whenever you want to save an instance of this bean you could esily do the following

    Persister persister = new Persister(getApplicationContext(),Person.class);
    Person person = new Person();
    person.name = "Rashed";
    persister.insert(person);

Now the instance will be saved into the SQLite database.
if you want to retrive the list of this instance you could write the following:

    ArrayList<Person> result = persister.select(Person.class).get();
    
or if you want the first result

    Person result = (Person)persister.select(Person.class).first();
    
and whenever you wanted the specific instance by ID which is the primary key

    Person result = (Person)persister.select(Person.class).byID(1).first();