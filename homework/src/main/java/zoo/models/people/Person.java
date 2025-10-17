package com.example.zoo.models.people;

import com.example.zoo.interfaces.IAlive;

public abstract class Person implements IAlive {
    String name;
    int age;
    int health;
    int money;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        this.money = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {

    }

    @Override
    public boolean isHealthy() {
        return health > 65;
    }

    public int getMoney() {
        return this.money;
    }

    public void getPaid(int check) {
        this.money += check;
    }

    public void pay(int cost) {
        this.money -= cost;
    }
}
