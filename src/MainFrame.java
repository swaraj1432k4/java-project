
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame {
    public static void main(String[] args) {
        MainWindow obj = new MainWindow();
        obj.mainwindow();
    }

}

class MainWindow {
    public void mainwindow() {
        JFrame frame = new JFrame("Floor Planner");
        // Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        // int width = screensize.width;
        // int height = screensize.height;

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MainPanel panel = new MainPanel();
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

}