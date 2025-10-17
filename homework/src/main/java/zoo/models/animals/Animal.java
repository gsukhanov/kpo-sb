package com.example.zoo.models.animals;

import com.example.zoo.interfaces.IAlive;
import com.example.zoo.interfaces.IInventory;

import java.util.Random;

public abstract class Animal implements IAlive, IInventory
{

    protected String name;
    protected int food;
    protected int number;
    protected int health;
    protected int mass;

    public Animal(String name, int food) {
        this.name = name;
        this.food = food;
        this.health = new Random().nextInt(101);
    }

    @Override
    public String getName() {
        return this.name;
    }
    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public int getNumber() { return number; }

    @Override
    public void setNumber(int number) { this.number = number; }

    public int getFood() {
        return this.food;
    }

    @Override
    public int getMass() {
        return this.mass;
    }

    public void setFood(int food) {
        this.food = food;
    }
}
