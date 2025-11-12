package bank.factories;

import bank.models.Category;
import bank.utils.CategoryType;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

public enum CategoryFactory {
    INSTANCE();

    int id = 0;

    CategoryFactory() {}
    public static Category create(CategoryType type, String name) {
        INSTANCE.id++;
        return new Category(INSTANCE.id, type, name);
    }

    public static Category create(int id, CategoryType type, @NotNull String name) {
        INSTANCE.id = id;
        return new Category(id, type, name);
    }
}
