package bank.commands.tocsv;

import bank.models.Category;

public class CategoryToCsv extends ToCsv{
    Category category;

    public CategoryToCsv(Category category) {this.category = category;}

    public String execute() {
        return category.getId() + ";" + category.getType() + ";" + category.getName() + "\n";
    }

    public String getName() {
        return "Category to csv conversion";
    }
}
