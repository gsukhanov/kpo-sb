package com.example.zoo.models.animals;

public class Tiger extends Predator {
    public Tiger(String name, int food) {
        super(name, food);
    }

    @Override
    public boolean isHealthy() {
        return this.health > 60;
    }
}
