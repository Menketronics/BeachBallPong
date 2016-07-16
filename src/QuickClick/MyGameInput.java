package QuickClick;
import java.io.Serializable;

public class MyGameInput implements Serializable
{
// command options
   static final int CONNECTING=1;
   static final int CLICK=2;
   static final int DISCONNECTING=100;
   static final int RESETTING=200;
   
   int command=CONNECTING;  
   
   String myName;
   DPoint dpoint;  
   
   public void setName(String name)
   {
	   myName=name;
   }   
    
}


class DPoint implements Serializable
{
	double x,y;
	
	DPoint()
	{
		x = MyGame.fieldWidth *Math.random();
		y = MyGame.fieldHeight*Math.random();
	}
	DPoint(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
}
