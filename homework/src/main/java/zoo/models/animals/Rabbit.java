package com.example.zoo.models.animals;

public class Rabbit extends Herbo {
    public Rabbit(String name, int food, int kindness) {
        super(name, food, kindness);
    }

    @Override
    public boolean isHealthy() {
        return this.health > 90;
    }
}
