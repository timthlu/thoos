import javax.swing.*;

public class GameFrame extends JFrame {
    JFrame thisFrame;

    public GameFrame() {
        super("Toohs");
        this.thisFrame = this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(1200, 800);
        GraphicsPanel mainPanel = new GraphicsPanel(thisFrame);
        getContentPane().add(mainPanel);
        setVisible(true);
    }
}