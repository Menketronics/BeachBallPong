package Drawing_networked;

import gameNet.GameNet_UserInterface;
import gameNet.GamePlayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class MyUserInterface extends JFrame implements ActionListener, GameNet_UserInterface{
   
    DrawingPanel drawingPanel = new DrawingPanel();
    JButton polygon_done;
    JCheckBox filled ;
    JLabel currentDrawer;
    
    
    String[] colors = {"red", "green", "blue"};
    JRadioButton[] rbColors=new JRadioButton[colors.length];
    
    String[] shapes = {"rectangle", "oval","line","scribble", "polygon"};
    JRadioButton[] rbDrawType=new JRadioButton[shapes.length];
    
    GamePlayer myGamePlayer;
    MyGameInput myGameInput = new MyGameInput();
    
    MyGame myGame=null;
    

	@Override
	public void receivedMessage(Object ob) {
		MyGameOutput myGameOutput = (MyGameOutput)ob;
		myGame = myGameOutput.myGame;
		drawingPanel.newScreen(myGame);
		
		String cd = myGame.currentDrawer;		
		currentDrawer.setText((cd==null)?"":cd+ " drawing");
		
		DrawingProperties drawingProperties = myGame.drawing.drawingProperties;
		
		filled.setSelected(drawingProperties.filled);
		switch (drawingProperties.drawType)
		{
		case rectangle:
			filled.setVisible(true);
			polygon_done.setVisible(false);
			rbDrawType[0].setSelected(true);
			break;
		case oval:
			filled.setVisible(true);
			polygon_done.setVisible(false);
			rbDrawType[1].setSelected(true);
			break;
		case line:
			filled.setVisible(false);
			polygon_done.setVisible(false);
			rbDrawType[2].setSelected(true);
			break;
		case scribble:
			filled.setVisible(false);
			polygon_done.setVisible(false);
			rbDrawType[3].setSelected(true);
			break;
		case polygon:
			filled.setVisible(true);
			polygon_done.setVisible(true);
			rbDrawType[4].setSelected(true);
			break;			
		}
		if (drawingProperties.color.equals(Color.red))
		{
		    rbColors[0].setSelected(true);
		}
		else if (drawingProperties.color.equals(Color.green))
		{
		    rbColors[1].setSelected(true);			
		}
		else // must be blue
		{
		    rbColors[2].setSelected(true);			
		}

		
	}

	@Override
	public void startUserInterface(GamePlayer player) {
		myGamePlayer = player;
		drawingPanel.setGamePlayer(player);
		myGameInput.setName(myGamePlayer.getPlayerName());
		myGamePlayer.sendMessage(myGameInput);
	}

    MyUserInterface()
    {
        super("My Drawing Program");
         
        setSize(800, 400);
        addWindowListener( new Termination());
       
        myLayout();
        

        setVisible(true);
    }
    
    private void myLayout( )
    {      
        ButtonGroup shapeGroup= new ButtonGroup();
        ButtonGroup colorGroup= new ButtonGroup();
        
        setLayout(new BorderLayout());

        //Drawing in the Center
        add(drawingPanel, BorderLayout.CENTER);
        
        JPanel north = new JPanel();
        north.setLayout(new FlowLayout());
        
        currentDrawer = new JLabel("");
        north.add(currentDrawer);
        filled = new JCheckBox("filled");
        north.add(filled);
        filled.addActionListener(this); 
        
       
        // Create the Shape Radio buttons on the North
        for (int i= 0; i < shapes.length; i++)
        {
            JRadioButton rb = new JRadioButton(shapes[i]);
            rbDrawType[i] = rb;
            if (i == 0)
                rb.setSelected(true);
            shapeGroup.add(rb);
            north.add(rb);
            rb.addActionListener(this);
        }

        polygon_done = new JButton("polygon_done");
        polygon_done.setVisible(false);
        polygon_done.addActionListener(this);
        north.add(polygon_done);
        
        add(north, BorderLayout.NORTH);
        
        JPanel west = new JPanel();
        west.setLayout(new GridLayout(0, 1));

        //Create the Color radio buttons on the West
        for (int i=0; i < colors.length; i++)
        {
            JRadioButton rb = new JRadioButton(colors[i]);
            rbColors[i] = rb;
            if (i==0)
                rb.setSelected(true);
            colorGroup.add(rb);
            west.add(rb);
            rb.addActionListener(this);
        }
        add(west, BorderLayout.WEST);
        
    }
   

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String action = actionEvent.getActionCommand();
        System.out.println(action);
        
        switch(action)
        {
        case "filled":
        	myGameInput.filled = filled.isSelected();
        	myGameInput.setCmd(MyGameInput.SET_FILLED);
            break;
        case "red":
        	myGameInput.color = Color.red;
        	myGameInput.setCmd(MyGameInput.SET_COLOR);
            break;
        case "green":  
        	myGameInput.color = Color.green;
        	myGameInput.setCmd(MyGameInput.SET_COLOR);
            break;
        case "blue":
        	myGameInput.color = Color.blue;
        	myGameInput.setCmd(MyGameInput.SET_COLOR);
            break;
        case "rectangle":
        	myGameInput.drawType = DrawType.rectangle;
        	myGameInput.setCmd(MyGameInput.SET_DRAW_TYPE);
            break;
        case "oval":
        	myGameInput.drawType = DrawType.oval;
        	myGameInput.setCmd(MyGameInput.SET_DRAW_TYPE);
            break;
        case "line":
        	myGameInput.drawType = DrawType.line;
        	myGameInput.setCmd(MyGameInput.SET_DRAW_TYPE);
            break;

        case "scribble":
        	myGameInput.drawType = DrawType.scribble;
        	myGameInput.setCmd(MyGameInput.SET_DRAW_TYPE);
            break;
        case "polygon":
        	myGameInput.drawType = DrawType.polygon;
        	myGameInput.setCmd(MyGameInput.SET_DRAW_TYPE);
            break;
        case "polygon_done":
        	myGameInput.drawType = DrawType.polygon;
        	myGameInput.setCmd(MyGameInput.SET_DRAW_TYPE);
            break;        
        }
        myGamePlayer.sendMessage(myGameInput);
        
    }
    
	//*******************************************
	//  Inner class 
	//*******************************************
	class Termination extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			myGameInput.setCmd(MyGameInput.DISCONNECTING);
			myGamePlayer.sendMessage(myGameInput);
			myGamePlayer.doneWithGame();
			System.exit(0);
		}
	}


}