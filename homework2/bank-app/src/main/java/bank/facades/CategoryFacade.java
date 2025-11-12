package bank.facades;

import bank.models.Category;
import org.springframework.stereotype.Component;

import java.util.TreeMap;

public enum CategoryFacade {
    INSTANCE();

    final TreeMap<String, Category> categories;

    CategoryFacade() {this.categories = new TreeMap<>();}
    public static Category findCategory(String name) {
        return INSTANCE.categories.get(name);
    }
    public static void addCategory(Category category) {
        INSTANCE.categories.put(category.getName(), category);
    }
    public static void removeCategory(String name) {
        INSTANCE.categories.remove(name);
    }
    public static TreeMap<String, Category> getAll() {
        return INSTANCE.categories;
    }
}
