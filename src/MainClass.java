import javax.swing.JFrame;

/**
 *
 */
public class MainClass {

    public static void main(String[] args) {
        final int FRAMEW = 400, FRAMEH = 423;
        Dynamics dy = new Dynamics(FRAMEW, FRAMEH - 23);
        JFrame frame = new JFrame("MINESWEEPER");
        frame.setSize(FRAMEW, FRAMEH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(dy);
        frame.setVisible(true);
    }

}
