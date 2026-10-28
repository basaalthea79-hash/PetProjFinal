package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import model.Customer;
import model.UserDatabase;

public class SignupFrame extends JFrame implements ActionListener {
    private JTextField nameField;
    private JTextField usernameField;
    private JTextField phoneField;
    private JTextField addressField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JCheckBox showPasswordsBox;
    private char defaultEchoChar;
    private final LoginFrame parent;
    public SignupFrame(LoginFrame parent) {
        this.parent = parent;

        setTitle("PawCare - Sign Up");
        setSize(520, 490);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent);
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(25, 30, 165, 30);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(195, 30, 260, 30);
        add(nameField);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(25, 70, 165, 30);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(195, 70, 260, 30);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(25, 110, 165, 30);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(195, 110, 260, 30);
        add(passwordField);

        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setBounds(25, 150, 165, 30);
        add(confirmLabel);

        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setBounds(195, 150, 260, 30);
        add(confirmPasswordField);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(25, 190, 165, 30);
        add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(195, 190, 260, 30);
        add(phoneField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(25, 230, 165, 30);
        add(addressLabel);

        addressField = new JTextField();
        addressField.setBounds(195, 230, 260, 30);
        add(addressField);

        showPasswordsBox = new JCheckBox("Show passwords");
        showPasswordsBox.setBounds(195, 270, 180, 20);
        showPasswordsBox.setOpaque(false);

        showPasswordsBox.setActionCommand("showPasswords");
        showPasswordsBox.addActionListener(this);
        add(showPasswordsBox);

        defaultEchoChar = passwordField.getEchoChar();

        JButton createButton = new JButton("Create Account");
        createButton.setBounds(195, 330, 220, 38);
        createButton.setActionCommand("createAccount");
        createButton.addActionListener(this);
        add(createButton);

        getRootPane().setDefaultButton(createButton);

        LoginFrame.styleForm(getContentPane());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("showPasswords")) {
            showPasswords();
        } else if (command.equals("createAccount")) {
            createAccount();
        }
    }

    private void showPasswords() {
        char echo = defaultEchoChar;
        if (showPasswordsBox.isSelected()) {
            echo = (char) 0;
        }
        passwordField.setEchoChar(echo);
        confirmPasswordField.setEchoChar(echo);
    }

    private void createAccount() {
        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();

        if (name.isEmpty()
                || username.isEmpty()
                || password.isEmpty()
                || phone.isEmpty()
                || address.isEmpty()) {

            JOptionPane.showMessageDialog(this, "Please complete all fields.");
        } else if (name.contains("|")
                || username.contains("|")
                || password.contains("|")
                || phone.contains("|")
                || address.contains("|")) {

            JOptionPane.showMessageDialog(this, "The | character is not allowed.");
        } else if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Use at least 6 characters for the password.");
        } else if (!phone.matches("[0-9+ -]{7,20}")) {
            JOptionPane.showMessageDialog(this, "Enter a valid contact number " + "(7 to 20 characters).");
        } else if (UserDatabase.usernameTaken(username)) {
            JOptionPane.showMessageDialog(this, "That username is already taken.");
        } else if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
        } else {
            String customerID = UserDatabase.nextCustomerId();
            Customer customer = new Customer(customerID, name, username, password, phone, address);

            if (!UserDatabase.addUser(customer)) {
                return;
            }
            JOptionPane.showMessageDialog(this, "Account Created Successfully!\n" + "Your Customer ID is " + customerID);
            parent.prefillUsername(username);
            dispose();
        }
    }
}