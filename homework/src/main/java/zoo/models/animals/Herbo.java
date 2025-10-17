package com.example.zoo.models.animals;

public abstract class Herbo extends Animal {
    int kindness;

    public Herbo(String name, int food, int kindness) {
        super(name, food);
        this.kindness = kindness;
        if (kindness > 10) throw new IllegalArgumentException("Kindness can't be greater than 10.");
    }

    public boolean isFriendly() {
        return (kindness > 5);
    }

    public int getKindness() {
        return kindness;
    }

}
