package gui;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.Appointment;
import model.AppointmentDatabase;
import model.Customer;
import model.GroomingService;
import model.Pet;

public class AppointmentForm extends JDialog implements ActionListener {
    private Customer customer;
    private Appointment existing;
    private Appointment result;
    private boolean saved = false;
    private JTextField ownerNameField;
    private JTextField contactField;
    private JTextField petNameField;
    private JTextField weightField;
    private JTextField ageField;

    private JRadioButton[] typeButtons = new JRadioButton[Pet.PET_TYPES.length];
    private JComboBox<GroomingService> serviceBox;
    private JCheckBox[] addOnBoxes = new JCheckBox[GroomingService.getAddOns().length];
    private JComboBox<String> dateBox;
    private JList<String> timeList;
    private JTextArea notesArea;
    private JLabel totalLabel;

    public AppointmentForm(JFrame parent, Customer customer, Appointment existing) {
        super(parent, "Book Appointment", true);
        this.customer = customer;
        this.existing = existing;
        
        if (existing != null) {
            setTitle("Edit Appointment #" + existing.getId());
        }

        setSize(500, 700);
        setLayout(null);
        setLocationRelativeTo(parent);
        buildForm();
        LoginFrame.styleForm(getContentPane());

        if (existing != null) {
            fillFromExisting();
        }
        updateTotal();
    }

    public boolean isSaved() {
        return saved;
    }

    public Appointment getResult() {
        return result;
    }

    private void buildForm() {
        JLabel ownerLabel = new JLabel("Owner name(Full Name):");
        ownerLabel.setBounds(20, 20, 110, 25);
        add(ownerLabel);

        ownerNameField = new JTextField();
        ownerNameField.setBounds(150, 20, 300, 25);
        add(ownerNameField);

        JLabel contactLabel = new JLabel("Contact number:");
        contactLabel.setBounds(20, 55, 110, 25);
        add(contactLabel);

        contactField = new JTextField();
        contactField.setBounds(150, 55, 300, 25);
        add(contactField);

        // login customer details
        if (customer != null) {
            ownerNameField.setText(customer.getName());
            contactField.setText(customer.getPhoneNumber());

            ownerNameField.setEditable(false);
            contactField.setEditable(false);
        }

        JLabel petNameLabel = new JLabel("Pet name:");
        petNameLabel.setBounds(20, 90, 110, 25);
        add(petNameLabel);

        petNameField = new JTextField();
        petNameField.setBounds(150, 90, 300, 25);
        add(petNameField);

        JLabel typeLabel = new JLabel("Pet type:");
        typeLabel.setBounds(20, 125, 110, 25);
        add(typeLabel);

        JPanel typePanel = new JPanel(
            new FlowLayout(FlowLayout.LEFT, 6, 0)
        );
        typePanel.setBounds(145, 125, 310, 25);

        ButtonGroup group = new ButtonGroup();

        for (int i = 0; i < Pet.PET_TYPES.length; i++) {
            typeButtons[i] = new JRadioButton(Pet.PET_TYPES[i]);
            typeButtons[i].setActionCommand("updateTotal");
            typeButtons[i].addActionListener(this);

            group.add(typeButtons[i]);
            typePanel.add(typeButtons[i]);
        }

        typeButtons[0].setSelected(true);
        add(typePanel);

        JLabel weightLabel = new JLabel("Weight (kg):");
        weightLabel.setBounds(20, 160, 110, 25);
        add(weightLabel);

        weightField = new JTextField("5");
        weightField.setBounds(150, 160, 150, 25);
        add(weightField);

        JLabel ageLabel = new JLabel("Age (years):");
        ageLabel.setBounds(20, 195, 110, 25);
        add(ageLabel);

        ageField = new JTextField("1");
        ageField.setBounds(150, 195, 150, 25);
        add(ageField);

        JLabel serviceLabel = new JLabel("Service:");
        serviceLabel.setBounds(20, 230, 110, 25);
        add(serviceLabel);

        serviceBox = new JComboBox<>(GroomingService.getServices());
        serviceBox.setBounds(150, 230, 300, 25);
        serviceBox.setActionCommand("updateTotal");
        serviceBox.addActionListener(this);
        add(serviceBox);

        JLabel addOnLabel = new JLabel("Add-ons:");
        addOnLabel.setBounds(20, 265, 110, 100);
        add(addOnLabel);

        GroomingService[] addOns = GroomingService.getAddOns();

        JPanel addOnPanel = new JPanel(
            new GridLayout(addOns.length, 1)
        );
        addOnPanel.setBounds(150, 265, 300, 100);

        for (int i = 0; i < addOns.length; i++) {
            addOnBoxes[i] = new JCheckBox(
                addOns[i].getName()
                + " (+P" + addOns[i].getPrice() + ")"
            );

            addOnBoxes[i].setActionCommand("updateTotal");
            addOnBoxes[i].addActionListener(this);
            addOnPanel.add(addOnBoxes[i]);
        }

        add(addOnPanel);

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setBounds(20, 375, 110, 25);
        add(dateLabel);

        dateBox = new JComboBox<>();
        dateBox.setBounds(150, 375, 150, 25);

        LocalDate tomorrow = LocalDate.now().plusDays(1);

        for (int i = 0; i < 14; i++) {
            dateBox.addItem(tomorrow.plusDays(i).toString());
        }

        add(dateBox);

        JLabel timeLabel = new JLabel("Time:");
        timeLabel.setBounds(20, 410, 110, 80);
        add(timeLabel);

        timeList = new JList<>(Appointment.TIME_SLOTS);
        timeList.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        timeList.setSelectedIndex(0);

        JScrollPane timeScroll = new JScrollPane(timeList);
        timeScroll.setBounds(150, 410, 200, 80);
        add(timeScroll);

        JLabel notesLabel = new JLabel("Notes:");
        notesLabel.setBounds(20, 500, 110, 50);
        add(notesLabel);

        notesArea = new JTextArea();
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);

