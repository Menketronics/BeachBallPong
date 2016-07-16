package rps;

import gameNet.GamePlayer;
import gameNet.GameNet_UserInterface;

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
		private MyGameInput myGameInput;
		private JTextArea resultsTextArea = new JTextArea(20, 70);
		private JTextField pickTextField = new JTextField(5);
		
		public MyUserInterface()
		{
			super("RPS Game");
		}
		
		public void startUserInterface (GamePlayer player)
		{
			myGamePlayer = player;
			myGameInput = new MyGameInput();
			myGameInput.setName(myGamePlayer.getPlayerName());
			
		    myLayout();
			
			myGameInput.setPick(MyGameInput.CONNECTING); // Send a msg to connect to the game
			myGamePlayer.sendMessage(myGameInput);	
		}
		
		
		public void receivedMessage(Object ob)
		{
			MyGameOutput mg = (MyGameOutput)ob;
			String playerName = myGamePlayer.getPlayerName();
			String status = mg.gameStatus.publish_status(playerName);
			resultsTextArea.setText(status);
		}
		
		// We go here when hit the enter key on the pick pickTextField
		public void actionPerformed(ActionEvent e) {
			String str = pickTextField.getText();
						
			char pick=MyGameInput.NO_PICK;
			if ("q".equals(str))
			{
				exitProgram();
				return;
			}
			
			if ("r".equals(str))
				pick = MyGameInput.ROCK;
			else if ("p".equals(str))
				pick=MyGameInput.PAPER;
			else if ("s".equals(str))
				pick=MyGameInput.SCISSORS;
			
			myGameInput.setPick(pick);
			if (pick != MyGameInput.NO_PICK)
				myGamePlayer.sendMessage(myGameInput);
			else
				resultsTextArea.append(" Bad Input: " + str);

		}
		
		// Nice to let people know that you are going away.
		private void exitProgram()
		{
			myGameInput.setPick(MyGameInput.EXITING);
			myGamePlayer.sendMessage(myGameInput);
			myGamePlayer.doneWithGame();
			System.exit(0);
		}
		
		// Inner Class
		class Termination extends WindowAdapter
		{
			public void windowClosing(WindowEvent e)
			{
				exitProgram();
			}
		}

		// Setup all of the screen stuff
		private void myLayout()
		{		
			setLayout(new BorderLayout());
			JPanel topPanel = new JPanel();
			topPanel.setLayout(new GridLayout(0,1));
			pickTextField.setBackground(Color.yellow);
			topPanel.add(new JLabel(myGamePlayer.getPlayerName()));
			topPanel.add(new JLabel("Enter r p s q for (Rock, Paper, Scissors, Quit)"));
			topPanel.add(pickTextField);
			add(topPanel, BorderLayout.NORTH);
			
			resultsTextArea.setBackground(Color.lightGray);
			JScrollPane scrolledText = new JScrollPane(resultsTextArea);
			scrolledText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			scrolledText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			
			add(scrolledText, BorderLayout.CENTER);
			pickTextField.addActionListener(this);
			addWindowListener (new Termination());
			 
			setSize(800, 600);
			setVisible(true);
		}
		

	}
