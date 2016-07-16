package Drawing_networked;

import java.io.Serializable;


public class MyGameOutput  implements Serializable
{    
	MyGame myGame=null;

	public MyGameOutput(MyGame myGame)
	{
		this.myGame =myGame;
	}

}
