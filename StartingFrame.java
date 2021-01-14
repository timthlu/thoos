/** 
 * this template can be used for a start menu
 * for your final project
**/

//Imports
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class StartingFrame extends JFrame {

	JFrame thisFrame;

	// Constructor - this runs first
	StartingFrame() {
		super();
		this.thisFrame = this; // lol

		// configure the window
		this.setSize(300, 200);
		this.setLocationRelativeTo(null); // start the frame in the center of
											// the screen
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);

		// Create a Panel for stuff
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

		// Create a JButton for the centerPanel
		JButton playButton = new JButton("Play");
		playButton.addActionListener(new StartButtonListener());

		JButton instructionButton = new JButton("How to play");
		instructionButton.addActionListener(new InstructionButtonListener());
		
		// Create a JButton for the centerPanel
		JLabel startLabel = new JLabel("Toohs", SwingConstants.CENTER);
		Font font = new Font("SansSerif", Font.BOLD, 24);
		startLabel.setFont(font);

		// Add all panels to the mainPanel according to border layout
		playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		instructionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.add(playButton);
		buttonPanel.add(instructionButton);
		mainPanel.add(startLabel, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		// add the main panel to the frame
		this.add(mainPanel);

		// Start the app
		this.setVisible(true);
	}

	// This is an inner class that is used to detect a button press
	class StartButtonListener implements ActionListener { // this is the
															// required class
															// definition
		public void actionPerformed(ActionEvent event) {
			System.out.println("Starting new Game");
			thisFrame.dispose();
			new GameFrame();
		}

	}

	class InstructionButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println("Showing instructions");
			thisFrame.dispose();
			new Instructions();
		}
	}

}