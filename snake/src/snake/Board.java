package snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener {

    // Ukuran board
    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    // Ukuran titik (snake's body part) dan jumlah maksimal titik
    private final int UKURAN_TITIK = 10;
    private final int SEMUA_TITIKS = 900;
    // Jumlah posisi acak untuk buah
    private final int RAND_POS = 29;
    // Delay untuk timer (kecepatan pergerakan snake)
    private final int DELAY = 140;
    // Array posisi X dan Y untuk setiap titik snake
    private final int x[] = new int[SEMUA_TITIKS];
    private final int y[] = new int[SEMUA_TITIKS];
    // Jumlah titik awal snake
    private int titiks = 3;
    // Posisi awal buah
    private int apel_x;
    private int apel_y;
    // Arah pergerakan snake
    private boolean arahKiri = false;
    private boolean arahKanan = true;
    private boolean arahAtas = false;
    private boolean arahBawah = false;
    // Status permainan
    private boolean inGame = true;
    // Timer untuk menggerakkan snake
    private Timer timer;
    // Gambar untuk snake, buah, dan kepala
    private Image bola;
    private Image apel;
    private Image kepala;
    // Skor permainan
    private int score = 0;
    // Dialog game over
    private GameOverDialog gameOverDialog;
    
    // Konstruktor Board
    public Board() {
        initBoard();
    }

    // Inisialisasi Board
    private void initBoard() {
        score = 0;
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        inGame = true;
        loadImages();
        initGame();
    }

    // Load gambar untuk snake, buah, dan kepala
    private void loadImages() {
        ImageIcon iit = new ImageIcon("src/Gambar/badan.png"); // Ganti dengan path gambar Anda
        bola = iit.getImage();
        ImageIcon iia = new ImageIcon("src/Gambar/buah.png"); // Ganti dengan path gambar Anda
        apel = iia.getImage();
        ImageIcon iik = new ImageIcon("src/Gambar/kepala.png"); // Ganti dengan path gambar Anda
        kepala = iik.getImage();
    }

    // Inisialisasi permainan
    private void initGame() {
        for (int z = 0; z < titiks; z++) {
            x[z] = B_WIDTH / 2;
            y[z] = B_HEIGHT / 2;
        }

        locateApel();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    // Override paintComponent untuk menggambar komponen pada JPanel
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

   private void doDrawing(Graphics g) {
    // Memeriksa apakah permainan masih berlangsung
    if (inGame) {
        // Menggambar buah pada posisi acak
        g.drawImage(apel, apel_x, apel_y, this);

        // Menggambar setiap titik dari snake
        for (int z = 0; z < titiks; z++) {
            // Jika z adalah kepala snake, gambar kepala, jika tidak, gambar badan snake
            if (z == 0) {
                g.drawImage(kepala, x[z], y[z], this);
            } else {
                g.drawImage(bola, x[z], y[z], this);
            }
        }
        // Menyinkronkan grafis dengan sistem
        Toolkit.getDefaultToolkit().sync();
    } else {
        // Jika permainan berakhir, panggil metode gameOver() untuk menampilkan dialog game over
        gameOver(g);
    }

    // Menetapkan font dan warna untuk skor
    Font smallFont = new Font("Helvetica", Font.BOLD, 14);
    g.setColor(Color.white);
    g.setFont(smallFont);

    // Menggambar skor di pojok kiri atas
    g.drawString("Skor: " + score, 10, 20);
}
   
    // Menangani peristiwa game over
    private void gameOver(Graphics g) {
        timer.stop();
        gameOverDialog = new GameOverDialog(this, score);
    }

    // Mengatur ulang permainan
    public void resetGame() {
        score = 0;
        titiks = 3;
        arahKiri = false;
        arahKanan = true;
        arahAtas = false;
        arahBawah = false;
        inGame = true;

        for (int z = 0; z < titiks; z++) {
            x[z] = B_WIDTH / 2;
            y[z] = B_HEIGHT / 2;
        }

        locateApel();
        timer.start();
    }

    // Memeriksa apakah snake memakan buah
    private void checkApel() {
        if ((x[0] == apel_x) && (y[0] == apel_y)) {
            titiks++;
            score++;
            locateApel();
        }
    }

    // Menggerakkan snake
    private void move() {
        for (int z = titiks; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        if (arahKiri) {
            x[0] -= UKURAN_TITIK;
        }
        if (arahKanan) {
            x[0] += UKURAN_TITIK;
        }
        if (arahAtas) {
            y[0] -= UKURAN_TITIK;
        }
        if (arahBawah) {
            y[0] += UKURAN_TITIK;
        }
    }

    // Memeriksa tabrakan dengan batas board atau tubuh snake sendiri
    private void checkCollision() {
        for (int z = titiks; z > 0; z--) {
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
        if (x[0] >= B_WIDTH) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
    }

    // Menempatkan buah pada posisi acak
    private void locateApel() {
        int r = (int) (Math.random() * RAND_POS);
        apel_x = ((r * UKURAN_TITIK));
        r = (int) (Math.random() * RAND_POS);
        apel_y = ((r * UKURAN_TITIK));
    }

    // Menangani peristiwa actionPerformed
    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApel();
            checkCollision();
            move();
        }
        repaint();
    }

    // Kelas adapter untuk meng-handle input keyboard
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!arahKanan)) {
                arahKiri = true;
                arahAtas = false;
                arahBawah = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!arahKiri)) {
                arahKanan = true;
                arahAtas = false;
                arahBawah = false;
            }

            if ((key == KeyEvent.VK_UP) && (!arahBawah)) {
                arahAtas = true;
                arahKanan = false;
                arahKiri = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!arahAtas)) {
                arahBawah = true;
                arahKanan = false;
                arahKiri = false;
            }
        }
    }
}
