package chess_networked_generic_coordinates;

import java.io.Serializable;


public class MyGameOutput  implements Serializable
{    
	MyGame myGame=null;

	public MyGameOutput(MyGame mg)
	{
		myGame =mg;
	}

}
