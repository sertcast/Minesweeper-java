import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Dynamics.java
 * <p>
 * Description:
 *
 * @author Mert Kaya
 * @version 1.0.1(created : Nov 2, 2018, updated : Nov 3, 2018)
 */
public class Dynamics extends JPanel implements MouseListener,
        MouseMotionListener, KeyListener, ActionListener {

    private Timer tm = new Timer(5, this);
    private Cell field[][];
    private int frameW, frameH, cellSize;
    private int numMines = 20;
    private boolean gameOver = false;

    public Dynamics(int frameW, int frameH) {
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);

        this.frameH = frameH;
        this.frameW = frameW;
        this.cellSize = frameW / 10;
        this.field = new Cell[frameH / cellSize][frameW / cellSize];

        this.init();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.white);
        g.drawRect(0, 0, frameW, frameH);
        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field[row].length; col++) {
                field[row][col].show(g);
            }
        }
        tm.start();
        if (gameOver) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
            g.drawString("Game Over", 100, 100);
            tm.stop();
        }

    }

    public void actionPerformed(ActionEvent e) {

        repaint();
    }

    /**
     * used to initialize the field
     */
    public void init() {
        // initializes each cell
        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field[row].length; col++) {
                field[row][col] = new Cell(col, row, this.cellSize);
            }
        }

        //adds mines to the field
        for (int n = 0; n < numMines; n++) {
            int y = (int) (Math.random() * field.length);// the row number of the mine
            int x = (int) (Math.random() * field[y].length);// the column number of the mine

            //if the chosen cell is already a mine, chooses another one
            while (field[y][x].isMine()) {
                y = (int) (Math.random() * field.length);
                x = (int) (Math.random() * field[y].length);
            }

            //sets the cell as a mine
            field[y][x].setMine(true);
        }

        //initializes the numbers in each cell
        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field[row].length; col++) {

                int num = 0;//the number that the cell will display

                //loops through the surrounding cells
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        try {
                            if (field[row + i][col + j].isMine()) {
                                num++;
                            }
                        } catch (ArrayIndexOutOfBoundsException e) {
                        }
                    }
                }
                field[row][col].setNum(num);
            }
        }
        //used to check the code
        for (Cell[] row : field) {
            for (Cell cell : row) {
                int num = cell.getNum();
                if (num >= 0)
                    System.out.print(num + " ");
                else
                    System.out.print("m ");
            }
            System.out.println();
        }
    }

    /**
     * reveals the whole field
     */
    public void revealAll() {
        for (int row = 0; row < field.length; row++) {
            for (int col = 0; col < field[row].length; col++) {
                field[row][col].reveal(field);
            }
        }
    }

    /**
     * the method is called when the mouse is pressed
     *
     * @param e
     */
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            //reveals the clicked cell, if the cell contains a mine, gameOver will equal to false
            try {
                gameOver = field[e.getY() / this.cellSize][e.getX()
                        / this.cellSize].reveal(field);
            } catch (ArrayIndexOutOfBoundsException ex) {
                gameOver = field[field.length - 1][field.length - 1]
                        .reveal(field);
            }
        }
        if (gameOver)
            revealAll();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 'R') {
            init();
            gameOver = false;
            repaint();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }


}
