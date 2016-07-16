package ticTacToe;
import gameNet.GameNet_CoreGame;

import java.io.Serializable;
import java.util.ArrayList;



public class MyGame extends GameNet_CoreGame implements Serializable
{
	private static final long serialVersionUID = 1L;
	private ArrayList<String> currPlayers = new ArrayList<String>();
	
	// Note that the TttLogic is private and not directly available.  You must 
	// go through the public methods in this class. 
	
	private TttLogic tttl = new TttLogic();

	public MyGame()
	{
		tttl.clearBoard();
	}
	// Process commands from each of the game players

	public Object process(Object inputs)
	{
		MyGameInput myGameInput = (MyGameInput)inputs;
		
		// Note that this routine will add the player if he isn't currently in the 
		// game and there is room (i.e. < 2 players)
		int clientIndex = getClientIndex(myGameInput.myName);
		
		if (clientIndex < 0)
		{
			System.out.println("Already have 2 players");
			return null; // Ignore input
		}
		
		switch (myGameInput.command)
		{
		case MyGameInput.JOIN_GAME:
			break;
		case MyGameInput.SELECT_SQUARE:
			tttl.makeSelection(clientIndex, myGameInput.row, myGameInput.col);
			break;
		case MyGameInput.DISCONNECTING:
			currPlayers.remove(myGameInput.myName);		
			break;
		case MyGameInput.RESETTING:
			tttl.clearBoard();               
			break;
		default: /* ignore */
		}
		
		// Send game back to all clients
		MyGameOutput myGameOutput = new MyGameOutput(this);
		return myGameOutput;
	}
	
	// Get the proper label for each button in the 3x3 grid
	public String getButtonLabel(int row, int col)
	{
		return ""+tttl.getButtonLabel(row, col);
	}
	
	// returns your name with either " -- O" or " -- X" appended
	public String getExtendedName(String myName)
	{
		 char myMarker =(( getYourIndex(myName)==0)? 'O' : 'X' );
		 return myName + " --  " + myMarker;
	}
	
	// Returns True if the Game is still going
	public boolean gameInProgress()
	{
		return tttl.gameInProgress();
	}
	
	// Returns whether you won, lost, or the Game is in progress
	public String getStatus(String myName)
	{
		int index = getYourIndex(myName);
		return tttl.getGameStatus(index);
	}
	
	// returns whether you can select a given 3x3 grid based on the current turn.
	public boolean checkAvailability(String myName, int row, int col)
	{
		int index = getYourIndex(myName);
		return tttl.checkAvailability(index,  row,  col);
		
	}
	
	// Returns Information about whose turn it is
	public String getTurnInfo(String myName)
	{
		if (!gameInProgress())
			return " Game Over ";
		
		int index = getYourIndex(myName);
		if (tttl.isTurn(index))
			return "Your Turn";
		String otherClient = otherPlayerName(myName);
		return otherClient+"'s Turn";
	}

	// If you have already connected, then this will return your index (0 or 1). 
	// If you are new and we currently have less than 2 players then you are added
	// to the game and your index is returned (0 or 1)
	// If we already have 2 players, then this will return -1
	
	private int getClientIndex(String name)
	{
		// The following will return -1 if the name can't be found
		int retval = currPlayers.indexOf(name);
		
		if (retval < 0 && currPlayers.size() < 2)
		{
			retval = currPlayers.size();
			currPlayers.add(name);
			if (currPlayers.size() == 2)
			{
				// Game ready to go.
			}
		}
		return retval;
	}
	
	// If you are already in the game, your index will be returned (0 or 1)
	// Otherwise -1 is returned ... you are never added with this routine.
	private int getYourIndex(String name)
	{
		return currPlayers.indexOf(name);
	}
	
	// This returns the other Player's name if he exists.  A null is returned if he doesn't exist.
	private String otherPlayerName(String yourName)
	{
		if (currPlayers.size() < 2)
			return null;
		if (yourName.equals(currPlayers.get(0)))
			return currPlayers.get(1);
		else
			return currPlayers.get(0);
	}
	

}