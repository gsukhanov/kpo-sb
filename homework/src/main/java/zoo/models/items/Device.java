package com.example.zoo.models.items;

public abstract class Device extends Thing {
    int power;
    boolean isPowered;
    void OnOff() {
        this.isPowered = !isPowered;
    }
}
