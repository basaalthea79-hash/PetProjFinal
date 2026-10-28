package model;
import java.awt.Component;
import java.io.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class UserDatabase {
    private static ArrayList<User> users = new ArrayList<>();
    private static boolean loaded = false;
    private static final String USERS_FILE = "users.txt";
    public static String loginMessage = "";
    public static boolean terminateLogin = false;
    public static boolean addUser(User user) {
        users.add(user);

        if (save()) {
            return true;
        }
        users.remove(user);
        return false;
    }

    public static User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public static User login(String username, String password) {
        terminateLogin = false;
        User user = findUser(username);
        if (user == null) {
            loginMessage = "Invalid username or password.";
            return null;
        }
        if (user.isBlocked()) {
            loginMessage = "Account blocked. Ask another " + "administrator to unblock it.";
            terminateLogin = true;
            return null;
        }
        if (!user.getPassword().equals(password)) {
            user.recordFailure();
            save();
            loginMessage = "Invalid password. Attempts: " + user.getFailedAttempts() + " of 3.";
            if (user.isBlocked()) {
                loginMessage = "Three failed attempts. " + "Account blocked. Ask an administrator " + "to unblock it.";
                terminateLogin = true;
            }
            return null;
        }
        user.resetAttempts();
        save();
        return user;
    }

    public static boolean confirmAdminPassword(Component parent) {
        JPasswordField field = new JPasswordField();
        int choice = JOptionPane.showConfirmDialog(parent, field, "Administrator password", JOptionPane.OK_CANCEL_OPTION);

        if (choice != JOptionPane.OK_OPTION) {
            return false;
        }
        String password = new String(field.getPassword());
        for (User user : users) {
            if (user instanceof Admin && !user.isBlocked() && user.getPassword().equals(password)) {
                return true;
            }
        }
        JOptionPane.showMessageDialog(parent, "Incorrect administrator password.");
        return false;
    }

    public static boolean usernameTaken(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public static String nextCustomerId() {
        int highest = 0;
        for (User user : users) {
            if (user instanceof Customer) {
                Customer customer = (Customer) user;
                int number = idNumber(customer.getCustomerID());
                if (number > highest) {
                    highest = number;
                }
            }
        }
        return "C" + (highest + 1);
    }

    public static ArrayList<Customer> findCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        for (User user : users) {
            if (user instanceof Customer) {
                customers.add((Customer) user);
            }
        }
        return customers;
    }

    private static int idNumber(String id) {
        try {
            return Integer.parseInt(id.substring(1));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void load() {
        if (loaded) {
            return;
        }
        loaded = true;
        File file = new File(USERS_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    User user = parseLine(line);
                    if (user != null) {
                        users.add(user);
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error loading accounts: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (findAdmin() == null) {
            users.add(new Admin("A001", "System Admin", "admin", "admin123"));
            save();
        }
    }

    private static User findAdmin() {
        for (User user : users) {
            if (user instanceof Admin) {
                return user;
            }
        }
        return null;
    }

    private static User parseLine(String line) {
        String[] parts = line.split("\\|", -1);
        User user = null;
        int securityIndex = 0;
        try {
            if (parts[0].equals("CUSTOMER") && (parts.length == 7 || parts.length == 9)) {
                user = new Customer(parts[1], parts[2], parts[3], parts[4], parts[5], parts[6]);
                securityIndex = 7;
            } else if (parts[0].equals("ADMIN") && (parts.length == 5 || parts.length == 7)) {
                user = new Admin(parts[1], parts[2], parts[3], parts[4]);
                securityIndex = 5;
            }
            if (user != null&& parts.length == securityIndex + 2) {
                user.restoreSecurity(Integer.parseInt(parts[securityIndex]), Boolean.parseBoolean(parts[securityIndex + 1]));
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return user;
    }

    public static boolean save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                writer.write(user.toFileLine() + "|" + user.getFailedAttempts() + "|" + user.isBlocked());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving accounts: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }
}