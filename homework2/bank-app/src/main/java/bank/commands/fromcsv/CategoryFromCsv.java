package bank.commands.fromcsv;

import bank.factories.CategoryFactory;
import bank.models.Category;
import bank.utils.CategoryType;

public class CategoryFromCsv extends FromCsv {

    public CategoryFromCsv(String line) {super(line);}

    public Category execute() {
        String[] separated = line.split(";");
        int id = Integer.parseInt(separated[0]);
        CategoryType type;
        if (separated[1].equals("Gain")) type = CategoryType.GAIN;
        else if (separated[1].equals("Loss")) type = CategoryType.LOSS;
        else throw new IllegalArgumentException ("Incorrect category type. Allowed types are \"Gain\" and \"Loss\"\n");
        String name = separated[2];
        return CategoryFactory.create(id, type, name);
    }

    public String getName() {
        return "Extraction of category from csv";
    }
}
