Droid Persistence API
==================

مقدمة
-----
الهدف من المكتبة هو المساعدة في سهولة حفظ بياناتك واسترجاعها بتقنية ORM

التثبيت
------
قم بتحميل المشروع ثم قم باستيراده import عن طريق برنامج Eclipse ومن ثم اجعله مكتبة لمشروعك الخاص

مثال
----
قم بإنشاء نوع class جديد كالتالي

    public class Person
    {
        @PrimaryKey
        @AutoIncrement
        int id;

        @Nullable
        String name;
    }
    
وحيثما اردت حفظ نسخة منه عليك فقط عمل التالي

    Persister persister = new Persister(getApplicationContext(),Person.class);
    Person person = new Person();
    person.name = "Rashed";
    persister.insert(person);
    
تم حفظ بيانات النسخة الآن وإذا اردت استرجاع البيانات المحفوظة عليك بالتالي
    
    ArrayList<Person> result = persister.select(Person.class).get();
    
أما إذا اردت اول قيمة مستخرجة

    Person result = (Person)persister.select(Person.class).first();
    
وإذا اردت قيمة بمعرف محدد

    Person result = (Person)persister.select(Person.class).byID(1).first();
    
دعواتكم لي بالتوفيق
--