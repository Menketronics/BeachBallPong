package Drawing_networked_cloneable;

import gameNet_cloneable.GameNet_CoreGame;

import java.io.Serializable;
import java.util.ArrayList;

public class MyGame extends GameNet_CoreGame implements Serializable, Cloneable{

	public Drawing drawing = new Drawing();
	public ArrayList<String> drawers = new ArrayList<String>();
	public String currentDrawer=null;

	public Object clone( )
	{
		MyGame myg =null;

		myg =  (MyGame)super.clone( );//Invocation of clone 
		//in the base class Object	
		myg.drawers = (ArrayList<String>)myg.drawers.clone();
		myg.drawing = (Drawing)myg.drawing.clone();

		return myg;
	}

	public Object process(Object ob)
	{
		MyGameInput myGameInput= (MyGameInput)ob;
		getClientIndex(myGameInput.sendersName); //Adds new users if this is their first time

		switch(myGameInput.cmd)
		{
		case MyGameInput.MOUSE_PRESSED:
			if (currentDrawer == null)
				currentDrawer = myGameInput.sendersName;
			if (myGameInput.sendersName.equals(currentDrawer))
				drawing.mousePressed(myGameInput.point);
			break;
		case MyGameInput.MOUSE_RELEASED:
			if (myGameInput.sendersName.equals(currentDrawer))
			{
				drawing.mouseReleased(myGameInput.point);
				currentDrawer = null;
			}
			break;
		case MyGameInput.MOUSE_DRAGGED:
			if (myGameInput.sendersName.equals(currentDrawer))
				drawing.mouseDragged(myGameInput.point);
			break;
		case MyGameInput.CONNECTING:
			break;
		case MyGameInput.DISCONNECTING:
			drawers.remove(myGameInput.sendersName);
			break;
		case MyGameInput.SET_COLOR:
			drawing.setColor(myGameInput.color);
			break;
		case MyGameInput.SET_DRAW_TYPE:
			drawing.setDrawType(myGameInput.drawType);
			break;
		case MyGameInput.SET_FILLED:
			drawing.setFilled(myGameInput.filled);
			break;
		}

		return new MyGameOutput(this);
	}

	int getClientIndex(String name)
	{
		int index = drawers.indexOf(name);
		if (index < 0)
		{
			drawers.add(name);
			index = drawers.size() -1;
		}
		return index;
	}


}


