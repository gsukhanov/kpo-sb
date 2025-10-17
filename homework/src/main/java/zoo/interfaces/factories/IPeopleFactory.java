package zoo.interfaces.factories;

import zoo.models.people.Visitor;
import zoo.models.people.Worker;

public interface PersonFactory {
    Visitor createVisitor();

    Worker createWorker(Class<? extends Worker> type);
}
