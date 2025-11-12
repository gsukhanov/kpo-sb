package bank.commands.tojson;

import bank.models.Category;
import org.json.JSONObject;

public class CategoryToJson extends ToJson {
    Category category;

    public CategoryToJson(Category category) {this.category = category;}

    @Override
    public JSONObject execute() {
        JSONObject obj = new JSONObject();
        obj.accumulate("Id", category.getId());
        obj.accumulate("Type", category.getType());
        obj.accumulate("Name", category.getName());
        return obj;
    }

    public String getName() {
        return "Category to json conversion";
    }
}
