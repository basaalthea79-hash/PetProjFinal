package main;
import gui.LoginFrame;
import model.AppointmentDatabase;
import model.UserDatabase;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main implements Runnable {
    public static void main(String[] args) {
        UserDatabase.load();
        AppointmentDatabase.load();
        SwingUtilities.invokeLater(new Main());
    }

    @Override
    public void run() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception error) {
        }

        new LoginFrame();
    }
}