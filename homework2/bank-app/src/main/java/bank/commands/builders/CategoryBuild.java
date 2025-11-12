package bank.commands.builders;

import bank.commands.interfaces.ICommand;
import bank.factories.CategoryFactory;
import bank.models.Category;
import bank.utils.CategoryType;

public class CategoryBuild implements ICommand {
    String name;
    CategoryType type;

    public CategoryBuild(CategoryType type, String name) {
        this.name = name;
        this.type = type;
    }

    @Override
    public Category execute() {
        return CategoryFactory.create(type, name);
    }

    public String getName() {
        return "New category creation";
    }
}
