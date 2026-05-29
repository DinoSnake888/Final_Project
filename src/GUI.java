import javax.swing.*;
import java.awt.*;

class GUI {

    JFrame frame;
    JPanel panel;

    public GUI(){

        frame = new JFrame("Library Interface");
        panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));
        panel.setLayout(new GridLayout(6,1));

        frame.add(panel, BorderLayout.CENTER);
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


    }
}
