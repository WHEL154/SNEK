import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int OBJECT_SIZE = 25;
    static final int GAME_OBJECTS = (SCREEN_WIDTH * SCREEN_HEIGHT) / OBJECT_SIZE;
    static final int DELAY = 75;
    final int x[] = new int[GAME_OBJECTS];
    final int y[] = new int[GAME_OBJECTS];
    int bodyParts = 4;
    int foodEaten;
    int foodX;
    int foodY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    int highScore;
    JButton retryButton;
    JLabel highScoreLabel;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.gray);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        loadScore();
        start();
    }

    public void start() {
        newFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            for (int i = 0; i < SCREEN_HEIGHT / OBJECT_SIZE; i++) {
                g.drawLine(i * OBJECT_SIZE, 0, i * OBJECT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * OBJECT_SIZE, SCREEN_WIDTH, i * OBJECT_SIZE);
            }
            g.setColor(Color.orange);
            g.fillOval(foodX, foodY, OBJECT_SIZE, OBJECT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.yellow);
                    g.fillRect(x[i], y[i], OBJECT_SIZE, OBJECT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], OBJECT_SIZE, OBJECT_SIZE);
                }
            }

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + foodEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + foodEaten)) / 2,
                    g.getFont().getSize());

            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            g.drawString("High Score: " + highScore, 10, g.getFont().getSize() + 10);

        } else {
            gameOver(g);
        }
    }

    public void newFood() {
        foodX = random.nextInt((int) (SCREEN_WIDTH / OBJECT_SIZE)) * OBJECT_SIZE;
        foodY = random.nextInt((int) (SCREEN_HEIGHT / OBJECT_SIZE)) * OBJECT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U':
                y[0] = y[0] - OBJECT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + OBJECT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - OBJECT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + OBJECT_SIZE;
                break;
        }
    }

    public void checkFood() {
        if ((x[0] == foodX) && (y[0] == foodY)) {
            bodyParts++;
            foodEaten++;
            newFood();
        }
    }

    public void checkCollision() {
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        if (x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] < 0 || y[0] > SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
            saveScore();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metricsScore = getFontMetrics(g.getFont());
        g.drawString("Score: " + foodEaten, (SCREEN_WIDTH - metricsScore.stringWidth("Score: " + foodEaten)) / 2,
                g.getFont().getSize());

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metricsGO = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metricsGO.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        retryButton = new JButton("Retry");
        retryButton.addActionListener(new RetryButtonListener());
        retryButton.setBounds(SCREEN_WIDTH / 2 - 50, SCREEN_HEIGHT / 2 + 50, 100, 50);
        add(retryButton);
    }

    public void resetGame() {
        bodyParts = 4;
        foodEaten = 0;
        direction = 'R';
        running = true;
        retryButton.setVisible(false);
        newFood();
        for (int i = 0; i < bodyParts; i++) {
            x[i] = 0;
            y[i] = 0;
        }
        timer.restart();
        repaint();
    }

    public void saveScore() {
        if (foodEaten > highScore) {
            highScore = foodEaten;
            try {
                Path path = Paths.get("highscore.txt");
                Files.writeString(path, String.valueOf(highScore));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadScore() {
        try {
            Path path = Paths.get("highscore.txt");
            if (Files.exists(path)) {
                String score = Files.readString(path);
                highScore = Integer.parseInt(score);
            } else {
                highScore = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFood();
            checkCollision();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

    public class RetryButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            resetGame();
        }
    }
}




