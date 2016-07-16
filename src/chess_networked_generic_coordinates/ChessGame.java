package chess_networked_generic_coordinates;

import java.awt.Graphics;
import java.io.Serializable;

public class ChessGame implements Serializable{
	private Piece[] pieces = new Piece[32];
	
	private int selectedIndex = -1;

	private DPoint pressed_point=new DPoint();
	private DPoint dragged_point = new DPoint();

	
	public ChessGame()
	{
		// Fill in all the pieces and their locations   
		int index=0;

		for (int i=0; i < 8; i++) // black pawns in row 1
		{
			pieces[index++] = new Piece(PieceType.Pawn, ColorType.black, i, 1);
		}
		for (int i=0; i < 8; i++) // white pawns in row 6
		{
			pieces[index++] = new Piece(PieceType.Pawn, ColorType.white, i, 6);
		}

		PieceType[] startingKingRow={PieceType.Rook, PieceType.Knight, PieceType.Bishop, 
				PieceType.Queen, PieceType.King, 
				PieceType.Bishop,PieceType.Knight,PieceType.Rook};

		for (int i=0; i < 8; i++) // black king row in row 0
		{
			pieces[index++] = new Piece(startingKingRow[i], ColorType.black, i, 0);
		}
		for (int i=0; i < 8; i++) // white king row in row 7
		{
			pieces[index++] = new Piece(startingKingRow[i], ColorType.white, i, 7);
		}

	}

	// Draws all pieces into their current location.
	public void drawInPosition(Graphics g, BoardDimensions chessBoard)
	{
		for (int i=0; i < pieces.length; i++)
		{
			if (i != selectedIndex)
				pieces[i].drawInPosition(g, chessBoard);
			else
			{
				// The selected piece is still being dragged around
				DPoint delta = pressed_point.deltaPoint(dragged_point);
				pieces[i].dragDraw(g, chessBoard, delta.x, delta.y);
			}
		}
	}



	
	void mousePressed(DPoint dpoint)
	{
		pressed_point = dpoint;
		dragged_point = dpoint;

		int xSelectLoc = (int)dpoint.x;
		int ySelectLoc = (int)dpoint.y;
		
		for (int i=0; i < pieces.length; i++)
		{
			if (pieces[i].areYouHere(xSelectLoc, ySelectLoc))
			{
				selectedIndex = i;
				break;
			}
		}
		
	}
	void mouseDragged(DPoint dpoint)
	{
		dragged_point = dpoint;
	}
	void mouseReleased(DPoint dpoint)
	{
		pressed_point = dpoint;
		if (selectedIndex >= 0)
		{
			int xSelectLoc = (int)dpoint.x;
			int ySelectLoc = (int)dpoint.y;
			if (xSelectLoc >= 0 && ySelectLoc >= 0) 
			{
				pieces[selectedIndex].moveLoc(xSelectLoc, ySelectLoc);
			}
			selectedIndex = -1;
		}
	}

	
}


