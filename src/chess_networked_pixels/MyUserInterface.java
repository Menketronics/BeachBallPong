package chess_networked_pixels;

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
import java.io.Serializable;

import javax.swing.JFrame;


class BoardDimensions implements Serializable
{
	int xstart=-1,  ystart=-1,  square_width=-1, square_height=-1;
	void setParms(int xstart, int ystart, int square_width, int square_height)
	{
		this.xstart = xstart;
		this.ystart = ystart;
		this.square_width = square_width;
		this.square_height = square_height;
	}
}

public class MyUserInterface extends JFrame implements GameNet_UserInterface
{
	GamePlayer myGamePlayer;
	MyGameInput myGameInput;
	MyGame myGame=null;

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
		boardDimensions = new BoardDimensions();
		//setResizable(false);
	}
	public void startUserInterface (GamePlayer player)
	{
		myGamePlayer = player;
		myGameInput = new MyGameInput();
		myGameInput.setName(player.getPlayerName());
		myGamePlayer.sendMessage(myGameInput);

		setVisible(true);
	}
	public void receivedMessage(Object ob)
	{
		MyGameOutput mg = (MyGameOutput)ob;
		myGame=mg.myGame;
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
		Insets insets = super.getInsets();
		int top = insets.top;
		int w = (d.width-insets.left - insets.right)/8;
		int h = (d.height-top - insets.bottom)/8;
		boardDimensions.setParms(insets.left, top, w, h);

		for (int i=0; i < 8; i++)
		{
			for (int j=0; j < 8; j++)
			{
				int x = j*w;
				int y = top + i*h;
				if ((i+j)%2==1)
				{
					g.setColor(Color.green);
					g.fillRect(x, y, w, h);
				}
				else
				{
					g.setColor(Color.gray);
					g.fillRect(x, y, w, h);
				}
			}    
		}// end of outer for loop

		if (myGame!= null)
			myGame.drawInPosition(g, boardDimensions);
				screen.drawImage(offScreenImage, 0,0, this);
	}   


	//*******************************************
	// Another Inner class 
	//*******************************************
	class MouseClickMonitor extends MouseAdapter
	{
		public void mousePressed(MouseEvent e)
		{
			int x= e.getX();
			int y = e.getY();
			myGameInput.setMouseCmd(MyGameInput.MOUSE_PRESSED, x, y, boardDimensions);
			myGamePlayer.sendMessage(myGameInput);

			repaint();
		}

		public void mouseReleased(MouseEvent e) 
		{
			int x= e.getX();
			int y = e.getY();
			myGameInput.setMouseCmd(MyGameInput.MOUSE_RELEASED, x, y, boardDimensions);
			myGamePlayer.sendMessage(myGameInput);

			repaint();
		}
		public void mouseDragged(MouseEvent e)
		{
			int x= e.getX();
			int y = e.getY();
			myGameInput.setMouseCmd(MyGameInput.MOUSE_DRAGGED, x, y, boardDimensions);
			myGamePlayer.sendMessage(myGameInput);

			repaint();
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

