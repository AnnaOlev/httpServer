package com.company;

public class Dog {
    private int id;
    private String name;
    private float age;
    private char sex;
    private String breed;

    public Dog(int id, String name, String breed, char sex, float age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.breed = breed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAge() {
        return age;
    }

    public void setAge(float age) {
        this.age = age;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", breed='" + breed + '\'' +
                '}';
    }
}
