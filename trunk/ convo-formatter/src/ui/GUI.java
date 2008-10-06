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
import javax.swing.JTabbedPane;

/**
 * The graphical user interface component of the software.  This is
 * responsible for creating and displaying the GUI you see on screen.
 * 
 * @author Andrew Correa
 */
public class GUI implements Runnable
{
  /**
   * The method that initiates the entire operation of this class.  Required
   * by the {@link Runnable} interface.
   */
  public void run()
  {
    // Make sure we have nice window decorations.
    JFrame.setDefaultLookAndFeelDecorated(false);

    // Create and set up the window.
    JFrame frame = new JFrame("Conversation formatter");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JTabbedPane tabs = new JTabbedPane();
    tabs.addTab("Tab!", makeTab1());

    frame.getContentPane().add(tabs, BorderLayout.CENTER);
    
    // Display the window.
    frame.pack();
    frame.setVisible(true);
  }

  public JPanel makeTab1()
  {
    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));

    JLabel label = new JLabel("Here's a label.");
    panel.add(label);

    JButton exit_button = new JButton("I'm a Swing button!");
    exit_button.setMnemonic(KeyEvent.VK_I);
    exit_button.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        System.out.println("Exit button pressed.");
      }
    });
    label.setLabelFor(exit_button);
    panel.add(exit_button);

    return panel;
  }
  
  public static void main(String[] args)
  {
    javax.swing.SwingUtilities.invokeLater(new GUI());
  }
}
