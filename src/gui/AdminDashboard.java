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
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import model.Admin;
import model.Appointment;
import model.AppointmentDatabase;
import model.Customer;
import model.UserDatabase;

public class AdminDashboard extends JFrame implements ActionListener {
    private Admin admin;
    private DefaultTableModel model;
    private JTable table;
    private JLabel info;
    private String searchText = "";

    // dashboard colors
    private final Color teal = new Color(35, 105, 105);
    private final Color background = new Color(245, 247, 248);
    private final Color textColor = new Color(45, 55, 60);
    private final Color lightTeal = new Color(230, 242, 241);
    private final Color borderColor = new Color(225, 230, 232);

    public AdminDashboard(Admin admin) { 
        this.admin = admin;

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

    // dashboard header
    private void createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(teal);
        header.setBorder(new EmptyBorder(17, 24, 17, 24));

        JLabel title = new JLabel("PawCare");
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("PET GROOMING APPOINTMENT SYSTEM");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 10));
        subtitle.setForeground(new Color(220, 238, 236));

        JPanel brand = new JPanel();
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBackground(teal);

        brand.add(title);
        brand.add(Box.createVerticalStrut(2));
        brand.add(subtitle);

        JLabel account = new JLabel(admin.getUsername() + "  |  " + admin.getRole());
        account.setFont(new Font("SansSerif", Font.PLAIN, 13));
        account.setForeground(Color.WHITE);

        header.add(brand, BorderLayout.WEST);
        header.add(account, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    // title appointment tbl
    private void createTable() {
        JPanel content = new JPanel(new BorderLayout(10, 12));
        content.setBackground(background);
        content.setBorder(new EmptyBorder(20, 22, 12, 22));

        JLabel heading = new JLabel("Appointment Management");
        heading.setFont(new Font("SansSerif", Font.BOLD, 21));
        heading.setForeground(textColor);

        JLabel subtitle = new JLabel("Manage and monitor all grooming appointments.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);

        JPanel headingPanel = new JPanel();
        headingPanel.setLayout( new BoxLayout(headingPanel, BoxLayout.Y_AXIS));
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
        table.getTableHeader().setBackground(new Color(232, 239, 239) );
        table.getTableHeader().setForeground(textColor);
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));
        table.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor));
        scrollPane.getViewport().setBackground(Color.WHITE);
        content.add(scrollPane, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);
    }

    //  buttons under tbl
    private void createBottomPanel() {
        JPanel bottom = new JPanel(new BorderLayout(8, 10));
        bottom.setBackground(Color.WHITE);
        bottom.setBorder(new EmptyBorder(12, 18, 10, 18));

        JPanel buttonArea = new JPanel(new BorderLayout(8, 8));
        buttonArea.setBackground(Color.WHITE);

        // first btn
        JPanel firstRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 3));
        firstRow.setBackground(Color.WHITE);

        JButton bookButton = createButton(
            "Book Appointment",
            teal,
            "book"
        );
        JButton refreshButton = createButton(
            "Refresh Table",
            new Color(90, 110, 115),
            "refresh"
        );
        JButton deleteButton = createButton(
            "Delete Appointment",
            new Color(175, 75, 75),
            "delete"
        );
        JButton receiptButton = createButton(
            "View Receipt",
            new Color(75, 110, 145),
            "receipt"
        );
        firstRow.add(bookButton);
        firstRow.add(refreshButton);
        firstRow.add(deleteButton);
        firstRow.add(receiptButton);
        buttonArea.add(firstRow, BorderLayout.NORTH);

        // second btn
        JPanel secondRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 3));
        secondRow.setBackground(Color.WHITE);

        JButton searchButton = createButton(
            "Search",
            new Color(90, 110, 115),
            "search"
        );
        JButton statusButton = createButton(
            "Update Status",
            new Color(75, 110, 145),
            "status"
        );
        JButton customersButton = createButton(
            "View Customers",
            new Color(90, 110, 115),
            "customers"
        );
        JButton accountButton = createButton(
            "Account / Security",
            teal,
            "account"
        );
        secondRow.add(searchButton);
        secondRow.add(statusButton);
        secondRow.add(customersButton);
        secondRow.add(accountButton);
        buttonArea.add(secondRow, BorderLayout.CENTER);

        // number of appointments and logout btn
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

    // create new btn same style
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

    // create appointment for customer
    private void bookAppointment() {AppointmentForm form = new AppointmentForm(this, null, null);
        form.setVisible(true);

        if (form.isSaved()) {
            Appointment created = form.getResult();

            AppointmentDatabase.add(created);
            searchText = "";
            refreshTable();

            JOptionPane.showMessageDialog(this, "Appointment #" + created.getId() + " booked!\nTotal: P" + created.getPrice());
        }
    }

    // search appointment
    private boolean matchesSearch(Appointment appointment) {
        if (searchText.isEmpty()) {
            return true;
        }

        String find = searchText.toLowerCase();

        return appointment.getOwnerName()
                .toLowerCase()
                .contains(find)
            || appointment.getPet()
                .getName()
                .toLowerCase()
                .contains(find);
    }

    // show appointment table
    private void refreshTable() {ArrayList<Appointment> appointments = AppointmentDatabase.getAll();
        model.setRowCount(0);
        int shown = 0;

        for (Appointment appointment : appointments) {
            if (matchesSearch(appointment)) {
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
                shown++;
            }
        }
        info.setText("Appointments displayed: " + shown);
    }

    // get appointent
    private Appointment getSelectedAppointment() {
        int row = table.getSelectedRow();

        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select an appointment first.");
            return null;
        }
        int id = (Integer) model.getValueAt(row, 0);

        for (Appointment appointment : AppointmentDatabase.getAll()) {
            if (appointment.getId() == id) {
                return appointment;
            }
        }
        return null;
    }

    // change status appointment
    private void updateStatus() {
        Appointment selected = getSelectedAppointment();

        if (selected == null) {
            return;
        }

        int choice = JOptionPane.showOptionDialog(this,
            "Appointment #" + selected.getId()
                + " for " + selected.getPet().getName()
                + "\nCurrent status: " + selected.getStatus()
                + "\n\nChange status to:",
            "Update Status",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            Appointment.STATUSES,
            selected.getStatus()
        );

        if (choice < 0) {
            return;
        }

        String newStatus = Appointment.STATUSES[choice];

        boolean reopening =
            !newStatus.equals(Appointment.STATUS_CANCELLED)
            && !selected.isActive();

        if (reopening && AppointmentDatabase.isSlotTaken(
                selected.getDate(),
                selected.getTimeSlot(),
                selected.getId())) {

            JOptionPane.showMessageDialog(
                this,
                "That time slot has been booked by someone else.\n"
                    + "This appointment cannot be re-activated.",
                "Slot Taken",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        selected.setStatus(newStatus);
        AppointmentDatabase.save();
        refreshTable();
    }

    // delete appointment
    private void deleteSelected() {
        Appointment selected = getSelectedAppointment();

        if (selected == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete appointment #" + selected.getId()
                + " for " + selected.getPet().getName()
                + "?\nThis cannot be undone.",
            "Delete Appointment",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (!UserDatabase.confirmAdminPassword(this)) {
                return;
            }

            AppointmentDatabase.remove(selected);
            refreshTable();
        }
    }

    // search owner name or pet name
    private void search() {
        String text = JOptionPane.showInputDialog(this, "Search by owner name or pet name:", "Search", JOptionPane.QUESTION_MESSAGE);

        if (text == null) {
            return;
        }
        searchText = text.trim();
        refreshTable();

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No appointments match \"" + searchText + "\".");
        }
    }

        // registered customer
    private void viewCustomers() {
        List<Customer> customers = UserDatabase.findCustomers();

        if (customers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No customers registered yet.");
            return;
        }

        DefaultListModel<String> listModel =
            new DefaultListModel<>();

        for (Customer customer : customers) {
            listModel.addElement(
                customer.getCustomerID()
                + " - " + customer.getName()
                + " (" + customer.getUsername() + ")  "
                + customer.getPhoneNumber()
            );
        }

        JList<String> list = new JList<>(listModel);
        JScrollPane scroll = new JScrollPane(list);
        scroll.setPreferredSize(new Dimension(380, 240));

        JOptionPane.showMessageDialog(this,scroll, "Registered Customers (" + customers.size() + ")", JOptionPane.PLAIN_MESSAGE);
    }

    // action btn
    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.equals("book")) {
            bookAppointment();
        }
        if (command.equals("refresh")) {
            searchText = "";
            refreshTable();
        }
        if (command.equals("delete")) {
            deleteSelected();
        }
        if (command.equals("receipt")) {
            Appointment selected = getSelectedAppointment();
            if (selected != null) {
                Receipt.showReceipt(selected, this);
            }
        }
        if (command.equals("search")) {
            search();
        }
        if (command.equals("status")) {
            updateStatus();
        }
        if (command.equals("customers")) {
            viewCustomers();
        }
        if (command.equals("account")) {
            new AccountForm(this, admin);
        }

        if (command.equals("logout")) {
            dispose();
            new LoginFrame();
        }
    }
}