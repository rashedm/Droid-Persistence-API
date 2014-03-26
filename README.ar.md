Droid Persistence API
==================

ãŞÏãÉ
-----
ÇáåÏİ ãä ÇáãßÊÈÉ åæ ÇáãÓÇÚÏÉ İí ÓåæáÉ ÍİÙ ÈíÇäÇÊß æÇÓÊÑÌÇÚåÇ ÈÊŞäíÉ ORM

ÇáÊËÈíÊ
------
Şã ÈÊÍãíá ÇáãÔÑæÚ Ëã Şã ÈÇÓÊíÑÇÏå import Úä ØÑíŞ ÈÑäÇãÌ Eclipse æãä Ëã ÇÌÚáå ãßÊÈÉ áãÔÑæÚß ÇáÎÇÕ

ãËÇá
----
Şã ÈÅäÔÇÁ äæÚ class ÌÏíÏ ßÇáÊÇáí

    public class Person
    {
        @PrimaryKey
        @AutoIncrement
        int id;

        @Nullable
        String name;
    }
    
æÍíËãÇ ÇÑÏÊ ÍİÙ äÓÎÉ ãäå Úáíß İŞØ Úãá ÇáÊÇáí

    Persister persister = new Persister(getApplicationContext(),Person.class);
    Person person = new Person();
    person.name = "Rashed";
    persister.insert(person);
    
Êã ÍİÙ ÈíÇäÇÊ ÇáäÓÎÉ ÇáÂä æÅĞÇ ÇÑÏÊ ÇÓÊÑÌÇÚ ÇáÈíÇäÇÊ ÇáãÍİæÙÉ Úáíß ÈÇáÊÇáí
    
    ArrayList<Person> result = persister.select(Person.class).get();
    
ÃãÇ ÅĞÇ ÇÑÏÊ Çæá ŞíãÉ ãÓÊÎÑÌÉ

    Person result = (Person)persister.select(Person.class).first();
    
æÅĞÇ ÇÑÏÊ ŞíãÉ ÈãÚÑİ ãÍÏÏ

    Person result = (Person)persister.select(Person.class).byID(1).first();
    
ÏÚæÇÊßã áí ÈÇáÊæİíŞ
--