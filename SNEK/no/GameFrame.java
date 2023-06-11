import javax.swing.*;

public class GameFrame extends JFrame {

    GameFrame() {
        add(new GamePanel());
        setTitle("SNEK");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
    }
}
