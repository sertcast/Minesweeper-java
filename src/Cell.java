import java.awt.*;

/**
 *
 */
public class Cell {
    private int col, row, x, y, size, num = 0, txtX, txtY;
    private boolean mine, revealed, initialized = false, flagged = false;

    public Cell(int col, int row, int size) {
        this.x = col * size;
        this.y = row * size;
        this.col = col;
        this.row = row;
        this.revealed = false;
        this.size = size;
        this.mine = false;

    }

    public boolean isMine() {
        return this.mine;
    }

    public void show(Graphics g) {
        if (!initialized) {
            this.setTextPos(g);
        }


        if (revealed && mine) {
            g.setColor(Color.red);
            g.fillRect(x, y, size, size);
        } else if (revealed) {
            g.setColor(Color.lightGray);
            g.fillRect(x, y, size, size);
            if (num != 0) {
                g.setColor(Color.black);
                g.drawString("" + num, txtX, txtY);
            }
        } else if (flagged) {
            g.setColor(Color.lightGray);
            g.fillRect(x, y, size, size);
            g.setColor(Color.green);
            g.fillRect(x + 5, y + 5, size - 10, size - 10);
        }
        g.setColor(Color.black);
        g.drawRect(x, y, size, size);


    }

    private void setTextPos(Graphics g) {
        g.setFont(new Font("Comic Sans MS", Font.BOLD, this.size * 1 / 2));
        this.txtX = this.x
                + (this.size - g.getFontMetrics().stringWidth("" + num)) / 2;
        this.txtY = this.y + ((this.size - g.getFontMetrics().getHeight()) / 2)
                + g.getFontMetrics().getHeight();
    }

    public void setNum(int num) {
        if (this.mine) {
            this.num = -1;
            return;
        }

        this.num = num;
    }

    public void setMine(boolean m) {
        this.mine = m;
    }

    public int getNum() {
        return this.num;
    }

    /**
     * reveals the cell
     *
     * @param field the whole field
     * @return boolean true if game is over, meaning that a mine is being revealed
     */
    public boolean reveal(Cell[][] field) {
        if (flagged) {
            return false;
        }

        if (this.mine) {
            this.revealed = true;
            return true;
        } else if (this.num == 0) {
            this.revealed = true;
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    try {
                        if (!field[row + i][col + j].revealed)
                            field[row + i][col + j].reveal(field);
                    } catch (ArrayIndexOutOfBoundsException e) {

                    }

                }
            }

        }
        this.revealed = true;

        return false;
    }

    public boolean isFlagged() {
        return this.flagged;
    }

    public void flag() {
        this.flagged = !this.flagged;
    }

    public boolean isRevealed() {
        return this.revealed;
    }
}
