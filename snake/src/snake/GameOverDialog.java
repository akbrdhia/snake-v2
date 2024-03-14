package snake;

import java.awt.GridLayout;
import javax.swing.*;
import java.awt.event.*;

public class GameOverDialog extends JDialog {

    private Board board;

    public GameOverDialog(Board board, int score) {
        this.board = board;
        setTitle("Game Over");
        setLayout(new GridLayout(3, 1));

        JLabel lblSkor = new JLabel("Skor: " + score);
        add(lblSkor);
        
        JButton btnMainLagi = new JButton("Main Lagi");
        btnMainLagi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.resetGame();
                dispose();
            }
        });

        JButton btnKeluar = new JButton("Keluar");
        btnKeluar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                System.exit(0);
            }
        });

        add(btnMainLagi);
        add(btnKeluar);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

   
}
