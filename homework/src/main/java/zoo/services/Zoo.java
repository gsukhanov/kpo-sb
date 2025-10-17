package com.example.zoo.services;

import com.example.zoo.models.animals.Animal;
import com.example.zoo.models.animals.Herbo;
import com.example.zoo.models.people.Visitor;
import com.example.zoo.models.people.Worker;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Zoo {
    private final VetClinic vetClinic;
    private final List<Animal> animals = new ArrayList<>();
    private final List<Visitor> visitors = new ArrayList<>();
    private final List<Worker> workers = new ArrayList<>();
    private int nextInventoryNumber = 1;
    private int ticketCost = 200;
    private int budget = 1000000;
    private int foodCost = 5;
    private int profit = 0;

    public Zoo(VetClinic vetClinic) {
        this.vetClinic = vetClinic;
    }

    public void tryAddAnimal(Animal animal) {
        if (vetClinic.checkHealth(animal)) {
            animal.setNumber(nextInventoryNumber++);
            animals.add(animal);
            System.out.println(animal.getName() + " принят в зоопарк!");
        } else {
            System.out.println(animal.getName() + " отклонён по состоянию здоровья.");
        }
    }

    public void setTicketCost(int ticketCost) {
        this.ticketCost = ticketCost;
    }

    public void setFoodCost(int foodCost) {
        this.foodCost = foodCost;
    }

    public void tryEnterVisitor(Visitor visitor) {
        if (visitor.getMoney() >= ticketCost && visitor.isHealthy()) {
            visitor.pay(ticketCost);
            budget += ticketCost;
            profit += ticketCost;
            visitors.add(visitor);
            System.out.println(visitor.getName() + " впущен в зоопарк!");
        } else {
            System.out.println(visitor.getName() + "не может заплатить за билет.");
        }
    }

    public void printReport() {
        System.out.println("\n=== Отчёт по животным ===");
        int totalFood = animals.stream().mapToInt(Animal::getFood).sum();
        System.out.println("Всего животных: " + animals.size());
        System.out.println("Всего еды в день: " + totalFood + " кг");
        System.out.println("Всего затрат на еду в день:" + totalFood * foodCost + " руб");

        System.out.println("\nЖивотные для контактного зоопарка:");
        animals.stream()
            .filter(a -> a instanceof Herbo && ((Herbo)a).isFriendly())
            .forEach(a -> System.out.println("- " + a.getName()));


        System.out.println("\n=== Отчёт по инвентарю ===");

        System.out.println("\n=== Отчёт по посетителям ===");
        visitors.stream().map(Visitor::getName).forEach(System.out::println);
        System.out.println("Всего посетителей: " + visitors.size());

        System.out.println("\n=== Отчёт по бюджету ===");


        System.out.println("Заработано денег на билетах: " + profit + " руб");
        System.out.println();
    }
}