        JScrollPane notesScroll = new JScrollPane(notesArea);
        notesScroll.setBounds(150, 500, 300, 50);
        add(notesScroll);

        totalLabel = new JLabel();
        totalLabel.setBounds(20, 565, 350, 25);
        add(totalLabel);

        JButton saveButton = new JButton("Book Appointment");

        if (existing != null) {
            saveButton.setText("Save Changes");
        }

        saveButton.setBounds(150, 605, 160, 30);
        saveButton.setActionCommand("save");
        saveButton.addActionListener(this);
        add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(320, 605, 100, 30);
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);
        add(cancelButton);

        getRootPane().setDefaultButton(saveButton);

        // Ina-update ang estimated total kapag binago ang weight.
        weightField.getDocument().addDocumentListener(
            new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent event) {
                    updateTotal();
                }

                @Override
                public void removeUpdate(DocumentEvent event) {
                    updateTotal();
                }

                @Override
                public void changedUpdate(DocumentEvent event) {
                    updateTotal();
                }
            }
        );
    }

    private String getSelectedPetType() {
        String type = Pet.PET_TYPES[0];

        for (int i = 0; i < typeButtons.length; i++) {
            if (typeButtons[i].isSelected()) {
                type = Pet.PET_TYPES[i];
            }
        }
        return type;
    }

    private ArrayList<GroomingService> getSelectedAddOns() {
        ArrayList<GroomingService> chosen = new ArrayList<>();
        GroomingService[] addOns = GroomingService.getAddOns();

        for (int i = 0; i < addOnBoxes.length; i++) {
            if (addOnBoxes[i].isSelected()) {
                chosen.add(addOns[i]);
            }
        }
        return chosen;
    }

    private void updateTotal() {
        if (totalLabel == null || serviceBox == null) {
            return;
        }
        int weight;

        try {
            weight = Integer.parseInt(weightField.getText().trim());
        } catch (NumberFormatException e) {
            totalLabel.setText("Estimated total: enter a valid weight");
            return;
        }

        if (weight < 1 || weight > Pet.MAX_WEIGHT) {
            totalLabel.setText(
                "Estimated total: enter weight from 1 to "
                + Pet.MAX_WEIGHT + " kg"
            );
            return;
        }

        GroomingService service =
            (GroomingService) serviceBox.getSelectedItem();

        if (service == null) {
            return;
        }

        // age 0 used only for price estimate
        Pet pet = Pet.create(
            getSelectedPetType(),
            petNameField.getText(),
            weight,
            0
        );

        int total = Appointment.calculateTotal(
            service,
            pet,
            getSelectedAddOns()
        );

        totalLabel.setText("Estimated total: P" + total);
    }

    private void fillFromExisting() {
        Pet pet = existing.getPet();

        ownerNameField.setText(existing.getOwnerName());
        contactField.setText(existing.getContact());
        petNameField.setText(pet.getName());
        weightField.setText(String.valueOf(pet.getWeight()));
        ageField.setText(String.valueOf(pet.getAge()));

        for (int i = 0; i < typeButtons.length; i++) {
            if (Pet.PET_TYPES[i].equals(pet.getPetType())) {
                typeButtons[i].setSelected(true);
            }
        }

        for (int i = 0; i < serviceBox.getItemCount(); i++) {
            if (serviceBox.getItemAt(i).getName().equals(
                    existing.getService().getName())) {
                serviceBox.setSelectedIndex(i);
            }
        }
        GroomingService[] addOns = GroomingService.getAddOns();

        for (GroomingService chosen : existing.getAddOns()) {
            for (int i = 0; i < addOns.length; i++) {
                if (addOns[i].getName().equals(chosen.getName())) {
                    addOnBoxes[i].setSelected(true);
                }
            }
        }

        boolean found = false;

        for (int i = 0; i < dateBox.getItemCount(); i++) {
            if (dateBox.getItemAt(i).equals(existing.getDate())) {
                found = true;
            }
        }

        if (!found) {
            dateBox.insertItemAt(existing.getDate(), 0);
        }

        dateBox.setSelectedItem(existing.getDate());
        timeList.setSelectedValue(existing.getTimeSlot(), true);
        notesArea.setText(existing.getNotes());
    }

    private void saveAppointment() {
        String ownerName = ownerNameField.getText().trim();
        String contact = contactField.getText().trim();
        String petName = petNameField.getText().trim();

        String date = (String) dateBox.getSelectedItem();
        String time = timeList.getSelectedValue();

        if (ownerName.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please enter the owner's name."
            );
            ownerNameField.requestFocus();
            return;
        }
        if (contact.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please enter the contact number."
            );
            contactField.requestFocus();
            return;
        }
        if (petName.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please enter the pet's name."
            );
            petNameField.requestFocus();
            return;
        }
        if (ownerName.contains("|") || contact.contains("|")
                || petName.contains("|")) {
            JOptionPane.showMessageDialog(this, "The | character is not allowed in the name or contact.");
            return;
        }

        int weight;
        int age;

        try {
            weight = Integer.parseInt(weightField.getText().trim());
            age = Integer.parseInt(ageField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter whole numbers for weight and age.");
            return;
        }

        if (weight < 1 || weight > Pet.MAX_WEIGHT) {
            JOptionPane.showMessageDialog(this, "Weight must be from 1 to " + Pet.MAX_WEIGHT + " kg.");
            weightField.requestFocus();
            return;
        }

        if (age < 0 || age > Pet.MAX_AGE) {
            JOptionPane.showMessageDialog(this, "Age must be from 0 to " + Pet.MAX_AGE + " years.");
            ageField.requestFocus();
            return;
        }

        if (date == null
                || LocalDate.parse(date).isBefore(LocalDate.now())) {

            JOptionPane.showMessageDialog(this, "Please choose today or a future date.");
            return;
        }

        if (time == null) {
            JOptionPane.showMessageDialog(this, "Please choose a time slot.");
            return;
        }

        int ignoreId = -1;

        if (existing != null) {
            ignoreId = existing.getId();
        }

        if (AppointmentDatabase.isSlotTaken(date, time, ignoreId)) {
            JOptionPane.showMessageDialog(this, "That date and time is already booked.\n" + "Please choose another time slot.");
            return;
        }

        Pet pet = Pet.create(
            getSelectedPetType(),
            petName,
            weight,
            age
        );

        GroomingService service = (GroomingService) serviceBox.getSelectedItem();

        ArrayList<GroomingService> addOns = getSelectedAddOns();
        String notes = notesArea.getText();

        if (existing == null) {
            String ownerUsername = "";

            if (customer != null) {
                ownerUsername = customer.getUsername();
            }

            result = new Appointment(
                ownerUsername,
                ownerName,
                contact,
                pet,
                service,
                addOns,
                date,
                time,
                notes
            );
        } else {
            result = new Appointment(
                existing.getId(),
                existing.getOwnerUsername(),
                existing.getOwnerName(),
                existing.getContact(),
                pet,
                service,
                addOns,
                date,
                time,
                notes,
                existing.getStatus()
            );
        }

        saved = true;
        dispose();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String command = event.getActionCommand();

        if (command.equals("updateTotal")) {
            updateTotal();
        }

        if (command.equals("save")) {
            saveAppointment();
        }

        if (command.equals("cancel")) {
            dispose();
        }
    }
}