import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame implements ActionListener {

    private JButton playButton;

    public MainMenu() {
        setTitle("Snake");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Snake Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.PLAIN, 20));
        playButton.addActionListener(this);

        mainPanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(playButton, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public void display() {
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == playButton) {
            setVisible(false);
            GameFrame gameFrame = new GameFrame();
            gameFrame.setVisible(true);
        }
    }
}

