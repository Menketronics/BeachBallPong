package ticTacToe;



import gameNet.GameNet_UserInterface;
import gameNet.GamePlayer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
// New Import for Events


public class MyUserInterface extends JFrame 
implements   ActionListener, GameNet_UserInterface
{     
	private MyGame myGame=null;
	private GamePlayer  myGamePlayer=null;
	private String myName="";
	private MyGameInput myGameInput = new MyGameInput();

	private JButton[][] buttonArr = new JButton[3][3];
	private JButton resetButton = new JButton("Reset");
	private JLabel messageLabel = new JLabel("");
	private JLabel nameLabel = new JLabel("");

	private Termination closeMonitor = new Termination();
	

	public MyUserInterface()
	{
		super("Tic Tac Toe");   
	}


	public void startUserInterface (GamePlayer gamePlayer) {    
		myGamePlayer = gamePlayer; 
		myName=gamePlayer.getPlayerName();
		myGameInput.setName(myName);

		sendMessage(MyGameInput.JOIN_GAME);    

		addWindowListener(this.closeMonitor);
		screenLayout();
		setVisible(true); 
	} 


	private void sendMessage(int command)
	{
		myGameInput.command = command;
		myGamePlayer.sendMessage(myGameInput);
	}

	private void sendMessageSelection(int row, int col)
	{
		myGameInput.row = row;
		myGameInput.col =col;
		sendMessage(MyGameInput.SELECT_SQUARE);
	}




	public void actionPerformed(ActionEvent e)
	{
		for (int row=0; row < 3; row++)
		{
			for (int col=0; col < 3; col++)
			{
				if (e.getSource() == buttonArr[row][col])
				{
					if (myGame != null && myGame.checkAvailability(myName, row, col))
					{
						sendMessageSelection(row, col);
					}

				}
			}
		}
		if (e.getSource() == resetButton)
		{
			sendMessage(MyGameInput.RESETTING);  
		}
	}

	public void receivedMessage(Object ob)
	{
		MyGameOutput myGameOutput = (MyGameOutput)ob;
		myGame = myGameOutput.myGame;

		String msg= myGame.getStatus(myName);
		String turnMsg = myGame.getTurnInfo(myName);
		String extendedName =  myGame.getExtendedName(myName);

		nameLabel.setText(extendedName);   
		messageLabel.setText(turnMsg + " ------- " + msg); 

		if ( myGame.gameInProgress())
			resetButton.setVisible(false);
		else
			resetButton.setVisible(true);

		for (int row=0; row < 3; row++)
		{
			for (int col=0; col < 3; col++)
			{
				String label = myGame.getButtonLabel(row, col);
				buttonArr[row][col].setText(label);
			}
		}

	}


	private void screenLayout()
	{
		setLayout(new BorderLayout());  

		setSize(500,500);     
		JPanel centerPanel = new JPanel();
		Font myFont = new Font("TimesRoman", Font.BOLD, 48);

		for (int i=0; i < 3; i++)
		{
			for (int j=0; j < 3; j++)
			{
				JButton b =new JButton(" ");
				b.addActionListener(this);
				b.setFont(myFont);
				centerPanel.add(b);
				buttonArr[i][j] = b;
			}
		}
		centerPanel.setLayout(new GridLayout(3,3));
		add (centerPanel, BorderLayout.CENTER);

		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(0,1));
		northPanel.add(nameLabel);

		JPanel topPanel = new JPanel(); 	  
		topPanel.setLayout(new FlowLayout());
		topPanel.add(resetButton);
		resetButton.addActionListener(this);
		topPanel.add(messageLabel);
		northPanel.add(topPanel);

		add(northPanel, BorderLayout.NORTH);

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

