import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Dynamics.java
 * <p>
 * Description:
 *
 * @author Mert Kaya
 * @version 1.0.1(created : Nov 2, 2018, updated : Nov 7, 2018)
 */
public class Dynamics extends JPanel implements MouseListener,
        MouseMotionListener, KeyListener, ActionListener {

    private Timer tm = new Timer(5, this);
    private Cell field[][];
    private int frameW, frameH, cellSize;
    private int numOfColumns = 20;
    private int numMines = 10;
    private int numFlags = numMines;
    private boolean lost = false, won = false;

    public Dynamics(int frameW, int frameH) {
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);

        this.frameH = frameH;
        this.frameW = frameW;
        this.cellSize = frameW / numOfColumns;
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
        if (lost) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
            g.drawString("Game Over", 100, 100);
            tm.stop();
        } else if (won) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
            g.drawString("CONGRATS! YOU WON!", 100, 100);
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
        System.out.println();

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

    public void mousePressed(MouseEvent e) {
        if (!lost && SwingUtilities.isLeftMouseButton(e)) {
            int xIndex = e.getX() / this.cellSize;
            int yIndex = e.getY() / this.cellSize;
            //reveals the clicked cell, if the cell contains a mine, lost will equal to false
            try {
                lost = field[yIndex][xIndex].reveal(field);
            } catch (ArrayIndexOutOfBoundsException ex) {
                if (xIndex == field.length && yIndex == field.length) {
                    lost = field[xIndex - 1][yIndex - 1]
                            .reveal(field);
                }
            }
        } else if (!lost && SwingUtilities.isRightMouseButton(e)) {
            int xIndex = e.getX() / this.cellSize;
            int yIndex = e.getY() / this.cellSize;
            //reveals the clicked cell, if the cell contains a mine, lost will equal to false
            try {
                if (field[yIndex][xIndex].isFlagged()) {
                    numFlags++;
                    field[yIndex][xIndex].flag();
                } else if (numFlags != 0 && !field[yIndex][xIndex].isRevealed()) {
                    numFlags--;
                    field[yIndex][xIndex].flag();
                }
            } catch (ArrayIndexOutOfBoundsException ex) {
                if (xIndex == field.length && yIndex == field.length) {
                    if (field[yIndex - 1][xIndex - 1].isFlagged()) {
                        numFlags++;
                        field[yIndex - 1][xIndex - 1].flag();
                    } else if (numFlags != 0 && !field[yIndex][xIndex].isRevealed()) {
                        numFlags--;
                        field[yIndex - 1][xIndex - 1].flag();
                    }

                }
            }
        }
        if (numFlags == 0) {
            boolean winCheck = true;
            for (Cell[] row : field) {
                for (Cell cell : row) {
                    if (cell.isMine() && !cell.isFlagged()) {
                        winCheck = false;
                        break;
                    }
                }
                if (!winCheck)
                    break;
            }
            won = winCheck;
        }

        if (lost || won)
            revealAll();

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == 'R') {
            init();
            lost = false;
            won = false;
            numFlags = numMines;
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
