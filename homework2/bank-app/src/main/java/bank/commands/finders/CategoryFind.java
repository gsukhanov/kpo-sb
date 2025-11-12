package bank.commands.finders;

import bank.commands.interfaces.ICommand;
import bank.facades.CategoryFacade;
import bank.models.Category;

public class CategoryFind implements ICommand {
    String name;

    public CategoryFind(String name) {this.name = name;}

    @Override
    public Category execute() {
        return CategoryFacade.findCategory(name);
    }

    @Override
    public String getName() {
        return "Category search";
    }
}
