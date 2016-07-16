package Drawing_networked_cloneable;

import gameNet_cloneable.CloneableObject;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;



public class MyGameInput  extends CloneableObject implements Serializable
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
    public void myCloneMethod( Object obj)
    {
    	MyGameInput mgi = (MyGameInput)obj;
    	if (mgi.point != null)
    		mgi.point = (Point)mgi.point.clone();
    	if (mgi.color != null)
    		mgi.color = new Color(mgi.color.getRGB());
    	
    }
    
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

