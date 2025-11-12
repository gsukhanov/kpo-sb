package bank.commands.removers;

import bank.commands.interfaces.ICommand;
import bank.commands.interfaces.Invoker;
import bank.facades.CategoryFacade;
import bank.facades.OperationFacade;
import bank.models.Category;
import bank.models.Operation;

public class CategoryRemove implements ICommand {
    Category category;

    public CategoryRemove(Category category) {this.category = category;}

    public Integer execute() {
        for (var pair : OperationFacade.getAll().entrySet()) {
            if (pair.getValue().getCategory().equals(category))
                Invoker.remove(pair.getValue()).execute();
        }
        CategoryFacade.removeCategory(category.getName());
        return 0;
    }

    public String getName() {
        return "Category removal";
    }
}
