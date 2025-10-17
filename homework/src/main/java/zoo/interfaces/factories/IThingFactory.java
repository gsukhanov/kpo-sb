package zoo.interfaces.factories;

import zoo.models.items.Device;
import zoo.models.items.Furniture;

public interface ThingFactory {
    Furniture createFurniture(Class<? extends Furniture> type);
    Device createDevice(Class<? extends Device> type);
}
