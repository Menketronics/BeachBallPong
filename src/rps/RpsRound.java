package rps;

import java.io.Serializable;

class RpsRound implements Serializable
{ 
	char[] picks = new char[2];
	RpsRound()
	{
		picks[0] = picks[1] = MyGameInput.NO_PICK;
	}
	boolean isComplete()
	{
		if (picks[0] == MyGameInput.NO_PICK || picks[1] == MyGameInput.NO_PICK)
			return false;
		else
			return true;
	}
	boolean insert(int index, char pick)
	{
		if (picks[index] == MyGameInput.NO_PICK)
		{
			picks[index] = pick;
			return true;
		}
		else
			return false; // Already filled in
	}
	String publish(int yourIndex,  String otherName)
	{
		int otherIndex;
		if (yourIndex == 1)
			otherIndex = 0;
		else
			otherIndex=1;
		
		char yourPick=picks[yourIndex];
		char otherPick = picks[otherIndex];
		String retStr="";
		if (otherName == null)
			return "Needing a Player";
		
		if (yourPick != MyGameInput.NO_PICK && otherPick !=  MyGameInput.NO_PICK)
		{
			retStr = "You:"+ choiceName(yourPick) +
				" -  " + otherName + ":" + choiceName(otherPick) + 
				" =====> " + rps_results(yourPick, otherPick);
		}
		else
		{
			retStr = "Waiting on: " + ((yourPick==MyGameInput.NO_PICK)? "YOU, ": "") +
					((otherPick==MyGameInput.NO_PICK)? otherName: "");
		}
		
		return retStr;				
	}
	
	
	
	public static String rps_results(char userInput,  char otherInput)
    {
        String response=" LOSE :-) !!";

        if ( userInput == otherInput)
        {
            response =  " Tied";
        }
        else
        {
            switch (userInput)
            {
            case MyGameInput.SCISSORS:
                if (otherInput == MyGameInput.PAPER)
                    response= " WIN!!";            
                break;
            case MyGameInput.ROCK:
                if (otherInput == MyGameInput.SCISSORS)
                    response =  " WIN!!";    
                break;
            case MyGameInput.PAPER:
                if (otherInput == MyGameInput.ROCK)
                    response = " WIN!!";
                break;
            default:
                response = " had bad input: " +userInput + 
                            " -- Enter r,p,s for MyGameInput.ROCK, MyGameInput.PAPER, MyGameInput.SCISSORS";
            }
        }
        return response;
    }
	public static String choiceName(char choice)
	{
		switch(choice)
		{
		case MyGameInput.PAPER:
			return "PAPER";
		case MyGameInput.ROCK:
			return "ROCK";
		case MyGameInput.SCISSORS:
			return "SCISSORS";
		case MyGameInput.NO_PICK:
			return "NO-PICK";
		default:
			return null;
		}
	}
}
