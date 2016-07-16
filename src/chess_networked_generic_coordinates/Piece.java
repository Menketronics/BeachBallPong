package chess_networked_generic_coordinates;

import java.awt.Graphics;
import java.io.Serializable;

enum ColorType {black, white};
enum PieceType {Pawn, Rook, Knight, Bishop, Queen, King};



class Piece implements Serializable
{    
    int xSquare, ySquare;
    PieceType pieceType;  
    ColorType color;
    
    
    Piece (PieceType p, ColorType c, int xSquare, int ySquare)
    {
        this.pieceType=p;
        this.color =c;
        this.xSquare = xSquare;
        this.ySquare = ySquare;
    }
    
    void moveLoc(int x, int y)
    {
        xSquare = x;
        ySquare = y;
    }
    boolean areYouHere(int xSelectLoc, int ySelectLoc)
    {
        if (xSelectLoc == xSquare && ySelectLoc == ySquare)
            return true;
        else
            return false;
    }
    
     void drawInPosition(Graphics g, BoardDimensions boardDimensions)
     {
    	 boardDimensions.boardDraw(g, pieceType, color, xSquare, ySquare);
     }
     
     // The following will be used while we are dragging piece around
     void dragDraw(Graphics g, BoardDimensions boardDimensions,
             double xDelta, double yDelta)
     {
    	 boardDimensions.boardDraw(g, pieceType, color, xSquare+xDelta, ySquare+yDelta);
     }
     
}

