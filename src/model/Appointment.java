package model;
import java.util.ArrayList;
import java.util.List;

public class Appointment {
    public static final String STATUS_SCHEDULED = "Scheduled";
    public static final String STATUS_COMPLETED = "Completed";
    public static final String STATUS_CANCELLED = "Cancelled";

    public static final String[] STATUSES = {
        STATUS_SCHEDULED,
        STATUS_COMPLETED,
        STATUS_CANCELLED
    };

    public static final String[] TIME_SLOTS = {
        "09:00 AM",
        "10:00 AM",
        "11:00 AM",
        "01:00 PM",
        "02:00 PM",
        "03:00 PM",
        "04:00 PM"
    };

    private static int nextId = 1;

    private final int id;
    private String ownerUsername;
    private String ownerName;
    private String contact;

    private Pet pet;
    private GroomingService service;
    private ArrayList<GroomingService> addOns;

    private String date;
    private String timeSlot;
    private String notes;
    private String status;

    public Appointment(String ownerUsername, String ownerName,  String contact, Pet pet, GroomingService service, ArrayList<GroomingService> addOns, String date, String timeSlot, String notes) {
        this(nextId, ownerUsername, ownerName, contact, pet, service, addOns, date, timeSlot, notes, STATUS_SCHEDULED);
    }

    public Appointment(int id, String ownerUsername, String ownerName, String contact, Pet pet, GroomingService service, ArrayList<GroomingService> addOns, String date, String timeSlot, String notes, String status) {
        this.id = id;

        if (id >= nextId) {
            nextId = id + 1;
        }

        this.ownerUsername = clean(ownerUsername);
        this.ownerName = clean(ownerName);
        this.contact = clean(contact);

        this.pet = pet;
        this.service = service;
        this.addOns = new ArrayList<>(addOns);

        this.date = clean(date);
        this.timeSlot = clean(timeSlot);
        this.notes = clean(notes);
        this.status = clean(status);
    }

    public int getId() {
        return id;
    }
    public String getOwnerUsername() {
        return ownerUsername;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public String getContact() {
        return contact;
    }
    public Pet getPet() {
        return pet;
    }
    public GroomingService getService() {
        return service;
    }
    public ArrayList<GroomingService> getAddOns() {
        return new ArrayList<>(addOns);
    }
    public String getDate() {
        return date;
    }
    public String getTimeSlot() {
        return timeSlot;
    }
    public String getNotes() {
        return notes;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = clean(status);
    }
    public static int calculateTotal(
            GroomingService service,
            Pet pet) {
        return service.getPrice() + pet.getSizeSurcharge();
    }

    public static int calculateTotal(GroomingService service, Pet pet, ArrayList<GroomingService> addOns) {
        int total = calculateTotal(service, pet);

        for (GroomingService addOn : addOns) {
            total += addOn.getPrice();
        }
        return total;
    }
    public int getPrice() {
        return calculateTotal(service, pet, addOns);
    }
    public String getDescription() {
        return service.getName() + " for " + pet.getDescription();
    }
    public boolean isActive() {
        return !STATUS_CANCELLED.equals(status);
    }
    public static boolean isSlotTaken(List<Appointment> list, String date, String slot, int ignoreId) {
        for (Appointment appointment : list) {
            if (appointment.isActive() && appointment.id != ignoreId && appointment.date.equals(date) && appointment.timeSlot.equals(slot)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getReceiptLines() {
        ArrayList<String> lines = new ArrayList<>();
        lines.add(service.getName());
        if (pet.getSizeSurcharge() > 0) {
            if (pet instanceof Dog) {
                Dog dog = (Dog) pet;
                lines.add("Size surcharge (" + dog.getSizeCategory() + " dog)");
            } else {
                lines.add("Size surcharge (" + pet.getPetType() + ", " + pet.getWeight() + " kg)");
            }
        }

        for (GroomingService addOn : addOns) {
            lines.add(addOn.getName());
        }
        return lines;
    }

    public ArrayList<Integer> getReceiptPrices() {
        ArrayList<Integer> prices = new ArrayList<>();
        prices.add(service.getPrice());
        if (pet.getSizeSurcharge() > 0) {
            prices.add(pet.getSizeSurcharge());
        }
        for (GroomingService addOn : addOns) {
            prices.add(addOn.getPrice());
        }
        return prices;
    }

    public String toFileLine() {
        String addOnNames = "";
        for (int i = 0; i < addOns.size(); i++) {
            if (i > 0) {
                addOnNames = addOnNames + ",";
            }
            addOnNames = addOnNames + addOns.get(i).getName();
        }
        return id + "|" + ownerUsername + "|" + ownerName + "|" + contact + "|" + pet.getPetType() + "|" + clean(pet.getName()) + "|" + pet.getWeight() + "|" + pet.getAge() + "|" + service.getName() + "|" + addOnNames + "|" + date + "|" + timeSlot + "|" + status   + "|" + notes;
    }

    public static Appointment fromFileLine(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length != 14) {
            return null;
        }
        
        try {
            int id = Integer.parseInt(parts[0]);
            Pet pet = Pet.create(parts[4], parts[5], Integer.parseInt(parts[6]), Integer.parseInt(parts[7]));

            GroomingService service = GroomingService.findService(parts[8]);
            if (service == null) {
                return null;
            }

            ArrayList<GroomingService> addOns = new ArrayList<>();

            if (!parts[9].isEmpty()) {
                for (String name : parts[9].split(",")) {
                    GroomingService addOn =
                            GroomingService.findAddOn(name);
                    if (addOn != null) {
                        addOns.add(addOn);
                    }
                }
            }
            return new Appointment(id, parts[1], parts[2], parts[3], pet, service, addOns, parts[10], parts[11], parts[13], parts[12]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static String clean(String text) {
        if (text == null) {
            return "";
        }

        return text.replace("|", "/")
                .replace("\r", " ")
                .replace("\n", " ")
                .trim();
    }
}