package zoo.interfaces.factories;

import zoo.models.animals.Herbo;
import zoo.models.animals.Predator;

public interface AnimalFactory {
    Herbo createHerbivore(Class<? extends Herbo> type);
    Predator createCarnivore(Class<? extends Predator> type);
}

