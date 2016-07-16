package GuiChat;
import java.io.Serializable;

public class MyGameOutput  implements Serializable
{
	private static final long serialVersionUID = 1L;
	String sendersMsg;    
    String sendersName;

    public void copyMsg(MyGameInput myInput)
    {
    	sendersName = myInput.myName;
    	sendersMsg = myInput.myMsg;
    }
    
    public String toString()
    {
    	return sendersName+ ": " + sendersMsg ;
    }    
}
