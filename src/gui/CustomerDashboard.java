package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Appointment;
import model.AppointmentDatabase;
import model.Customer;

public class CustomerDashboard extends JFrame implements ActionListener {
    private Customer customer;
    private DefaultTableModel model;
    private JTable table;
    private JLabel info;

    private final Color teal = new Color(35, 105, 105);
    private final Color background = new Color(245, 247, 248);
    private final Color textColor = new Color(45, 55, 60);
    private final Color lightTeal = new Color(230, 242, 241);
    private final Color borderColor = new Color(225, 230, 232);

    public CustomerDashboard(Customer customer) {
        this.customer = customer;

        setTitle("PawCare | Dashboard");
        setSize(1150, 680);
        setMinimumSize(new Dimension(900, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(background);

        createHeader();
        createTable();
        createBottomPanel();

        refreshTable();
        setVisible(true);
    }

    private void createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(teal);
        header.setBorder(new EmptyBorder(17, 24, 17, 24));

        JLabel title = new JLabel("PawCare");
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
            "PET GROOMING APPOINTMENT SYSTEM"
        );
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 10));
        subtitle.setForeground(new Color(220, 238, 236));

        JPanel brand = new JPanel();
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBackground(teal);

        brand.add(title);
        brand.add(Box.createVerticalStrut(2));
        brand.add(subtitle);

        JLabel account = new JLabel(
            customer.getUsername() + "  |  " + customer.getRole()
        );
        account.setFont(new Font("SansSerif", Font.PLAIN, 13));
        account.setForeground(Color.WHITE);

        header.add(brand, BorderLayout.WEST);
        header.add(account, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    private void createTable() {
        JPanel content = new JPanel(new BorderLayout(10, 12));
        content.setBackground(background);
        content.setBorder(new EmptyBorder(20, 22, 12, 22));

        JLabel heading = new JLabel("Appointment Management");
        heading.setFont(new Font("SansSerif", Font.BOLD, 21));
        heading.setForeground(textColor);

        JLabel subtitle = new JLabel("View and manage your grooming appointments.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);

        JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.Y_AXIS));
        headingPanel.setBackground(background);

        headingPanel.add(heading);
        headingPanel.add(Box.createVerticalStrut(5));
        headingPanel.add(subtitle);

        content.add(headingPanel, BorderLayout.NORTH);

        String[] columns = {
            "ID",
            "Owner",
            "Pet",
            "Pet Type",
            "Service",
            "Date",
            "Time",
            "Price",
            "Status"
        };

        model = new AppointmentTableModel(columns);
        table = new JTable(model);

        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setRowHeight(32);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(borderColor);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(lightTeal);
        table.setSelectionForeground(textColor);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(232, 239, 239));
        table.getTableHeader().setForeground(textColor);
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor));
        scrollPane.getViewport().setBackground(Color.WHITE);
        content.add(scrollPane, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);
    }

    private void createBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout(8, 10));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(12, 18, 10, 18));

        JPanel buttonArea = new JPanel(new BorderLayout(8, 8));
        buttonArea.setBackground(Color.WHITE);

        JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 3));
        firstRow.setBackground(Color.WHITE);

        JButton bookButton = createButton("Book Appointment",teal, "book");
        JButton refreshButton = createButton("Refresh Table", new Color(90, 110, 115), "refresh");
        JButton cancelButton = createButton("Cancel Booking", new Color(175, 75, 75), "cancel");
        JButton receiptButton = createButton("View Receipt", new Color(75, 110, 145), "receipt");
        JButton editButton = createButton("Edit Booking", new Color(90, 110, 115),"edit");
        JButton accountButton = createButton("Account / Security", teal, "account");

        firstRow.add(bookButton);
        firstRow.add(refreshButton);
        firstRow.add(cancelButton);
        firstRow.add(receiptButton);
        firstRow.add(editButton);
        firstRow.add(accountButton);

        buttonArea.add(firstRow, BorderLayout.NORTH);

        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(Color.WHITE);

        info = new JLabel("Ready");
        info.setFont(new Font("SansSerif", Font.PLAIN, 12));
        info.setForeground(Color.GRAY);

        JButton logoutButton = new JButton("Log Out");
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        logoutButton.setForeground(new Color(160, 65, 65));
        logoutButton.setBackground(new Color(255, 245, 245));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setActionCommand("logout");
        logoutButton.addActionListener(this);

        statusPanel.add(info, BorderLayout.WEST);
        statusPanel.add(logoutButton, BorderLayout.EAST);
        bottom.add(buttonArea, BorderLayout.CENTER);
        bottom.add(statusPanel, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, Color color, String command) {
        JButton button = new JButton(text);

        button.setFont(new Font("SansSerif", Font.PLAIN, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(8, 10, 8, 10));

        button.setActionCommand(command);
        button.addActionListener(this);
        return button;
    }

    private void refreshTable() {
        ArrayList<Appointment> appointments = AppointmentDatabase.getForCustomer(customer.getUsername());
        model.setRowCount(0);
        int activeTotal = 0;

        for (Appointment appointment : appointments) {
            if (appointment.isActive()) {
                activeTotal += appointment.getPrice();
            }

            model.addRow(new Object[]{
                appointment.getId(),
                appointment.getOwnerName(),
                appointment.getPet().getName(),
                appointment.getPet().getPetType(),
                appointment.getService().getName(),
                appointment.getDate(),
                appointment.getTimeSlot(),
                "PHP " + appointment.getPrice(),
                appointment.getStatus()
            });
        }

        info.setText("You have " + appointments.size() + " appointment(s)  |  Value of active bookings: P" + activeTotal);
    }

    private Appointment getSelectedAppointment() {
        int row = table.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment first.");
            return null;
        }

        int id = (Integer) model.getValueAt(row, 0);
        ArrayList<Appointment> appointments =AppointmentDatabase.getForCustomer(customer.getUsername());

        for (Appointment appointment : appointments) {
            if (appointment.getId() == id) {
                return appointment;
            }
        }
        return null;
    }

    private void bookAppointment() {
        AppointmentForm form = new AppointmentForm(this, customer, null);
        form.setVisible(true);

        if (form.isSaved()) {
            Appointment created = form.getResult();
            AppointmentDatabase.add(created);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Appointment #" + created.getId() + " booked!\nTotal: P" + created.getPrice());
        }
    }

    private void editSelected() {
        Appointment selected = getSelectedAppointment();

        if (selected == null) {
            return;
        }

        if (!selected.isActive()) {
            JOptionPane.showMessageDialog(this, "A cancelled appointment cannot be edited.\n" + "Please book a new one instead.");
            return;
        }

        AppointmentForm form = new AppointmentForm(this, customer, selected);
        form.setVisible(true);

        if (form.isSaved()) {
            AppointmentDatabase.replace(selected,form.getResult());
            refreshTable();
            JOptionPane.showMessageDialog(this, "Appointment #" + selected.getId() + " updated.");
        }
    }

    private void cancelSelected() {
        Appointment selected = getSelectedAppointment();

        if (selected == null) {
            return;
        }

        if (!selected.isActive()) {
            JOptionPane.showMessageDialog(this, "This appointment is already cancelled.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Cancel appointment #" + selected.getId() + " for " + selected.getPet().getName() + "?", "Cancel Appointment", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            selected.setStatus(Appointment.STATUS_CANCELLED);
            AppointmentDatabase.save();
            refreshTable();
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.equals("book")) {
            bookAppointment();
        }
        if (command.equals("refresh")) {
            refreshTable();
        }
        if (command.equals("cancel")) {
            cancelSelected();
        }
        if (command.equals("receipt")) {
            Appointment selected = getSelectedAppointment();
            if (selected != null) {
                Receipt.showReceipt(selected, this);
            }
        }
        if (command.equals("edit")) {
            editSelected();
        }
        if (command.equals("account")) {
            new AccountForm(this, customer);
        }
        if (command.equals("logout")) {
            dispose();
            new LoginFrame();
        }
    }
}