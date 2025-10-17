package com.example.zoo.models.animals;

public class Capybara extends Herbo {
    public Capybara(String name, int food, int kindness) {
        super(name, food, kindness);
    }

    @Override
    public boolean isHealthy() {
        return this.health > 50;
    }
}
