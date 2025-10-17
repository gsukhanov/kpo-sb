package com.example.zoo.models.animals;

public class Wolf extends Predator {
    public Wolf(String name, int food) {
        super(name, food);
    }

    @Override
    public boolean isHealthy() {
        return this.health > 70;
    }
}
