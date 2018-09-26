import javax.swing.*;
import java.awt.*;

public class Main {

    private static final int WINDOW_HEIGHT = 451;
    private static final int WINDOW_WIDTH = 406;

    public static void main(String[] args) {

        JFrame frame = new JFrame();
        Game game = new Game();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        frame.setBounds(dim.width/2-WINDOW_WIDTH/2, dim.height/2-WINDOW_HEIGHT/2, WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setBackground(Color.GRAY);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);
    }
}
