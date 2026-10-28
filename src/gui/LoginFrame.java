package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import model.Admin;
import model.Customer;
import model.User;
import model.UserDatabase;

public class LoginFrame extends JFrame implements ActionListener {
    public static final Color TEAL = new Color(36, 104, 107);
    public static final Color BACKGROUND = new Color(245, 247, 248);
    public static final Color MUTED = new Color(125, 136, 146);
    public static final Color SLATE = new Color(88, 110, 115);
    public static final Color BLUE = new Color(76, 113, 150);
    public static final Color RED = new Color(178, 72, 75);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordBox;
    private char defaultEchoChar;
    private int invalidAttempts;

    public LoginFrame() {
        setTitle("PawCare | Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        JPanel background = new JPanel(null);
        background.setBackground(BACKGROUND);
        background.setPreferredSize(new Dimension(500, 475));
        setContentPane(background);
        JPanel card = new JPanel(null);
        card.setBackground(Color.WHITE);
        card.setBounds(65, 32, 375, 414);
        background.add(card);
        JLabel brand = new JLabel("PawCare", SwingConstants.CENTER);
        brand.setFont(new Font("Arial", Font.BOLD, 38));
        brand.setForeground(TEAL);
        brand.setBounds(35, 38, 305, 45);
        card.add(brand);
        JLabel subtitle = new JLabel("Pet Grooming Appointment System",SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(MUTED);
        subtitle.setBounds(20, 89, 335, 26);
        card.add(subtitle);
        JLabel usernameLabel = new JLabel("Username", SwingConstants.CENTER);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setBounds(45, 148, 285, 23);

        card.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(45, 175, 285, 24);

        card.add(usernameField);

        JLabel passwordLabel = new JLabel("Password", SwingConstants.CENTER);
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setBounds(45, 218, 285, 23);
        card.add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(45, 245, 285, 24);
        card.add(passwordField);
        defaultEchoChar = passwordField.getEchoChar();
        showPasswordBox = new JCheckBox("Show password");
        showPasswordBox.setOpaque(false);
        showPasswordBox.setFont(new Font("Arial", Font.PLAIN, 11));
        showPasswordBox.setForeground(MUTED);
        showPasswordBox.setBounds(45, 273, 130, 20);
        showPasswordBox.setActionCommand("showPassword");
        showPasswordBox.addActionListener(this);
        card.add(showPasswordBox);

        JButton loginButton = new JButton("Log In");
        styleButton(loginButton, TEAL);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBounds(160, 298, 170, 35);
        loginButton.setActionCommand("login");
        loginButton.addActionListener(this);
        card.add(loginButton);

        JButton signupButton = new JButton("Create an Account");
        styleButton(signupButton, Color.WHITE);
        signupButton.setForeground(TEAL);
        signupButton.setBorder(BorderFactory.createLineBorder(new Color(128, 146, 165)));
        signupButton.setBounds(160, 343, 170, 35);
        signupButton.setActionCommand("openSignup");
        signupButton.addActionListener(this);
        card.add(signupButton);
        getRootPane().setDefaultButton(loginButton);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void styleButton(JButton button, Color color) {
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(14, 17, 14, 17));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setToolTipText(button.getText());
    }

    public static JPanel createHeader(String account) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(TEAL);
        header.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));
        JPanel branding = new JPanel(new GridLayout(2, 1, 0, 3));
        branding.setOpaque(false);
        JLabel brand = new JLabel("PawCare");
        brand.setFont(new Font("Arial", Font.BOLD, 32));
        brand.setForeground(Color.WHITE);
        branding.add(brand);
        JLabel subtitle = new JLabel("PET GROOMING APPOINTMENT SYSTEM");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitle.setForeground(Color.WHITE);
        branding.add(subtitle);
        header.add(branding, BorderLayout.WEST);
        JLabel identity = new JLabel(account);
        identity.setFont(new Font("Arial", Font.PLAIN, 14));
        identity.setForeground(Color.WHITE);
        header.add(identity, BorderLayout.EAST);
        return header;
    }

    public static void styleTable(JTable table) {

        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(40);
        table.setBackground(Color.WHITE);
        table.setForeground(new Color(35, 49, 60));
        table.setSelectionBackground(new Color(229, 242, 241));
        table.setSelectionForeground(new Color(35, 49, 60));
        table.setGridColor(new Color(221, 229, 233));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(232, 239, 241));
        table.getTableHeader().setForeground(new Color(35, 49, 60));
        table.getTableHeader().setPreferredSize(new Dimension(100, 44));
        table.getTableHeader().setReorderingAllowed(false);
    }

    public static void styleForm(Container container) {

        container.setBackground(Color.WHITE);
        for (Component component : container.getComponents()) {
            component.setFont(new Font("Arial", Font.PLAIN, 14));

            if (component instanceof JButton) {
                JButton button = (JButton) component;
                styleButton(button, TEAL);
                button.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
            } else if (component instanceof JLabel) {
                component.setForeground(new Color(35, 49, 60));
            } else if (component instanceof JPanel) {
                styleForm((JPanel) component);
            }
        }
    }

    void prefillUsername(String username) {
        usernameField.setText(username);
        passwordField.setText("");
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();
        if (command.equals("showPassword")) {
            showPassword();
        } else if (command.equals("login")) {
            login();
        } else if (command.equals("openSignup")) {
            new SignupFrame(this);
        }
    }

    private void showPassword() {
        if (showPasswordBox.isSelected()) {
            passwordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar(defaultEchoChar);
        }
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your username and password.");
            return;
        }

        User user = UserDatabase.login(username, password
        );

        if (user != null) {
            dispose();
            if (user instanceof Admin) {
                new AdminDashboard((Admin) user);
            } else {
                new CustomerDashboard((Customer) user);
            }
        } else {
            JOptionPane.showMessageDialog(this, UserDatabase.loginMessage);
            invalidAttempts++;
            if (invalidAttempts >= 3
                    || UserDatabase.terminateLogin) {
                System.exit(0);
            }
        }
    }
}