package QuickClick;

import gameNet.GameNet_UserInterface;
import gameNet.GamePlayer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;




public class MyUserInterface extends JFrame implements  GameNet_UserInterface
{   
	GamePlayer  myGamePlayer=null;

	String myName=null;
	JLabel statusLabel = new JLabel("");
	MyGameInput myGameInput = new MyGameInput();
	PaintPanel paintPanel=null;

	public MyUserInterface()
	{
		super("Quick Click");

	}

	public void receivedMessage(Object ob)
	{
		MyGameOutput myGameOutput = (MyGameOutput)ob;
		if (paintPanel != null)
			paintPanel.updatePaint(myGameOutput.myGame);
		else
			System.out.println("Thread is already sending  stuff before I am ready");
	}

	
	void sendMessage(int command)
	{		
		myGameInput.command = command;
		myGamePlayer.sendMessage(myGameInput);
	}


	public void startUserInterface (GamePlayer gamePlayer) {    
		myGamePlayer = gamePlayer; 
		myName=gamePlayer.getPlayerName();
		myGameInput.setName(myName);
		
		mylayout(); 
		
		sendMessage(MyGameInput.CONNECTING);

		addWindowListener(new Termination());

		setVisible(true); 
	} 
	private void mylayout()
	{
		setLayout(new BorderLayout());
		paintPanel = new PaintPanel( statusLabel,  myName, myGamePlayer);
		add(paintPanel, BorderLayout.CENTER);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout());     
		JButton b = new JButton("Reset");
		topPanel.add(b);
		b.addActionListener(new ActionMonitor()); 
		statusLabel.setText(myName);
		topPanel.add(statusLabel);
		add(topPanel, BorderLayout.NORTH);


		setSize(500,300);  
	}


	//*******************************************
	// Another Inner class
	//*******************************************
	class ActionMonitor implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{            
			sendMessage(MyGameInput.RESETTING); 
		}
	}

	//*******************************************************
	// Inner Class
	//*******************************************************

	class Termination extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			sendMessage(MyGameInput.DISCONNECTING);

			myGamePlayer.doneWithGame();
			System.exit(0);
		}
	}



}

