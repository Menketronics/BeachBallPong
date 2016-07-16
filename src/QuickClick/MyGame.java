package QuickClick;

import gameNet.GameControl;
import gameNet.GameNet_CoreGame;


import java.io.Serializable;
import java.util.ArrayList;

class Player implements Serializable
{
	int count=0;
	String name;
	Player(String n)
	{
		name=n;
	}
	public String toString()
	{
		return name + " = "+ count;
	}
}


public class MyGame extends GameNet_CoreGame implements Serializable, Runnable
{	
	transient GameControl gameControl ;
	static final int NUM_DISKS = 20;

	static final double fieldHeight =50.0;
	static final double fieldWidth=100.0;
	static final double diskRadius=4.0;

	static final int sleepTime=30;
	static final double OUT_OF_BOUNDS = -fieldWidth;

	Disk[] disks=  new Disk[NUM_DISKS];


	boolean continueToPlay = true;

	ArrayList<Player>players = new ArrayList<Player>();


	public void startGame(GameControl g)
	{
		this.gameControl = g;
		Thread t = new Thread(this);
		restartGame();
		t.start();
	}
	public void run()
	{
		while(continueToPlay)
		{
			try
			{
				Thread.sleep(sleepTime);
				for (int i=0; i < NUM_DISKS; i++)
				{
					disks[i].update();
				}

				MyGameOutput myGameOutput = new MyGameOutput(this);
				gameControl.putMsgs(myGameOutput);
			} catch (InterruptedException e){}
		}
	}

	// Process commands from each of the game players

	public MyGameOutput process(Object inputs)
	{
		MyGameInput myGameInput = (MyGameInput)inputs;
		switch (myGameInput.command)
		{
		case MyGameInput.CONNECTING:
			// This routine will add a new user if the 
			//  player doesn't already exist
			getClientIndex(myGameInput.myName);
			break;
		case MyGameInput.CLICK:
			//System.out.println("checking x="+ myIn.x + " y="+ myIn.y);
			for (int i=0; i < NUM_DISKS; i++)
			{
				if (disks[i].insideDisk(myGameInput.dpoint, diskRadius))
				{ // The click nailed a disk
					
					int index = getClientIndex(myGameInput.myName);
					if (index >= 0)
						players.get(index).count+=1;
					break; // Only get one ball per click
				}
			}
			break;
		case MyGameInput.DISCONNECTING:
			int index = getClientIndex(myGameInput.myName);
			if (index >= 0)
				players.remove(index);
			break;
		case MyGameInput.RESETTING:
			restartGame();
			break;
		default: /* ignore */
		}
		MyGameOutput myGameOutput = new MyGameOutput();
		myGameOutput.myGame = this;
		return myGameOutput;
	}

	

	

	// Find index of existing player.  If he doesn't exist add him.
	// There is no limit to the number of clients to this game
	private int getClientIndex(String name)
	{
		for (int i=0; i < players.size(); i++)
		{
			if (name.equals(players.get(i).name) )
			{
				return i;
			}
		}
		players.add(new Player(name));


		return players.size() -1;
	}


	

	private void restartGame()
	{
		for (int i=0; i < players.size(); i++)
		{
			players.get(i).count=0;
		}
		for (int i=0; i < NUM_DISKS; i++)
		{
			disks[i] = new Disk(); // Generates Random Disks
		}
	}
	
	




	public String getScore()
	{
		String retStr="";
		for (int i=0; i < players.size(); i++)
			retStr += players.get(i).toString()+"   ";

		return retStr;
	}
}



