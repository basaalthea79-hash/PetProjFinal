package model;  
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class AppointmentDatabase {
    private static ArrayList<Appointment> appointments = new ArrayList<>();
    private static boolean loaded = false;
    private static final String APPOINTMENTS_FILE = "appointments.txt";
    private static final String SAMPLE_MARKER_FILE = "sample_bookings_added.txt";

    public static void load() {
        if (loaded) {
            return;
        }
        loaded = true;
        File file = new File(APPOINTMENTS_FILE);

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Appointment appointment = Appointment.fromFileLine(line);
                    if (appointment == null) {
                        JOptionPane.showMessageDialog(null, "An appointment record could not be read.\n" + "Sample bookings will not be added.", "Error loading appointments", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    appointments.add(appointment);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error loading appointments: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        addSamplesOnce();
    }

    private static void addSamplesOnce() {
        File marker = new File(SAMPLE_MARKER_FILE);
        if (marker.exists()) {
            return;
        }
        String[] ownerNames = {
            "Maria Santos",
            "Juan Dela Cruz",
            "Ana Reyes",
            "Carlo Garcia",
            "Liza Ramos"
        };
        String[] contacts = {
            "09170000001",
            "09170000002",
            "09170000003",
            "09170000004",
            "09170000005"
        };
        String[] petNames = {
            "Max",
            "Mingming",
            "Buddy",
            "Snow",
            "Luna"
        };
        String[] petTypes = {
            "Dog",
            "Cat",
            "Dog",
            "Rabbit",
            "Cat"
        };
        int[] weights = {8, 4, 12, 2, 5};
        int[] ages = {3, 2, 4, 1, 3};

        GroomingService[] services = GroomingService.getServices();
        LocalDate date = LocalDate.now().plusDays(1);
        int timeIndex = 0;

        for (int i = 0; i < ownerNames.length; i++) {
            if (sampleAlreadyExists(ownerNames[i])) {
                continue;
            }
            String dateText = date.toString();
            String time = Appointment.TIME_SLOTS[timeIndex];

            while (isSlotTaken(dateText, time, -1)) {
                timeIndex++;
                if (timeIndex >= Appointment.TIME_SLOTS.length) {
                    timeIndex = 0;
                    date = date.plusDays(1);
                }
                dateText = date.toString();
                time = Appointment.TIME_SLOTS[timeIndex];
            }
            Pet pet = Pet.create(petTypes[i], petNames[i], weights[i],ages[i]);
            ArrayList<GroomingService> addOns = new ArrayList<>();
            GroomingService service = services[i % services.length];
            Appointment sample = new Appointment("", ownerNames[i], contacts[i], pet, service, addOns, dateText, time, "Sample booking");
            appointments.add(sample);
        }

        if (save()) {
            try {
                marker.createNewFile();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Saved ang samples, pero hindi nagawa ang marker: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static boolean sampleAlreadyExists(String ownerName) {
        for (Appointment appointment : appointments) {
            if (appointment.getOwnerName().equals(ownerName) && appointment.getNotes().equals("Sample booking")) {
                return true;
            }
        }
        return false;
    }
    public static ArrayList<Appointment> getAll() {
        return new ArrayList<>(appointments);
    }
    public static ArrayList<Appointment> getForCustomer(String username) {
        ArrayList<Appointment> mine = new ArrayList<>();

        for (Appointment appointment : appointments) {
            if (appointment.getOwnerUsername().equals(username)) {
                mine.add(appointment);
            }
        }
        return mine;
    }
    public static void add(Appointment appointment) {
        appointments.add(appointment);
        save();
    }
    public static void replace(Appointment oldAppointment,
            Appointment newAppointment) {

        int index = appointments.indexOf(oldAppointment);

        if (index >= 0) {
            appointments.set(index, newAppointment);
            save();
        }
    }
    public static void remove(Appointment appointment) {
        appointments.remove(appointment);
        save();
    }
    public static boolean isSlotTaken(String date, String time, int ignoreId) {
        return Appointment.isSlotTaken(appointments, date, time, ignoreId);
    }
    public static boolean save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPOINTMENTS_FILE))) {
            for (Appointment appointment : appointments) {
                writer.write(appointment.toFileLine());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving appointments: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}