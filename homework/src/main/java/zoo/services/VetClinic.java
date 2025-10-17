package com.example.zoo.services;

import org.springframework.stereotype.Component;
import com.example.zoo.models.animals.Animal;

import java.util.LinkedList;
import java.util.Random;

@Component
public class VetClinic {

    private final LinkedList<Animal> sickAnimals = new LinkedList<>();

    public boolean checkHealth(Animal animal) {
        if (animal.isHealthy()) {
            return true;
        } else {
            sickAnimals.add(animal);
            return false;
        }
    }

    public Animal healNext() {
        Animal animal = sickAnimals.poll();
        if (animal == null) System.out.println("Лечить больше некого!");
        return animal;
    }
}
