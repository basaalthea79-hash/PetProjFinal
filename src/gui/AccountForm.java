package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import model.*;

public class AccountForm extends JDialog implements ActionListener {

    private User currentUser;
    private JPasswordField oldPassword = new JPasswordField();
    private JPasswordField newPassword = new JPasswordField();
    private JPasswordField confirmPassword = new JPasswordField();

    public AccountForm(JFrame parent, User user) {
        super(parent, "Account / Security", true);

        currentUser = user;
        setSize(430, 330);
        setLayout(null);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addField("Current password:", oldPassword, 25);
        addField("New password:", newPassword, 65);
        addField("Confirm password:", confirmPassword, 105);
        addButton("Change Password", 25, 155, 175);
        addButton("Close", 215, 155, 160);

        if (user instanceof Admin) {
            addButton("Unblock User", 25, 205, 175);
            addButton("Add Admin", 215, 205, 160);
        }
        LoginFrame.styleForm(getContentPane());
        setVisible(true);
    }

    private void addField(
            String text,
            JPasswordField field,
            int y) {

        JLabel label = new JLabel(text);
        label.setBounds(20, y, 145, 28);
        field.setBounds(170, y, 220, 28);
        add(label);
        add(field);
    }

    private void addButton(
            String text,
            int x,
            int y,
            int width) {

        JButton button = new JButton(text);
        button.setBounds(x, y, width, 30);
        button.setToolTipText(text);
        button.addActionListener(this);
        add(button);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.equals("Change Password")) {
            changePassword();
        } else if (command.equals("Close")) {
            dispose();
        } else if (currentUser instanceof Admin) {
            if (command.equals("Unblock User")) {
                unblockUser();
            } else if (command.equals("Add Admin")) {
                addAdmin();
            }
        }
    }

    private void changePassword() {
        String oldValue = new String(oldPassword.getPassword());
        String newValue = new String(newPassword.getPassword());
        String confirmValue = new String(confirmPassword.getPassword());

        if (!currentUser.getPassword().equals(oldValue)) {
            JOptionPane.showMessageDialog(this, "Current password is incorrect.");
            return;
        }

        if (newValue.length() < 6 || newValue.contains("|") || !newValue.equals(confirmValue)) {
            JOptionPane.showMessageDialog(this, "Use at least 6 characters, no |, " + "and matching passwords.");
            return;
        }
        currentUser.setPassword(newValue);

        if (UserDatabase.save()) {
            JOptionPane.showMessageDialog(this, "Password changed.");
            dispose();
        } else {
            currentUser.setPassword(oldValue);
        }
    }

    private void unblockUser() {
        if (!UserDatabase.confirmAdminPassword(this)) {
            return;
        }
        String username = JOptionPane.showInputDialog(this, "Username to unblock:");
        
        if (username == null) {
            return;
        }
        User user = UserDatabase.findUser(username.trim());

        if (user == null) {
            JOptionPane.showMessageDialog(this, "User not found.");
            return;
        }
        user.unblock();

        if (UserDatabase.save()) {
            JOptionPane.showMessageDialog(this, "User unblocked.");
        }
    }

    private void addAdmin() {
        if (!UserDatabase.confirmAdminPassword(this)) {
            return;
        }
        String name = JOptionPane.showInputDialog(this, "New administrator name:");

        if (name == null) {
            return;
        }
        String username = JOptionPane.showInputDialog(this, "New administrator username:");

        if (username == null) {
            return;
        }
        JPasswordField passwordField = new JPasswordField();
        int choice = JOptionPane.showConfirmDialog(this, passwordField, "New administrator password", JOptionPane.OK_CANCEL_OPTION);

        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        String password = new String(passwordField.getPassword());
        name = name.trim();
        username = username.trim();

        if (name.isEmpty()
                || username.isEmpty()
                || password.length() < 6
                || name.contains("|")
                || username.contains("|")
                || password.contains("|")
                || UserDatabase.usernameTaken(username)) {

            JOptionPane.showMessageDialog(this, "Complete all fields.\n" + "Use a unique username, no |, " + "and a password of at least 6 characters.");
            return;
        }
        JPasswordField confirmation = new JPasswordField();
        
        int confirmChoice = JOptionPane.showConfirmDialog(this, confirmation, "Confirm new password", JOptionPane.OK_CANCEL_OPTION);

        if (confirmChoice != JOptionPane.OK_OPTION) {
            return;
        }

        if (!password.equals(new String(confirmation.getPassword()))) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }

        Admin admin = new Admin("A-" + username, name, username, password);
        if (!UserDatabase.addUser(admin)) {
            return;
        }
        JOptionPane.showMessageDialog(this, "Administrator added.");
    }
}