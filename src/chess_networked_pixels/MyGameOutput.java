package chess_networked_pixels;

import java.io.Serializable;


public class MyGameOutput  implements Serializable
{    
	MyGame myGame=null;

	public MyGameOutput(MyGame mg)
	{
		myGame =mg;
	}

}
