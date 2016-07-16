package Drawing_networked_cloneable;

import gameNet_cloneable.CloneableObject;

import java.io.Serializable;


public class MyGameOutput  extends CloneableObject implements Serializable
{    
	MyGame myGame=null;

	public MyGameOutput(MyGame myGame)
	{
		this.myGame =myGame;
	}
	public void myCloneMethod( Object obj)
	{
		if (myGame != null)
			myGame = (MyGame)myGame.clone();
	}

}
