package chess_networked_pixels;

import java.io.Serializable;

import gameNet.*;

public class MyGameInput  implements Serializable
{
    static final int CONNECTING=0;
	static final int MOUSE_PRESSED=1;
    static final int MOUSE_RELEASED=2;
    static final int MOUSE_DRAGGED=3;
    
    String sendersName;
    int cmd=CONNECTING;
    int x,y;
    BoardDimensions boardDimensions;

    public void setName(String n)
    {
    	sendersName =n;
    }
    
    public void setMouseCmd(int cmd, int x, int y, BoardDimensions boardDimensions)
    {
    	this.cmd=cmd;
    	this.x=x;
    	this.y=y;
    	this.boardDimensions = boardDimensions;
    }
    
}
