/**
* This template can be used as reference or a starting point
* for your final summative project
* @author Mangat
**/

//Graphics &GUI imports

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameOver extends JFrame {

	// class variable (non-static)
	static double x, y;
	static InstructionPanel instructionPanel;
	JFrame thisFrame;

	// Constructor - this runs first
	GameOver(int player) {
		super("Game Over");
		this.thisFrame = this;
		// Set the frame to full screen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		this.setUndecorated(true); //Set to true to remove title barcc
		//setBackground(new Color(0,0,0,0));

		// Set up the game panel (where we put our graphics)
		instructionPanel = new InstructionPanel(player);
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		JButton backButton = new JButton("< Back");
		backButton.addActionListener(new BackButtonListener());
		instructionPanel.add(backButton);
		mainPanel.add(instructionPanel);
		this.add(mainPanel);
		this.requestFocusInWindow(); // make sure the frame has focus

		this.setVisible(true);
	} // End of Constructor

	class InstructionPanel extends JPanel {
		private BufferedImage img;

		public InstructionPanel(int player) {
			try {
				img = ImageIO.read(new File("player" + player + ".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			int w = getWidth();
			int h = getHeight();
			int iw = img.getWidth();
			int ih = img.getHeight();
			double xScale = (double) w / iw;
			double yScale = (double) h / ih;
			double scale = Math.min(xScale, yScale); // scale to fit
			// Math.max(xScale, yScale); // scale to fill
			int width = (int) (scale * iw);
			int height = (int) (scale * ih);
			int x = (w - width) / 2;
			int y = (h - height) / 2;
			g2.drawImage(img, x, y, width, height, this);
		}
	}

	class BackButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			thisFrame.dispose();
			new StartingFrame();
		}

	}
}