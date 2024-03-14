package snake;

import javax.swing.JFrame;

public class Snake extends JFrame {

    public Snake() {
        initUI();
    }

    private void initUI() {
        Board board = new Board();
        add(board);
        setResizable(false);
        pack();
        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Snake().setVisible(true);
    }
}
   