package Drawing_networked_cloneable;

import gameNet_cloneable.GamePlayer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel{
   // Drawing drawing = new Drawing(); // This will now come from the MyGame class
    MyGame myGame=null;

    Image offScreenImage = null;
    Dimension screenDimension = null;
    BoardDimensions boardDimensions = new BoardDimensions();
    GamePlayer myGamePlayer=null;
    MyGameInput myGameInput= new MyGameInput();
   
    
    public void setGamePlayer(GamePlayer gp)
    {
    	myGamePlayer = gp;
    	myGameInput.setName(myGamePlayer.getPlayerName());
    }
    
    public void newScreen(MyGame myGame)
    {
    	this.myGame = myGame;
    	repaint();
    }


    DrawingPanel()
    {
        MyMouseHandler mmh = new MyMouseHandler();
        addMouseListener(mmh);
        addMouseMotionListener(mmh);
    }
    public void paint(Graphics screen)
    {
        Dimension dimen = getSize();
        if (offScreenImage==null || !dimen.equals(screenDimension))
        {
            screenDimension = dimen;
            offScreenImage = createImage(dimen.width, dimen.height);
        }
        Graphics g = offScreenImage.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, dimen.width, dimen.height);
        
        boardDimensions.setParms(dimen);
        if (myGame == null)
        	return;

        myGame.drawing.draw(g, boardDimensions);

        screen.drawImage(offScreenImage, 0,0, this);
    }
    

    // INNER Class
    class MyMouseHandler extends MouseAdapter
    {     
    	Point getPoint(MouseEvent e)
    	{
    		return boardDimensions.toGeneric(e.getPoint());  		 
    	}
        public void mousePressed(MouseEvent e)
        {
        	Point p = getPoint(e);
        	myGameInput.setMousePoint(MyGameInput.MOUSE_PRESSED, p);
        	myGamePlayer.sendMessage(myGameInput);
            //drawing.mousePressed(getPoint(e));
            //repaint();  // We will repaint our drawing when this comes from the MyGame class
        }
        public void mouseReleased(MouseEvent e)
        {
        	Point p = getPoint(e);
        	myGameInput.setMousePoint(MyGameInput.MOUSE_RELEASED, p);
        	myGamePlayer.sendMessage(myGameInput);
        }
        public void mouseDragged(MouseEvent e)
        {
        	Point p = getPoint(e);
        	myGameInput.setMousePoint(MyGameInput.MOUSE_DRAGGED, p);
        	myGamePlayer.sendMessage(myGameInput);
        }
    }

    // End of INNER Class
}

