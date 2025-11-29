import javax.swing.JFrame;

public class Frame extends JFrame{
    public Frame() {
        Screen screen = new Screen();

        this.setVisible(true);
        this.setTitle("Shape");

        this.add(screen);
        this.pack();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }
}
