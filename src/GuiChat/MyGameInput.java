package GuiChat;

import java.io.Serializable;


public class MyGameInput implements Serializable
{
	private static final long serialVersionUID = 1L;
	String myMsg;    
    String myName;

    
    public MyGameInput(String n)
    {
       myName = n;
    }
  
    
    public void setMsg(String m)
    {
        myMsg = m;
    }    
    
}