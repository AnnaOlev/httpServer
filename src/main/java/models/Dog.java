package models;

import javax.persistence.*;

@Entity (name = "dog")
@Table (name = "dog")
public class Dog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "breed")
    private String breed;
    @Column(name = "sex")
    private char sex;
    @Column (name = "age")
    private int age;

    public Dog(){
    }

    public int getId() {
        return id;
    }

    public Dog(int id, String name, String breed, char sex, int age) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.sex = sex;
        this.age = age;
    }

    public Dog(String name, String breed, char sex, int age) {
        this.name = name;
        this.breed = breed;
        this.sex = sex;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "DogDB{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", sex=" + sex +
                ", age=" + age +
                '}';
    }

    public String responseToString(){
        return id + "&" + name + "&" + breed + "&" + sex + "&" + age + "&";
    }
}
