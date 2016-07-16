package Drawing_networked;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;



public class MyGameInput  implements Serializable
{
    static final int CONNECTING=0;
    static final int DISCONNECTING=1;
	static final int MOUSE_PRESSED=2;
    static final int MOUSE_RELEASED=3;
    static final int MOUSE_DRAGGED=4;
    static final int SET_COLOR=5;
    static final int SET_DRAW_TYPE=6;
    static final int SET_FILLED=7;
    
    String sendersName;
    int cmd=CONNECTING;
    
    Point point=new Point(-1,-1);
    DrawType drawType;
    boolean filled;
    Color color;
    
    public void setName(String n)
    {
    	sendersName =n;
    }
    public void setCmd(int cmd)
    {
    	this.cmd = cmd;
    }
    
    public void setMousePoint(int cmd, Point dp)
    {
    	this.cmd=cmd;
    	point = dp;
    }
    
}

