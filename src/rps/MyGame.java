package rps;

import gameNet.GameNet_CoreGame;

import java.io.Serializable;
import java.util.ArrayList;

public class MyGame extends GameNet_CoreGame implements Serializable{
	
	private static final long serialVersionUID = 1L;
		
	//List of players (max of 2) currently playing
	private ArrayList<String> currPlayers = new ArrayList<String>();
	
	// RPS Rounds played so far
	private ArrayList<RpsRound> rpsRounds = new ArrayList<RpsRound>();
		
	public Object process(Object ob)
	{
		MyGameInput myGameInput= (MyGameInput)ob;
		int playerIndex = getClientIndex(myGameInput.sendersName);
		// If we already have 2 players, we will ignore additional attempts to 
		// connect;
		if (playerIndex < 0)
		{
			System.out.println("We already have 2 players ... ignoring new request");
			return null;
		}
		if (myGameInput.pick == MyGameInput.EXITING)
		{
			currPlayers.remove(myGameInput.sendersName);
			System.out.println("Player Exiting: " + myGameInput.sendersName);
		}
		else if (myGameInput.pick != MyGameInput.CONNECTING)
		{
			if (RpsRound.choiceName(myGameInput.pick)== null)
			{
				System.out.println("Illegal Pick: " + myGameInput.pick);
				return null;
			}
			// Add the User's Pick to the RPS Rounds
			if (!add2RpsRounds(playerIndex, myGameInput.pick))
				return null;  // This user has already picked in this round
		}
		
		// Send back Updated game to all clients
		return new MyGameOutput(this);
		
	}
	
	// Routine for adding a current player's pick to the RPS Rounds
	private boolean add2RpsRounds(int playerIndex, char pick)
	{
		// Must always have at least one
		if (rpsRounds.size() == 0)
			rpsRounds.add(new RpsRound());
		
		// If the last round is complete, then we need to start a new one
		RpsRound topRound = rpsRounds.get(rpsRounds.size()-1);
		if (topRound.isComplete())
			rpsRounds.add(new RpsRound());
		
		// Get the latest Round and try to add user's pick to it
		RpsRound currRound = rpsRounds.get(rpsRounds.size()-1);
		if (!currRound.insert(playerIndex, pick))
		{
			System.out.println("Player already made a pick on this round ... ignoring");
			return false;
		}
		return true;
	}

// The following will return the player's current index (0 or 1).  
// If the player is new and we don't yet have 2 players, then this player will be added
// and his index will be returned.
// If we already have 2 players and this is a new player, then -1 is returned.
	
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
				// Once we have 2 players, we will reset rpsRounds
				// This clears out games played with other opponents.
				rpsRounds.clear();
			}
		}
		return retval;
	}
	
	// If a player is already in the game, then his index is returned (0 or 1)
	// Otherwise, -1 is returned
	
	private int getYourIndex(String name)
	{
		return currPlayers.indexOf(name);
	}
	
	// Return the name of the other player
	
	private String otherPlayerName(String yourName)
	{
		if (currPlayers.size() < 2)
			return null;
		if (yourName.equals(currPlayers.get(0)))
			return currPlayers.get(1);
		else
			return currPlayers.get(0);
	}
	
	// Return a String with the results of all of the rounds.
	
	public String publish_status(String name)
	{
		String end_of_line = System.getProperty("line.separator");
		int yourIndex = getYourIndex(name);
		if (yourIndex < 0)
			return "Disconnected from Rock Paper Scissors";
		
		String otherName = otherPlayerName(name);
		
		StringBuilder sb = new StringBuilder();
		for (int i=0; i < rpsRounds.size(); i++)
		{
			RpsRound rr = rpsRounds.get(i);
			sb.append(rr.publish(yourIndex,  otherName)+end_of_line);
		}
		return sb.toString();
	}
}


