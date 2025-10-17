package com.example.zoo.models.people;


public class Visitor extends Person {

    public Visitor(String name, int age, int money) {
        super(name, age);
        this.money = money;
    }
}
