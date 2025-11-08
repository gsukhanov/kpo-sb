package models;

import enums.CategoryType;
import interfaces.ICategory;

public class Category implements ICategory {
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
