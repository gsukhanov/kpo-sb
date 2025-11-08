package interfaces;

import enums.CategoryType;

public interface ICategory {
    int getId();
    CategoryType getType();
    String getName();
}
