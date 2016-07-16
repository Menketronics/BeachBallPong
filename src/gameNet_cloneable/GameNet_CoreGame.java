package gameNet_cloneable;
/**
 * 
 All of your MyGame classes must extend this abstract class.
 This implies that your MyGame class must have code for the abstract method:
 Process
 *
 */
public abstract class GameNet_CoreGame implements Cloneable{
	
	public Object clone( )
    {
	   Object object=null;
       try
       {
          object =  super.clone( );//Invocation of clone 
                               //in the base class Object
         
       }
       catch(CloneNotSupportedException e)
       {//This should not happen.
    	   System.out.println("GameNet_CoreGame.clone: CloneNotSupported error: "+e);
    	   e.printStackTrace(System.out);        
       }
       
	 return object;
    }
	
	/**
	 * You will override startGame if you need to send out output that was not the direct result of 
	 * user inputs.  For example you are creating a Pong game and you need to 
	 * let everyone know the current location of the Pong ball.
	 * The normal things you might do in the startGame method:
	 <ul>
	 <li>Since startGame is called just as the Server is up and running, this is a good place
	 to start any threads.</li>
	 <li>Secondly, it is good to save the address of the gameControl object.  Whenever you want to
	 send out updates that are not precipitated by a call to "process" (i.e. user inputs), then
	 you can call gameControl.putMsg to send out extra updates to all of your clients. </li>
	 </ul>
	 *   
	 * @param gameControl - If you need an instance of gameControl, 
	 * you can override startGame to get a reference to gameControl.
	 */
	public void startGame(GameControl gameControl)
	{
		
	}
	
	/**
	 * The "process" routine must be present in your myGame class. Every time a client calls SendMessage through
	 * his GamePlayer class, the object sent is passed to the MyGame "process" routine.   
	 * @param inputObj - Normally this will be cast to a MyUserInput.  This is an object sent through one of the 
	 * clients GamePlayer.sendMessage calls.  MyUserInput must be Serializable. 
	 * @return - "process" must return an instance of MyGameOutput.  This class must be Serializable. 
	 * A return of null results in nothing being sent.  Any sent object is received in each of the Client's
	 * receivedMessage methods.  
	 */
	public abstract Object process(Object inputObj);
}
