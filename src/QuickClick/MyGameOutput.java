package QuickClick;
import java.io.Serializable;



public class MyGameOutput  implements Serializable
{
   MyGame myGame =null;   
   MyGameOutput(){};
   MyGameOutput(MyGame mygame)
   {
	   this.myGame = mygame;
   }
}