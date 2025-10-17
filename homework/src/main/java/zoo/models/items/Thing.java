package com.example.zoo.models.items;

import com.example.zoo.interfaces.IInventory;

public abstract class Thing implements IInventory {
    int mass;
    int volume;
    int cost;
    int number;

    @Override
    public int getNumber() {
        return this.number;
    }

    public int getMass() {
        return this.mass;
    }

    public int getVolume() {
        return this.volume;
    }

    public int getCost() {
        return this.cost;
    }

    @Override
    public void setNumber(int number) {
        this.number = number;
    }
}
