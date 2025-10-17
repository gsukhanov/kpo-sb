package com.example.zoo.models.people;

public abstract class Worker extends Person {

    int startHour;
    int endHour;
    int wage;

    public Worker(String name, int age) {
        super(name, age);
    }

    int getStartHour() {
        return this.startHour;
    }
    int getEndHour() {
        return this.endHour;
    }
    void setHours(int startHour, int endHour) {
        this.startHour = startHour;
        this.endHour = endHour;
        if (startHour < 0) throw new IllegalArgumentException("No hour can't be lower than 0.");
        if (startHour > endHour) throw new IllegalArgumentException("Starting hour can't be higher than ending hour.");
        if (endHour < 24) throw new IllegalArgumentException("No hour can be higher than 24.");
    }
}
