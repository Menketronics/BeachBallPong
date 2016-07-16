package chess_networked_generic_coordinates;

import gameNet.GamePlayer;
import gameNet.GameNet_UserInterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;



public class MyUserInterface extends JFrame implements GameNet_UserInterface
{
	GamePlayer myGamePlayer;
	MyGameInput myGameInput;
	ChessGame chessGame=null;

	Image offScreenImage=null;
	int lastWidth=-1, lastHeight=-1;

	BoardDimensions boardDimensions = new BoardDimensions();

	MyUserInterface() {
		super("The chessboard is the world, the chessPieces are the phenomena of the universe");
		setSize(600, 600);
		
		ChessPiece.readInImages();
		
		MouseClickMonitor ml = new MouseClickMonitor();
		addMouseListener(ml);
		addMouseMotionListener(ml);
		
		addWindowListener(new Termination());
		//setResizable(false);
	}
	public void startUserInterface (GamePlayer player)
	{
		myGamePlayer = player;
		
		myGameInput = new MyGameInput();
		myGameInput.setName(player.getPlayerName());
		myGamePlayer.sendMessage(myGameInput); // Default is the "Connecting command"

		setVisible(true);
	}
	public void receivedMessage(Object ob)
	{
		MyGameOutput myGameOutput = (MyGameOutput)ob;
		chessGame = myGameOutput.myGame.chessGame;
		repaint();
	}


	public void paint(Graphics screen) {

		Dimension d = getSize();
		if (d.width != lastWidth || d.height != lastHeight)
		{
			lastWidth = d.width;
			lastHeight = d.height;
			offScreenImage = createImage(lastWidth, lastHeight);
		}
		Graphics g = offScreenImage.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0,0, d.width, d.height);
		
		Insets insets = getInsets();
		int top = insets.top;
		int left = insets.left;
		int square_width = (d.width - left - insets.right)/8;
		int square_height = (d.height - top - insets.bottom)/8;
		boardDimensions.setParms(left, top, square_width, square_height);

		// Color in the Board squares
		for (int i=0; i < 8; i++)
		{
			for (int j=0; j < 8; j++)
			{
				int x = left +  j*square_width;
				int y = top + i*square_height;
				if ((i+j)%2==1)
				{
					g.setColor(Color.green);
					g.fillRect(x, y, square_width, square_height);
				}
				else
				{
					g.setColor(Color.gray);
					g.fillRect(x, y, square_width, square_height);
				}
			}    
		}// end of outer for loop

		// Draw pieces in their current location
		if (chessGame != null)
		{
			chessGame.drawInPosition(g, boardDimensions);
		}
		// To avoid flicker we copy the offScreenImage to the real screen
		screen.drawImage(offScreenImage, 0,0, this);
	}   


	//*******************************************
	// Another Inner class to handle Mouse Events
	//*******************************************
	class MouseClickMonitor extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			DPoint dpoint =  boardDimensions.getDpoint(e.getX(), e.getY());
			if (dpoint != null)
			{
				myGameInput.setMouseCmd(MyGameInput.MOUSE_PRESSED, dpoint);
				myGamePlayer.sendMessage(myGameInput);
			}
		}

		public void mouseReleased(MouseEvent e) 
		{
			DPoint dpoint =  boardDimensions.getDpoint(e.getX(), e.getY());
			if (dpoint != null)
			{
				myGameInput.setMouseCmd(MyGameInput.MOUSE_RELEASED, dpoint);
				myGamePlayer.sendMessage(myGameInput);
			}
		}
		public void mouseDragged(MouseEvent e)
		{
			DPoint dpoint =  boardDimensions.getDpoint(e.getX(), e.getY());
			if (dpoint != null)
			{
				myGameInput.setMouseCmd(MyGameInput.MOUSE_DRAGGED, dpoint);
				myGamePlayer.sendMessage(myGameInput);
			}
		}

		

	}
	//*******************************************
	// Another Inner class 
	//*******************************************
	class Termination extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			myGamePlayer.doneWithGame();
			System.exit(0);
		}
	}



}

