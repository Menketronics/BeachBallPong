package rps;

import java.io.Serializable;

public class MyGameOutput  implements Serializable
{    
	private static final long serialVersionUID = 1L;
	MyGame gameStatus=null;

	public MyGameOutput(MyGame gs)
	{
		gameStatus =gs;
	}

}
