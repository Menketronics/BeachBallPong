package QuickClick;

import gameNet.GamePlayer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

public	class PaintPanel extends JPanel
	{
		Image gOffScreenImage=null;
		int lastWidth=-1, lastHeight=-1;
		MyGame myGame=null;
		JLabel statusLabel;
		String myName;
		MyGameInput myGameInput= new MyGameInput();
		GamePlayer myGamePlayer;

		BoardDimensions boardDimensions = new BoardDimensions();

		PaintPanel(JLabel statusLabel, String myName, GamePlayer gamePlayer)
		{
			this.statusLabel = statusLabel;
			this.myName = myName;
			myGameInput.setName(myName);
			myGamePlayer= gamePlayer;
			addMouseListener(new MouseClickMonitor());
		}
		public void updatePaint(MyGame g)
		{
			myGame =g;
			repaint();
		}
		
	

		public void paint(Graphics g)
		{
			if (myGame == null) return;
			
			Insets insets = super.getInsets();		 
			Dimension d = getSize();
			if (lastWidth != d.width || lastHeight != d.height)
			{ 
				gOffScreenImage = createImage(getBounds().width, getBounds().height);
				lastWidth = d.width;
				lastHeight = d.height;
			}	    
			Graphics offScreenGraphics = gOffScreenImage.getGraphics(); 
			
			offScreenGraphics.setColor(Color.white);
			offScreenGraphics.fillRect(0, 0, d.width, d.height);
			
			statusLabel.setText(myName + "'s Screen: " +myGame.getScore());
			
			boardDimensions.setParms(insets.top, insets.left,
					d.width-insets.left -insets.right , d.height - insets.top -insets.bottom);
			
			offScreenGraphics.setColor(Color.black);
			
			// Draw Disks
			for (int i=0; i < MyGame.NUM_DISKS; i++)
			{
				Disk disk = myGame.disks[i];
				if (disk.isInPlay())
				{
					offScreenGraphics.setColor(disk.color);
					DPoint dp = boardDimensions.toPaintPoint(disk.dp);
					int x = (int)dp.x;
					int y = (int)dp.y;
					int r= (int)(boardDimensions.toPaintScaleX(MyGame.diskRadius));
					offScreenGraphics.fillOval(x-r, y-r, 2*r, 2*r);
				}
			}
			g.drawImage(gOffScreenImage, 0, 0, this); 

		}  

		//*******************************************
		// Another Inner class of PaintPanel
		//*******************************************
		class MouseClickMonitor extends MouseAdapter
		{
			public void mousePressed(MouseEvent e)
			{	 
				DPoint dpoint = boardDimensions.toGenericPoint( e.getPoint());
				if (dpoint != null)
				{
					myGameInput.dpoint = dpoint;
					myGameInput.command = MyGameInput.CLICK;
				    myGamePlayer.sendMessage(myGameInput);
				}
			}

		}



	}
