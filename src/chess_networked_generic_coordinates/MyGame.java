package chess_networked_generic_coordinates;


import java.awt.Graphics;
import java.io.Serializable;

import gameNet.*;


public class MyGame extends GameNet_CoreGame implements Serializable{
	ChessGame chessGame= new ChessGame();


	public Object process(Object ob)
	{
		MyGameInput myGameInput= (MyGameInput)ob;
		switch(myGameInput.cmd)
		{
		case MyGameInput.MOUSE_PRESSED:
			chessGame.mousePressed(myGameInput.dpoint);
			break;
		case MyGameInput.MOUSE_RELEASED:
			chessGame.mouseReleased(myGameInput.dpoint);
			break;
		case MyGameInput.MOUSE_DRAGGED:
			chessGame.mouseDragged(myGameInput.dpoint);
			break;
		case MyGameInput.CONNECTING:
			break;
		}

		return new MyGameOutput(this);
	}




}


