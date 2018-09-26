import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class Game extends JPanel implements KeyListener, ActionListener{

    int width = 400;
    int height = 400;

    static int size = 20;

    private Random rnd = new Random();

    private Deque<Position> snake = new ArrayDeque<>();
    private Set<Position> empties = new HashSet<>();

    static boolean area[][] = new boolean[size][size];

    Map<Integer, Boolean> keys = new HashMap<>();

    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;

    private Position newElement;

    private ImageIcon element;
    private ImageIcon increase;
    private ImageIcon gameOver;

    private Timer timer;
    private int delay = 60;
    boolean isGameOver = false;

    public Game() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        Button button = new Button("Reset Game");
        button.setVisible(true);
        button.addActionListener(e -> {
            revalidateGame();
            isGameOver = false;
            timer.restart();
            timer.start();
            grabFocus();
            repaint();
        });
        setLayout(new BorderLayout());
        add(button, BorderLayout.SOUTH);

        timer = new Timer(delay, this);

        revalidateGame();
        isGameOver = false;
        timer.start();
    }

    private void revalidateGame() {
        right = false;
        left = false;
        up = false;
        down = false;
        empties.clear();
        snake.clear();
        for (int i = 0; i < size - 3; i++) {
            snake.addFirst(new Position(i,0));
        }
        clearArea();
        mapSnakeToArea();
        gatherEmptyPoints();
        newElement = getNewElementPosition();
    }

    private void clearArea() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                area[i][j] = false;
            }
        }
    }

    public void paint(Graphics g) {

        g.setColor(Color.WHITE);
        g.fillRect(0,0, width, height);

        element = new ImageIcon(Game.class.getResource("element.png"));
        increase = new ImageIcon(Game.class.getResource("increase.png"));

        increase.paintIcon(this, g, newElement.x * size, newElement.y * size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (area[i][j]) {
                    element.paintIcon(this, g, i * size, j * size);
                }
            }
        }

        if (isGameOver) {
            gameOver = new ImageIcon(Game.class.getResource("game-over.png"));
            gameOver.paintIcon(this, g, (width/2) - size, (height/2) - size);
            timer.stop();
        }

    }

    private boolean isSnakeEatHimself() {
        if (snake.size() < 1) {
            return false;
        }

        for (Position p : snake) {
            if (snake.getFirst().equals(p)) {

            }
        }
//        for (int i = 0; i < snake.size(); i++) {
//            if (i > 0 && head.equals(snake.getFirst())) {
//                return true;
//            }
//            i++;
//        }
        return false;
    }

    private void gatherEmptyPoints() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (!area[i][j]) {
                    empties.add(new Position(i, j));
                }
            }
        }
    }

    private Position getNewElementPosition() {
        int index = rnd.nextInt(empties.size());
        int i = 0;
        for (Position p : empties) {
            if (i == index) {
                return p;
            }
            i++;
        }
        return null;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_RIGHT && !left) {
            right = true;
            left = false;
            up = false;
            down = false;
        }

        if(e.getKeyCode() == KeyEvent.VK_LEFT && !right) {
            right = false;
            left = true;
            up = false;
            down = false;
        }

        if(e.getKeyCode() == KeyEvent.VK_UP && !down) {
            right = false;
            left = false;
            up = true;
            down = false;
        }

        if(e.getKeyCode() == KeyEvent.VK_DOWN && !up) {
            right = false;
            left = false;
            up = false;
            down = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int xTail = snake.getLast().x;
        int yTail = snake.getLast().y;
        area[xTail][yTail] = false;

        int x = snake.getFirst().x;
        int y = snake.getFirst().y;

        if (right) {
            if (x + 1 < size) {
                ++x;
            } else {
                x = 0;
            }
        }
        if (left) {
            if (x - 1 >= 0) {
                --x;
            } else {
                x = size - 1;
            }
        }
        if (down) {
            if (y + 1 < size) {
                ++y;
            } else {
                y = 0;
            }
        }
        if (up) {
            if (y - 1 >= 0) {
                --y;
            } else {
                y = size - 1;
            }
        }

        if (right || left || up || down) {
            snake.addFirst(new Position(x, y));
            if (!isNewElementEaten()) {
                empties.remove(snake.getLast());
                snake.removeLast();
            }
            empties.remove(snake.getFirst());
            mapSnakeToArea();
            isGameOver = isSnakeEatHimself();
            repaint();
        }
    }



    boolean isNewElementEaten() {
        if (snake.getFirst().equals(newElement)) {
            newElement = getNewElementPosition();
            return true;
        }
        return false;
    }

    private void mapSnakeToArea() {
        for (Position p : snake) {
            area[p.x][p.y] = true;
        }
    }

    public class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Position)) return false;
            Position position = (Position) o;
            return x == position.x &&
                    y == position.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
