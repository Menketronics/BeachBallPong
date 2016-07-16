package rps;

import java.io.Serializable;


public class MyGameInput  implements Serializable
{
  
	private static final long serialVersionUID = 1L;
	
	static final char EXITING='e';
	static final char CONNECTING ='c';
	static final char PAPER ='p';
    static final char ROCK = 'r';
    static final char SCISSORS ='s';
    static final char NO_PICK=' ';
	
	String sendersName;
    char pick;

      
    public void setName(String name)
    {
        sendersName = name;
    }
    public void setPick(char pick)
    {
    	this.pick = pick;  
    }
    
}
