package GuiChat;

import gameNet.GameNet_UserInterface;
import gameNet.GamePlayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


class MyUserInterface extends JFrame 
	implements GameNet_UserInterface, ActionListener
{
	private GamePlayer myGamePlayer;
	private JTextArea chatStatusTextArea = new JTextArea(20, 70);
	private JTextField chatInputTextField = new JTextField(70);

	private MyGameInput myGameInput;
	private String lineSeparator = System.getProperty("line.separator");
	
	public MyUserInterface()
	{
		super("Chat Room");
	}
	
	
	public void startUserInterface (GamePlayer player)
	{
		myGamePlayer = player;
		myGameInput = new MyGameInput(player.getPlayerName());
		
		// Boring screen things
		myLayout();
		
		myGameInput.setMsg("just entered chat room");
		myGamePlayer.sendMessage(myGameInput);
	}
	
	
	public void receivedMessage(Object ob)
	{
		chatStatusTextArea.setText( ob.toString()  +
				lineSeparator + chatStatusTextArea.getText());
	}
	
	public void actionPerformed(ActionEvent e) {
		String str = chatInputTextField.getText();
		if (!myGamePlayer.GameIsAlive() ||
				"quit".equals(str))
		{
			exitProgram();
		}
		else
		{
			myGameInput.setMsg(str);
			myGamePlayer.sendMessage(myGameInput);
			chatInputTextField.setText("");
		}
	}
	
	// Nice to let people know you are leaving
	private void exitProgram()
	{
		myGameInput.setMsg("exitting chat room");
		myGamePlayer.sendMessage(myGameInput);
		myGamePlayer.doneWithGame();
		System.exit(0);
	}
	
	private void myLayout()
	{
		setLayout(new BorderLayout());
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(2,1));
		chatInputTextField.setBackground(Color.yellow);
		topPanel.add(new JLabel("Enter Message to Send to other Chatters (quit to exit)"));
		topPanel.add(chatInputTextField);
		add(topPanel, BorderLayout.NORTH);
		
		chatStatusTextArea.setBackground(Color.lightGray);
		JScrollPane scrolledText = new JScrollPane(chatStatusTextArea);
		scrolledText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrolledText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		add(scrolledText, BorderLayout.CENTER);
		chatInputTextField.addActionListener(this);
		addWindowListener (new Termination());

		setSize(800, 600);
		setVisible(true);
	}
	
	// Inner Class
	class Termination extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			exitProgram();
		}
	}

}