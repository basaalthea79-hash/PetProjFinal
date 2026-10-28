package gui;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import model.*;

public class Receipt implements ActionListener {
    private JDialog dialog;
    private Appointment appointment;
    private String text;
    
    public static void showReceipt(Appointment appointment, JFrame parent) {
        new Receipt(appointment, parent);
    }

    public Receipt(Appointment appointment, JFrame parent) {
        this.appointment = appointment;
        text = buildText(appointment);
        dialog = new JDialog(parent, "Receipt - Appointment #" + appointment.getId(),true);

        dialog.setSize(510, 650);
        dialog.setLayout(null);
        dialog.setLocationRelativeTo(parent);
        JTextArea area = new JTextArea(text);
        area.setEditable(false);
        area.setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBounds(20, 20, 455, 510);
        dialog.add(scroll);
        JButton save = new JButton("Save Receipt");
        save.setBounds(130, 550, 170, 35);
        save.addActionListener(this);
        dialog.add(save);
        JButton close = new JButton("Close");
        close.setBounds(315, 550, 100, 35);
        close.addActionListener(this);
        dialog.add(close);
        LoginFrame.styleForm(dialog.getContentPane());
        dialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand().equals("Close")) {
            dialog.dispose();
            return;
        }

        String fileName = "Receipt_" + appointment.getId() + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(text);
            JOptionPane.showMessageDialog(dialog, "Receipt saved: " + fileName);
        } catch (IOException error) {
            JOptionPane.showMessageDialog(dialog, "Could not save receipt: " + error.getMessage());
        }
    }

    private static String buildText(Appointment appointment) {
        ArrayList<String> items = appointment.getReceiptLines();
        ArrayList<Integer> prices = appointment.getReceiptPrices();
        Pet pet = appointment.getPet();
        String text = "PAWCARE GROOMING\n";
        text += "Appointment Receipt\n\n";
        text += "Appointment #: " + appointment.getId() + "\n";
        text += "Schedule: " + appointment.getDate() + " at " + appointment.getTimeSlot() + "\n";
        text += "Status: " + appointment.getStatus() + "\n";
        text += "Owner: " + appointment.getOwnerName() + "\n";
        text += "Contact: " + appointment.getContact() + "\n";
        text += "Pet: " + pet.getDescription() + "\n";
        text += "Notes: " + appointment.getNotes() + "\n\n";

        for (int i = 0; i < items.size(); i++) {
            text += items.get(i) + " : P " + prices.get(i) + "\n";
        }
        text += "\nTOTAL: P " + appointment.getPrice() + "\n";
        text += "THANK YOU!\n";
        return text;
    }
}