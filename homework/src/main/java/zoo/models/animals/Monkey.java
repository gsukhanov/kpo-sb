package com.example.zoo.models.animals;

public class Monkey extends Herbo {
    public Monkey(String name, int food, int kindness) {
        super(name, food, kindness);
    }

    @Override
    public boolean isHealthy() {
        return health > 80;
    }
}
