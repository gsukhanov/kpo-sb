package bank.commands.fromjson;

import bank.factories.CategoryFactory;
import bank.models.Category;
import bank.utils.CategoryType;
import org.json.JSONException;
import org.json.JSONObject;

public class CategoryFromJson extends FromJson {

    public CategoryFromJson(JSONObject obj) {super(obj);}

    public Category execute() {
        try {int id = obj.getInt("Id");
            CategoryType type = null;
            if (obj.getString("Type").equals("Gain")) type = CategoryType.GAIN;
            if (obj.getString("Type").equals("Loss")) type = CategoryType.LOSS;
            String name = obj.getString("Name");
            return CategoryFactory.create(id, type, name);
        } catch (JSONException e) {
            throw new IllegalArgumentException("Bank Account JSON parsing error: field unknown");
        }
    }

    public String getName() {
        return "Extraction of category from json";
    }
}
