package bank.commands.adders;

import bank.commands.interfaces.ICommand;
import bank.facades.CategoryFacade;
import bank.models.Category;

public class CategoryAdd implements ICommand {
    Category category;

    public CategoryAdd(Category category) {this.category = category;}

    public Category execute() {
        CategoryFacade.addCategory(category);
        return category;
    }

    public String getName() {
        return "Category database addition";
    }
}
