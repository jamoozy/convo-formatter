package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The graphical user interface component of the software.  This is responsible
 * for creating and displaying the GUI you see on screen.
 * 
 * @author Andrew Correa
 */
public class GUI implements Runnable
{
	/**
	 * The method that initiates the entire operation of this class.
	 * Required by the {@link Runnable} interface.
	 */
	public void run()
	{
		// Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);

		// Create and set up the window.
		JFrame frame = new JFrame("Bless your mother.");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel label = new JLabel("Here's a label.");

		JButton button = new JButton("I'm a Swing button!");
		button.setMnemonic(KeyEvent.VK_I);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.out.println("button pressed");
			}
		});
		label.setLabelFor(button);

		JPanel pane = new JPanel(new GridLayout(0, 1));
		pane.add(button);
		pane.add(label);
		pane.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));

		frame.getContentPane().add(pane, BorderLayout.CENTER);
		
		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		javax.swing.SwingUtilities.invokeLater(new GUI());
	}
}
