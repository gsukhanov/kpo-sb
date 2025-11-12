package bank.models;

import bank.facades.CategoryFacade;
import bank.factories.CategoryFactory;
import bank.utils.CategoryType;

public class Category {
    int id;
    CategoryType type;
    String name;


    public Category(int id, CategoryType type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public int getId() {
        return id;
    }
    public CategoryType getType() {
        return type;
    }
    public String getName() {
        return name;
    }
}
