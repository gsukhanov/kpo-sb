
package zoo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import zoo.models.animals.*;
import zoo.models.items.Computer;
import zoo.models.items.Table;
import zoo.models.people.Caretaker;
import zoo.models.people.Cleaner;
import zoo.models.people.Manager;
import zoo.services.VetClinic;
import zoo.services.Zoo;
import zoo.services.factories.AnimalFactory;
import zoo.services.factories.PeopleFactory;
import zoo.services.factories.ThingFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ZooCoreTests {

    private Zoo zoo;

    @BeforeEach
    void setUp() {
        zoo = new Zoo(new VetClinic(), new AnimalFactory(), new PeopleFactory(), new ThingFactory());
        zoo.setTicketCost(200);
        zoo.setFoodCost(5);
        zoo.setPowerCost(1);
    }

    // --- Reflection helpers to inspect private state safely in tests ---
    @SuppressWarnings("unchecked")
    private <T> T getField(Object target, String name, Class<T> type) {
        try {
            Field f = target.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return (T) f.get(target);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setIntField(Object target, String name, int value) {
        try {
            Field f = target.getClass().getDeclaredField(name);
            f.setAccessible(true);
            f.setInt(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("tryAddAnimal: healthy animal is added with inventory number")
    void addAnimalHealthy() {
        Capybara capy = new Capybara("Kapy", 3, 8);
        capy.setHealth(100); // ensure deterministic healthy

        zoo.tryAddAnimal(capy);

        List<Animal> animals = getField(zoo, "animals", List.class);
        assertEquals(1, animals.size());
        assertEquals("Kapy", animals.get(0).getName());
        assertTrue(animals.get(0).getNumber() >= 1);
        assertEquals(3, animals.get(0).getFood());
    }

    @Test
    @DisplayName("tryAddAnimal: unhealthy goes to clinic, healNext returns and then added")
    void addAnimalUnhealthyThenHeal() {
        Rabbit r = new Rabbit("Bunny", 2, 7);
        r.setHealth(30); // force unhealthy
        zoo.tryAddAnimal(r);

        List<Animal> animals = getField(zoo, "animals", List.class);
        assertEquals(0, animals.size(), "Unhealthy animal should not be added immediately");

        // Heal and re-add via tryHealAnimal
        zoo.tryHealAnimal();

        animals = getField(zoo, "animals", List.class);
        assertEquals(1, animals.size(), "After healing, animal should be added");
        assertEquals(100, animals.get(0).getHealth());
    }

    @Test
    @DisplayName("tryHireWorker: adds worker, decrements openPositions, sets hours")
    void hireWorkerWithPositions() {
        zoo.tryHireWorker(Cleaner.class, "Alice", 22);

        List<?> workers = getField(zoo, "workers", List.class);
        assertEquals(1, workers.size());
        int openPositions = getField(zoo, "openPositions", Integer.class);
        assertEquals(19, openPositions);
        // Check hours via Worker getters
        zoo.models.people.Worker w = (zoo.models.people.Worker) workers.get(0);
        assertEquals(9, w.getStartHour());
        assertEquals(18, w.getEndHour());
        assertEquals("Alice", w.getName());
    }

    @Test
    @DisplayName("tryHireWorker: no open positions -> not added")
    void hireWorkerNoPositions() {
        setIntField(zoo, "openPositions", 0);
        zoo.tryHireWorker(Manager.class, "Bob", 35);

        List<?> workers = getField(zoo, "workers", List.class);
        assertEquals(0, workers.size());
    }

    @Test
    @DisplayName("tryEnterVisitor: adult pays full ticket")
    void enterVisitorAdult() {
        int initialBudget = getField(zoo, "budget", Integer.class);
        int initialProfit = getField(zoo, "profit", Integer.class);

        zoo.tryEnterVisitor("Carl", 25);

        int budget = getField(zoo, "budget", Integer.class);
        int profit = getField(zoo, "profit", Integer.class);
        assertEquals(initialBudget + 200, budget);
        assertEquals(initialProfit + 200, profit);

        List<?> visitors = getField(zoo, "visitors", List.class);
        assertEquals(1, visitors.size());
    }

    @Test
    @DisplayName("tryEnterVisitor: minor pays half ticket")
    void enterVisitorMinor() {
        zoo.setTicketCost(160);
        int initialBudget = getField(zoo, "budget", Integer.class);
        int initialProfit = getField(zoo, "profit", Integer.class);

        zoo.tryEnterVisitor("Dana", 12);

        int budget = getField(zoo, "budget", Integer.class);
        int profit = getField(zoo, "profit", Integer.class);
        assertEquals(initialBudget + 80, budget);
        assertEquals(initialProfit + 80, profit);
    }

    @Test
    @DisplayName("tryBuyDevice: enough budget -> inventory grows and budget decreases")
    void buyDeviceEnoughBudget() {
        zoo.tryBuyDevice(Computer.class);

        List<?> inventory = getField(zoo, "inventory", List.class);
        assertFalse(inventory.isEmpty(), "Inventory should contain the bought device");
        int budget = getField(zoo, "budget", Integer.class);
        // Computer costs 10000
        assertEquals(1000000 - 10000, budget);
        // Inventory numbers should start at 1
        zoo.models.items.Thing t = (zoo.models.items.Thing) inventory.get(0);
        assertTrue(t.getNumber() >= 1);
    }

    @Test
    @DisplayName("tryBuyFurniture: insufficient budget -> no purchase")
    void buyFurnitureInsufficient() {
        // Set a tiny budget to force failure
        setIntField(zoo, "budget", 100);
        zoo.tryBuyFurniture(Table.class);

        List<?> inventory = getField(zoo, "inventory", List.class);
        assertTrue(inventory.isEmpty(), "No items should be added to inventory");
        int budget = getField(zoo, "budget", Integer.class);
        assertEquals(100, budget, "Budget remains unchanged on failed purchase");
    }

    @Test
    @DisplayName("printReport prints computed totals (sanity check via captured output)")
    void printReportTotals() {
        // Prepare deterministic state
        Capybara capy = new Capybara("Capy", 3, 9); capy.setHealth(100);
        Tiger tiger = new Tiger("Tig", 5); tiger.setHealth(100);
        zoo.tryAddAnimal(capy);
        zoo.tryAddAnimal(tiger);

        // Buy one powered device and one furniture
        zoo.tryBuyDevice(Computer.class); // power = 2, cost = 10000
        // Ensure at least one visitor for profit calc
        zoo.setTicketCost(150);
        zoo.tryEnterVisitor("Eve", 30);

        // Hire one cleaner (wage 400, hours 9-18 => 9*400=3600)
        zoo.tryHireWorker(Cleaner.class, "Frank", 28);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream old = System.out;
        System.setOut(new PrintStream(baos));
        try {
            zoo.printReport();
        } finally {
            System.out.flush();
            System.setOut(old);
        }
        String out = baos.toString();

        assertTrue(out.contains("Всего животных: 2"));
        assertTrue(out.contains("Всего требуется еды: 8")); // 3 + 5
        assertTrue(out.contains("Всего электрических устройств: 1"));
        assertTrue(out.contains("Всего затрат на электроэнергию: 2")); // powerCost = 1 * totalPower(=2)
        assertTrue(out.contains("Заработано денег на билетах: 150"));
        assertTrue(out.contains("Всего затрат на зарплаты: 3600"));
    }
}
