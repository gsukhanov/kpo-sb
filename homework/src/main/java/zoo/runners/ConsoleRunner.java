package com.example.zoo.runners;

import com.example.zoo.models.animals.Animal;
import com.example.zoo.models.animals.Capybara;
import com.example.zoo.models.animals.Rabbit;
import com.example.zoo.models.animals.Tiger;
import com.example.zoo.services.Zoo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsoleRunner implements CommandLineRunner {

    private final Zoo zoo;

    public ConsoleRunner(Zoo zoo) {
        this.zoo = zoo;
    }

    @Override
    public void run(String... args) {
        Animal tiger = new Tiger("Шерхан", 10);
        Animal rabbit = new Rabbit("Кролик", 3, 7);
        Animal capybara = new Capybara("Барик", 8, 12);

        zoo.tryAddAnimal(tiger);
        zoo.tryAddAnimal(rabbit);
        zoo.tryAddAnimal(capybara);

        zoo.printReport();


    }
}
