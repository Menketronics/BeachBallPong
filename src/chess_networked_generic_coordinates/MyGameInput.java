package chess_networked_generic_coordinates;

import java.io.Serializable;



public class MyGameInput  implements Serializable
{
    static final int CONNECTING=0;
	static final int MOUSE_PRESSED=1;
    static final int MOUSE_RELEASED=2;
    static final int MOUSE_DRAGGED=3;
    
    String sendersName;
    int cmd=CONNECTING;
    DPoint dpoint=new DPoint(-1,-1);
    
    public void setName(String n)
    {
    	sendersName =n;
    }
    
    public void setMouseCmd(int cmd, DPoint dp)
    {
    	this.cmd=cmd;
    	dpoint = dp;
    }
    
}

class DPoint implements Serializable
{
	double x,y;
	DPoint()
	{
		x=-1;
		y=-1;
	}
	DPoint(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	DPoint deltaPoint(DPoint secondPoint)
	{
		double dx = secondPoint.x -x;
		double dy = secondPoint.y -y;
		return new DPoint(dx,dy);
	}
}
