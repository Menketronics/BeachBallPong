package ticTacToe;
import java.io.Serializable;

public class MyGameInput  implements Serializable
{
	private static final long serialVersionUID = 1L;
// command options
   static final int NO_COMMAND=-1;
   static final int JOIN_GAME=1;


   static final int SELECT_SQUARE=2;
   static final int DISCONNECTING=3;
   static final int RESETTING=4;
   
   int command=NO_COMMAND;  
   
   String myName;
   int row, col;   
   
   public void setName(String name)
   {
	   myName=name;
   }
}