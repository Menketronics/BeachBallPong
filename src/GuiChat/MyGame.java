package GuiChat;

import gameNet.GameNet_CoreGame;

public class MyGame extends GameNet_CoreGame{

	public Object process(Object ob)


    {
        // Process takes the name and msg from the GameInputObj 
        // and copies it to the GameOutputObj
        
        MyGameInput myIn = (MyGameInput)ob;
        MyGameOutput myGameOutput = new MyGameOutput();


        myGameOutput.copyMsg(myIn); // Copy Input name and message to Output name and Message
        
        // Broadcast message to All Clients
        return myGameOutput;
    }

}

