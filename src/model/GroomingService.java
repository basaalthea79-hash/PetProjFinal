package model;

public final class GroomingService {
    private final String name;
    private final int price;
    private final int minutes;
    private static final GroomingService[] SERVICES = {
        new GroomingService("Basic Bath", 350, 45),
        new GroomingService("Full Groom (Bath + Haircut)", 600, 90),
        new GroomingService("De-shedding Treatment", 500, 75),
        new GroomingService("Spa Package", 850, 120)
    };
    private static final GroomingService[] ADD_ONS = {
        new GroomingService("Nail Trimming", 100, 10),
        new GroomingService("Teeth Brushing", 120, 10),
        new GroomingService("Ear Cleaning", 100, 10),
        new GroomingService("Perfume Spritz", 80, 5)
    };
    public GroomingService(String name, int price, int minutes) {
        this.name = name;
        this.price = price;
        this.minutes = minutes;
    }
    
    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
    public int getMinutes() {
        return minutes;
    }
    public String getDescription() {
        return name + " (" + minutes + " min)";
    }
    
    @Override
    public String toString() {
        return name + " - P" + price;
    }
    public static GroomingService[] getServices() {
        return SERVICES.clone();
    }
    public static GroomingService[] getAddOns() {
        return ADD_ONS.clone();
    }
    public static GroomingService findService(String name) {
        return find(SERVICES, name);
    }
    public static GroomingService findAddOn(String name) {
        return find(ADD_ONS, name);
    }

    private static GroomingService find(GroomingService[] list, String name) {
        for (GroomingService service : list) {
            if (service.getName().equals(name)) {
                return service;
            }
        }
        return null;
    }
}