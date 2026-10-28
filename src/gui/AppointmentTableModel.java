package gui;
import javax.swing.table.DefaultTableModel;

public class AppointmentTableModel extends DefaultTableModel {
    public AppointmentTableModel(String[] columns) {
        super(columns, 0);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}