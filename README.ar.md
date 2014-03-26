Droid Persistence API
==================

�����
-----
����� �� ������� �� �������� �� ����� ��� ������� ���������� ������ ORM

�������
------
�� ������ ������� �� �� ��������� import �� ���� ������ Eclipse ��� �� ����� ����� ������� �����

����
----
�� ������ ��� class ���� �������

    public class Person
    {
        @PrimaryKey
        @AutoIncrement
        int id;

        @Nullable
        String name;
    }
    
������ ���� ��� ���� ��� ���� ��� ��� ������

    Persister persister = new Persister(getApplicationContext(),Person.class);
    Person person = new Person();
    person.name = "Rashed";
    persister.insert(person);
    
�� ��� ������ ������ ���� ���� ���� ������� �������� �������� ���� �������
    
    ArrayList<Person> result = persister.select(Person.class).get();
    
��� ��� ���� ��� ���� �������

    Person result = (Person)persister.select(Person.class).first();
    
���� ���� ���� ����� ����

    Person result = (Person)persister.select(Person.class).byID(1).first();
    
������� �� ��������
--